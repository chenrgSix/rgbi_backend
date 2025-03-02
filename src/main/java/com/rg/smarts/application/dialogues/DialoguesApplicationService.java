package com.rg.smarts.application.dialogues;

import com.rg.smarts.interfaces.vo.dialogues.ChatVO;
import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;
import com.rg.smarts.interfaces.vo.dialogues.DialoguesVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description:
 */
public interface DialoguesApplicationService {
    ChatVO chat(Long memoryId,String content, HttpServletRequest request) ;

    void chatStream(Long memoryId, String content, SseEmitter sseEmitter, HttpServletRequest request);


    List<DialogueSummaryVO> getBatchOfChatList(HttpServletRequest request);

    DialoguesVO getDialogueById(Long memoryId, HttpServletRequest request);
}
