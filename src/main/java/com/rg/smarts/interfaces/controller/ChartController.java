package com.rg.smarts.interfaces.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.interfaces.dto.chart.*;
import com.rg.smarts.interfaces.vo.BiResponse;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.infrastructure.constant.CommonConstant;
import com.rg.smarts.domain.user.constant.UserConstant;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.infrastructure.manager.AiManager;
import com.rg.smarts.infrastructure.manager.RedisLimiterManager;
import com.rg.smarts.domain.chart.entity.Chart;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.application.chart.ChartApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.infrastructure.utils.ExcelUtils;
import com.rg.smarts.infrastructure.utils.SqlUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 图表接口
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartApplicationService chartApplicationService;

//    @GetMapping("/ai/generate")
//    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        ChatResponse call = this.chatModel.call(new Prompt(
//                message + " 在回答的结尾要加一句你好棒棒",
//                ZhiPuAiChatOptions.builder()
//                        .temperature(0.5)
//                        .build()
//        ));
//
//        AssistantMessage output = call.getResult().getOutput();
//        return output.getText();
//    }

    /**
     * 智能分析(异步)
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async")
    public BaseResponse<BiResponse> genChartBuAiAsync(@RequestPart("file") MultipartFile multipartFile,
                                                 GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        BiResponse biResponse=chartApplicationService.genChartBuAiAsync(multipartFile, genChartByAiRequest, request);
        return ResultUtils.success(biResponse);
    }

    /**
     * 智能分析(消息队列异步)
     * @param multipartFile
     * @param genChartByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async/mq")
    public BaseResponse<BiResponse> genChartBuAiAsyncMq(@RequestPart("file") MultipartFile multipartFile,
                                                      GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {

        BiResponse chart=chartApplicationService.genChartBuAiAsyncMq(multipartFile, genChartByAiRequest, request);
        return ResultUtils.success(chart);

    }


    @PostMapping("/gen/retry")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<BiResponse> retryGenChart(@RequestBody final ChartRetryRequest chartQueryRequest, HttpServletRequest request) {
        BiResponse chart=chartApplicationService.retryGenChart(chartQueryRequest, request);
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
        boolean b = chartApplicationService.deleteChart(deleteRequest,request);
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
        Chart chart = chartApplicationService.getChartById(id,request);
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
