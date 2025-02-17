package com.rg.smarts.domain.chart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.chart.constant.BiMqConstant;
import com.rg.smarts.domain.chart.constant.ChartConstant;
import com.rg.smarts.domain.chart.entity.Chart;
import com.rg.smarts.domain.chart.repository.ChartRepository;
import com.rg.smarts.domain.chart.service.ChartDomainService;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.infrastructure.manager.AiManager;
import com.rg.smarts.infrastructure.mq.BiMessageProducer;
import com.rg.smarts.infrastructure.utils.ExcelUtils;
import com.rg.smarts.interfaces.dto.chart.GenChartByAiRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
* @author czr
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-03-07 15:31:39
*/
@Service
@Slf4j
public class ChartDomainServiceImpl implements ChartDomainService {
    @Resource
    private ChartRepository chartRepository;
    @Resource
    private BiMessageProducer biMessageProducer;

    /**
     * 重新生成图表
     * @param chartId
     */
    @Override
    public void retryGenChart(Long chartId) {
        chartRepository.updateById(new Chart(ChartConstant.CHART_STATUS_WAIT, "", "", "", chartId));
        biMessageProducer.sendMessage(BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY, String.valueOf(chartId));
    }
    @Override
    public void handleChartUpdateError(long chartId, String execMessage){
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setExecMessage(execMessage);
        updateChartResult.setStatus("failed");
        boolean updateResult = chartRepository.updateById(updateChartResult);
        if (!updateResult){
            log.error("更新图表失败状态失败"+chartId+execMessage);
        }
    }

    /**
     * 向 RabbitMQ 发送消息
     * @param chart
     * @return
     */
    @Override
    public Long getChartMQ(Chart chart) {
        boolean saveResult = chartRepository.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        long newChartId = chart.getId();
        biMessageProducer.sendMessage(BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY, String.valueOf(newChartId));
        return newChartId;
    }
    @Override
    public Boolean updateChart(Long id, Chart chart) {
        // 判断是否存在
        Chart oldChart = chartRepository.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        return chartRepository.updateById(chart);
    }

    @Override
    public Boolean updateById(Chart chart) {
        return chartRepository.updateById(chart);
    }

    @Override
    public Boolean save(Chart chart) {
        return chartRepository.save(chart);
    }

    @Override
    public Boolean removeById(long id) {
        return chartRepository.removeById(id);
    }

    @Override
    public Chart getById(long id) {
        return chartRepository.getById(id);
    }

    @Override
    public Page<Chart> page(Page<Chart> page, QueryWrapper<Chart> queryWrapper) {
        return chartRepository.page(page, queryWrapper);
    }

    @Override
    public long count(QueryWrapper<Chart> queryWrapper) {
        return chartRepository.count(queryWrapper);
    }


    private String formatExcelData(final MultipartFile multipartFile,final GenChartByAiRequest genChartByAiRequest){
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append(AiManager.PRECONDITION);
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，需要生成图表的类型是" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String data = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(data).append("\n");
        return String.valueOf(userInput);
    }
}




