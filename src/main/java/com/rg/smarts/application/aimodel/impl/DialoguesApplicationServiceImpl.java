package com.rg.smarts.application.aimodel.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.aimodel.DialoguesApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.aimodel.entity.Dialogues;
import com.rg.smarts.domain.aimodel.service.DialoguesDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.interfaces.dto.dialogues.DialoguesQueryRequest;
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
    public Page<DialogueSummaryVO> getBatchOfChatList(DialoguesQueryRequest dialoguesQueryRequest,HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);

        int current = dialoguesQueryRequest.getCurrent();
        int pageSize = dialoguesQueryRequest.getPageSize();

        Page<Dialogues> dialoguesPage = dialoguesDomainService.getBatchOfChatList(current,pageSize,loginUser.getId());

        List<DialogueSummaryVO> dialogueSummaryVOList = dialoguesPage.getRecords().stream().map(dialogues -> {
            DialogueSummaryVO dialogueSummaryVO = new DialogueSummaryVO();
            BeanUtils.copyProperties(dialogues, dialogueSummaryVO);
            return dialogueSummaryVO;
        }).toList();
        Page<DialogueSummaryVO> dialogueSummaryVOPage = new Page<>(current, pageSize, dialoguesPage.getTotal());
        dialogueSummaryVOPage.setRecords(dialogueSummaryVOList);
        return dialogueSummaryVOPage;
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

    @Override
    public Boolean deleteDialogueById(DeleteRequest deleteRequest, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        return dialoguesDomainService.deleteDialogueById(deleteRequest.getId(), loginUser.getId());

    }
}
