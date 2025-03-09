package com.rg.smarts.interfaces.controller;

import com.rg.smarts.application.dialogues.DialoguesApplicationService;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;
import com.rg.smarts.interfaces.vo.dialogues.DialoguesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("chat/list")
    public BaseResponse<List<DialogueSummaryVO>> getChatList(HttpServletRequest request) {
        List<DialogueSummaryVO> result = dialoguesApplicationService.getBatchOfChatList(request);
        return ResultUtils.success(result);
    }

    @GetMapping("chat/info")
    public BaseResponse<DialoguesVO> getDialogueById(Long memoryId, HttpServletRequest request) {
        DialoguesVO result = dialoguesApplicationService.getDialogueById(memoryId, request);
        return ResultUtils.success(result);
    }



}
