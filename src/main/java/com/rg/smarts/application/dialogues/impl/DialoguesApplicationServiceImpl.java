package com.rg.smarts.application.dialogues.impl;

import com.rg.smarts.application.dialogues.DialoguesApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.dialogues.entity.Dialogues;
import com.rg.smarts.domain.dialogues.service.DialoguesDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;
import com.rg.smarts.interfaces.vo.dialogues.DialoguesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description:
 */
@Service
public class DialoguesApplicationServiceImpl implements DialoguesApplicationService {
    @Autowired
    public DialoguesDomainService dialoguesDomainService;
    @Resource
    private UserApplicationService userApplicationService;


    @Override
    public List<DialogueSummaryVO> getBatchOfChatList(HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        List<Dialogues> records = dialoguesDomainService.getBatchOfChatList(loginUser.getId());
        return records.stream().map(dialogues -> {
            DialogueSummaryVO dialogueSummaryVO = new DialogueSummaryVO();
            BeanUtils.copyProperties(dialogues, dialogueSummaryVO);
            return dialogueSummaryVO;
        }).toList();
    }

    @Override
    public DialoguesVO getDialogueById(Long memoryId, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        Dialogues dialogues = dialoguesDomainService.getDialoguesById(memoryId);
        ThrowUtils.throwIf(!Objects.equals(dialogues.getUserId(), loginUser.getId()), ErrorCode.OPERATION_ERROR, "您并无该对话内容");
        DialoguesVO dialoguesVO = new DialoguesVO();
        BeanUtils.copyProperties(dialogues, dialoguesVO);
        return dialoguesVO;
    }

    @Override
    public Long getMemoryIdOrAdd(String content, Long id, Long memoryId) {
        return dialoguesDomainService.getMemoryIdOrAdd(content, id, memoryId);
    }
}
