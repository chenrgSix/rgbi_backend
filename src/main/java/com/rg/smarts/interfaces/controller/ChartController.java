package com.rg.smarts.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.chart.ChartApplicationService;
import com.rg.smarts.domain.chart.entity.Chart;
import com.rg.smarts.domain.user.constant.UserConstant;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.interfaces.dto.chart.*;
import com.rg.smarts.interfaces.vo.BiResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图表接口
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartApplicationService chartApplicationService;
//    @Resource
//    private AiManager aiManager;
//    @GetMapping("/ai/generate")
//    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
//        return aiManager.chatAiService(message, "萌妹","傲娇",1L);
//    }

    /**
     * 智能分析(异步)
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async")
    public BaseResponse<BiResponse> genChartBuAiAsync(@RequestPart("file") MultipartFile multipartFile,
                                                      GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        BiResponse biResponse = chartApplicationService.genChartBuAiAsync(multipartFile, genChartByAiRequest, request);
        return ResultUtils.success(biResponse);
    }

    /**
     * 智能分析(消息队列异步)
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async/mq")
    public BaseResponse<BiResponse> genChartBuAiAsyncMq(@RequestPart("file") MultipartFile multipartFile,
                                                        GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {

        BiResponse chart = chartApplicationService.genChartBuAiAsyncMq(multipartFile, genChartByAiRequest, request);
        return ResultUtils.success(chart);

    }


    @PostMapping("/gen/retry")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<BiResponse> retryGenChart(@RequestBody final ChartRetryRequest chartQueryRequest, HttpServletRequest request) {
        BiResponse chart = chartApplicationService.retryGenChart(chartQueryRequest, request);
        return ResultUtils.success(chart);
    }

    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        Long l = chartApplicationService.addChart(chartAddRequest, request);
        return ResultUtils.success(l);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        boolean b = chartApplicationService.deleteChart(deleteRequest, request);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        boolean result = chartApplicationService.updateChart(chartUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(long id, HttpServletRequest request) {
        Chart chart = chartApplicationService.getChartById(id, request);
        return ResultUtils.success(chart);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param chartQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        Page<Chart> chartPage = chartApplicationService.listChartByPage(chartQueryRequest);
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Chart>> listChartByPageVo(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        Page<Chart> chartPage = chartApplicationService.listChartByPageVo(chartQueryRequest, request);
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        Page<Chart> chartPage = chartApplicationService.listMyChartByPage(chartQueryRequest, request);
        return ResultUtils.success(chartPage);
    }

    /**
     * 编辑（用户）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        Boolean b = chartApplicationService.editChart(chartEditRequest, request);
        return ResultUtils.success(b);
    }

}
