package com.rg.smarts.domain.dialogues.service;

import com.rg.smarts.domain.dialogues.entity.Dialogues;
import com.rg.smarts.interfaces.vo.dialogues.ChatVO;
import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;

import java.util.List;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service
* @createDate 2024-03-18 16:44:12
*/
public interface DialoguesDomainService {

    String chat(String content, Long userId, Long memoryId);

    Long addDialoguesByUserId(String chatContent, Long userId);

    Boolean existDialoguesByUserId(Long memoryId, Long userId);

    Dialogues getDialoguesById(Long memoryId);

    List<Dialogues> getBatchOfChatList(Long userId);
}
