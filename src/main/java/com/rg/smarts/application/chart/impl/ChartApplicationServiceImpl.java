package com.rg.smarts.application.chart.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.application.user.ScoreApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.application.chart.ChartApplicationService;
import com.rg.smarts.domain.chart.service.ChartDomainService;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.infrastructure.constant.CommonConstant;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.infrastructure.manager.RedisLimiterManager;
import com.rg.smarts.infrastructure.utils.SqlUtils;
import com.rg.smarts.interfaces.dto.chart.*;
import com.rg.smarts.interfaces.vo.BiResponse;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.domain.chart.entity.Chart;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
* @author czr
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-03-07 15:31:39
*/
@Service
@Slf4j
public class ChartApplicationServiceImpl implements ChartApplicationService {
    @Resource
    private ChartDomainService chartDomainService;
    @Lazy
    @Resource
    private UserApplicationService userApplicationService;
    @Lazy
    @Resource
    private ScoreApplicationService scoreApplicationService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Resource
    private AiModelApplicationService aiModelApplicationService;



    @Override
    public BiResponse retryGenChart(ChartRetry chartRetry) {
        ThrowUtils.throwIf(chartRetry == null, ErrorCode.PARAMS_ERROR);
        Long chartId = chartRetry.getChartId();
        chartDomainService.retryGenChart(chartId);
        return new BiResponse(chartId);
    }

    @Override
    public BiResponse retryGenChart(ChartRetryRequest chartQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(chartQueryRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userApplicationService.getLoginUser(request);
        Boolean checkResult = userApplicationService.checkUserPoints(loginUser);
        ThrowUtils.throwIf(!checkResult, ErrorCode.OPERATION_ERROR,"积分不足");
        ChartRetry chartRetry = new ChartRetry(chartQueryRequest.getId(), loginUser);
        return this.retryGenChart(chartRetry);
    }
    public void validGenChartFile(GenChartByAiRequest genChartByAiRequest,MultipartFile multipartFile) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx","xls","csv");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");
    }

    @Override
    public BiResponse genChartBuAiAsync(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        //校验入参
        validGenChartFile(genChartByAiRequest,multipartFile);
        User loginUser = userApplicationService.getLoginUser(request);
        Boolean checkResult = userApplicationService.checkUserPoints(loginUser);
        ThrowUtils.throwIf(!checkResult, ErrorCode.OPERATION_ERROR,"积分不足");
        // 限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(csvData).append("\n");
        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus("wait");
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartDomainService.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        //todo 处理任务队列满后抛异常情况
        CompletableFuture.runAsync(()->{//使用线程池
            //修改图表状态为执行中
            Chart updateChart = new Chart();
            updateChart.setId(chart.getId());
            updateChart.setStatus("running");
            boolean b = chartDomainService.updateById(updateChart);
            if (!b){
                chartDomainService.handleChartUpdateError(chart.getId(),"更新图表失败状态失败");
                return;
            }
            //调用Ai
            String result = aiModelApplicationService.genChart(userInput.toString(),loginUser.getId());
            scoreApplicationService.deductPoints(loginUser.getId(), 20L);//调用成功后扣除积分
            String[] split = result.split("￥￥￥￥￥");
            if (split.length<3){
                chartDomainService.handleChartUpdateError(chart.getId(),"AI生成失败");
                return;
            }
            String genChart = split[1].trim();
            String genResult= split[2].trim();
            Chart updateChartResult = new Chart();
            updateChartResult.setId(chart.getId());
            updateChartResult.setGenChart(genChart);
            updateChartResult.setGenResult(genResult);
            updateChartResult.setStatus("succeed");
            boolean updateResult = chartDomainService.updateById(updateChartResult);
            if(!updateResult){
                chartDomainService.handleChartUpdateError(chart.getId(),"更新图表成功状态失败");
            }
        },threadPoolExecutor);
        BiResponse biResponse =new BiResponse();
        biResponse.setChartId(chart.getId());
        return biResponse;
    }

    /**
     * 向 RabbitMQ 发送消息
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BiResponse getChartMQ(final MultipartFile multipartFile, final GenChartByAiRequest genChartByAiRequest) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        User loginUser = genChartByAiRequest.getLoginUser();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
        // 构造用户输入\获取压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus("wait");
        chart.setUserId(loginUser.getId());
        Long newChartId = chartDomainService.getChartMQ(chart);
        return new BiResponse(newChartId);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BiResponse genChartBuAiAsyncMq(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        // 校验入参
        validGenChartFile(genChartByAiRequest,multipartFile);
        //登录用户获取
        User loginUser = userApplicationService.getLoginUser(request);
        Boolean checkResult = userApplicationService.checkUserPoints(loginUser);
        ThrowUtils.throwIf(!checkResult, ErrorCode.OPERATION_ERROR,"积分不足");
        genChartByAiRequest.setLoginUser(loginUser);
        // 限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
        return this.getChartMQ(multipartFile, genChartByAiRequest);
    }


    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chartQueryRequest.getId();
        String goal = chartQueryRequest.getGoal();
        String name = chartQueryRequest.getName();
        Long userId = chartQueryRequest.getUserId();
        String chartType = chartQueryRequest.getChartType();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();
        queryWrapper.eq(id!=null && id > 0,"id",id);
        queryWrapper.eq(StringUtils.isNotBlank(goal),"goal",goal);
        queryWrapper.like(StringUtils.isNotBlank(name),"name",name);
        queryWrapper.eq(StringUtils.isNotBlank(chartType),"chartType",chartType);

        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    @Override
    public Boolean editChart(ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userApplicationService.getLoginUser(request);
        long id = chartEditRequest.getId();

        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        // 判断是否存在
        Chart oldChart = chartDomainService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userApplicationService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return chartDomainService.updateById(chart);
    }
    @Override
    public Page<Chart> listMyChartByPage(ChartQueryRequest chartQueryRequest,
                                         HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userApplicationService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartDomainService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return chartPage;
    }
    @Override
    public Page<Chart> listChartByPageVo(ChartQueryRequest chartQueryRequest,
                                       HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartDomainService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return chartPage;
    }
    @Override
    public Page<Chart> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        return chartDomainService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));

    }

    @Override
    public Boolean updateChart(ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        long id = chartUpdateRequest.getId();
        return chartDomainService.updateChart(id,chart);
    }
    @Override
    public Chart getChartById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartDomainService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return chart;
    }
    @Override
    public Boolean deleteChart(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userApplicationService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartDomainService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userApplicationService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return chartDomainService.removeById(id);
    }
    @Override
    public Long addChart(ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userApplicationService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartDomainService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return chart.getId();
    }

    @Override
    public long count(QueryWrapper<Chart> queryWrapper) {
        return chartDomainService.count(queryWrapper);
    }

    @Override
    public Boolean updateById(Chart updateChartResult) {
        return chartDomainService.updateById(updateChartResult);
    }

    @Override
    public Chart getById(String message) {
        return chartDomainService.getById(Long.parseLong(message));
    }
}




