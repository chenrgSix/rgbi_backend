package com.rg.smarts.application.aimodel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.interfaces.dto.dialogues.DialoguesQueryRequest;
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

    Page<DialogueSummaryVO> getBatchOfChatList(DialoguesQueryRequest dialoguesQueryRequest, HttpServletRequest request);

    DialoguesVO getDialogueById(Long memoryId, HttpServletRequest request);

    Long getMemoryIdOrAdd(String content, Long id, Long memoryId, List<Long> kbIds);

    Boolean deleteDialogueById(DeleteRequest deleteRequest, HttpServletRequest request);
}
