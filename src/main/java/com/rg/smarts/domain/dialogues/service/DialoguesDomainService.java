package com.rg.smarts.domain.dialogues.service;

import com.rg.smarts.domain.dialogues.entity.Dialogues;
import dev.langchain4j.service.TokenStream;

import java.util.List;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service
* @createDate 2024-03-18 16:44:12
*/
public interface DialoguesDomainService {

    Long getMemoryIdOrAdd(String content, Long userId, Long memoryId);

    String chat(String content, Long userId, Long memoryId);

    TokenStream chatStream(String content, Long userId, Long memoryId);

    Long addDialoguesByUserId(String chatContent, Long userId);

    Dialogues getDialoguesById(Long memoryId);

    List<Dialogues> getBatchOfChatList(Long userId);
}
