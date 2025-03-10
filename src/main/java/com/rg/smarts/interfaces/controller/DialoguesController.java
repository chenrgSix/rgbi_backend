package com.rg.smarts.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.dialogues.DialoguesApplicationService;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.interfaces.dto.chart.ChartQueryRequest;
import com.rg.smarts.interfaces.dto.dialogues.DialoguesQueryRequest;
import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;
import com.rg.smarts.interfaces.vo.dialogues.DialoguesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description: ai聊天对话
 */
@Slf4j
@RestController
@RequestMapping("dialogues")
public class DialoguesController {
    @Resource
    private DialoguesApplicationService dialoguesApplicationService;


    @PostMapping("chat/list")
    public BaseResponse<Page<DialogueSummaryVO>> getChatList(@RequestBody DialoguesQueryRequest dialoguesQueryRequest, HttpServletRequest request) {
        Page<DialogueSummaryVO> batchOfChatList = dialoguesApplicationService.getBatchOfChatList(dialoguesQueryRequest, request);
        return ResultUtils.success(batchOfChatList);
    }

    @GetMapping("chat/info")
    public BaseResponse<DialoguesVO> getDialogueById(Long memoryId, HttpServletRequest request) {
        DialoguesVO result = dialoguesApplicationService.getDialogueById(memoryId, request);
        return ResultUtils.success(result);
    }
    @PostMapping("chat/delete")
    public BaseResponse<Boolean> deleteDialogueById(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        Boolean result = dialoguesApplicationService.deleteDialogueById(deleteRequest, request);
        return ResultUtils.success(result);
    }


}
