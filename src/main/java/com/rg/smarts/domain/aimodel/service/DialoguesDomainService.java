package com.rg.smarts.domain.aimodel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.aimodel.entity.Dialogues;

import java.util.List;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service
* @createDate 2024-03-18 16:44:12
*/
public interface DialoguesDomainService {

    Long getMemoryIdOrAdd(String content, Long userId, Long memoryId, List<Long> kbIds);

    Long addDialoguesByUserId(String chatContent, Long userId, List<Long> kbIds);

    Dialogues getDialoguesById(Long memoryId);

    Page<Dialogues> getBatchOfChatList(int current, int pageSize, Long userId);

    Boolean deleteDialogueById(Long id, Long id1);
}
