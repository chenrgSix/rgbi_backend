package com.rg.smarts.application.dialogues;

import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;
import com.rg.smarts.interfaces.vo.dialogues.DialoguesVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description:
 */
public interface DialoguesApplicationService {

    List<DialogueSummaryVO> getBatchOfChatList(HttpServletRequest request);

    DialoguesVO getDialogueById(Long memoryId, HttpServletRequest request);

    Long getMemoryIdOrAdd(String content, Long id, Long memoryId);
}
