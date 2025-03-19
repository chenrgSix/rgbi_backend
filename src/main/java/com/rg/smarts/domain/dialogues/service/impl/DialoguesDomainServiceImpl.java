package com.rg.smarts.domain.dialogues.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.dialogues.entity.Dialogues;
import com.rg.smarts.domain.dialogues.repository.DialoguesRepository;
import com.rg.smarts.domain.dialogues.service.DialoguesDomainService;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author czr
 * @description 聊天会话的服务实现类
 * @createDate 2024-03-18 16:44:12
 */
@Service
public class DialoguesDomainServiceImpl implements DialoguesDomainService {
    @Resource
    private DialoguesRepository dialoguesRepository;

    @Override
    public Long getMemoryIdOrAdd(String content, Long userId, Long memoryId) {
        // 获取会话id，没有的话就生成
        if (memoryId == null) {
            return addDialoguesByUserId(content, userId);
        }
        LambdaQueryWrapper<Dialogues> dialoguesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dialoguesLambdaQueryWrapper.eq(Dialogues::getId, memoryId);
        dialoguesLambdaQueryWrapper.eq(Dialogues::getUserId, userId);
        Dialogues dialogues = dialoguesRepository.getOne(dialoguesLambdaQueryWrapper);
        ThrowUtils.throwIf(dialogues==null, ErrorCode.PARAMS_ERROR, "对话不存在");
        return memoryId;
    }

    @Override
    public Long addDialoguesByUserId(String chatContent, Long userId) {
        // 确保字符串长度大于或等于10
        if (chatContent.length() >= 10) {
            // 截取前十个字符
            chatContent = chatContent.substring(0, 10);
        }
        Dialogues dialogues = new Dialogues();
        dialogues.setChatTitle(chatContent);
        dialogues.setUserId(userId);
        boolean saveResult = dialoguesRepository.save(dialogues);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息创建失败，数据库错误");
        }
        return dialogues.getId();
    }

    @Override
    public Dialogues getDialoguesById(Long memoryId) {
        return dialoguesRepository.getById(memoryId);
    }

    @Override
    public Page<Dialogues> getBatchOfChatList( int current,int pageSize,Long userId) {
        LambdaQueryWrapper<Dialogues> dialoguesLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dialoguesLambdaQueryWrapper.orderByDesc(Dialogues::getCreateTime);
        Page<Dialogues> dialoguesPage = dialoguesRepository.page(new Page<>(current, pageSize),
                dialoguesLambdaQueryWrapper);
        return dialoguesPage;
    }

    @Override
    public Boolean deleteDialogueById(Long id, Long userId) {
        Dialogues optById = dialoguesRepository.getById(id);
        ThrowUtils.throwIf(optById==null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!optById.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR);
        return dialoguesRepository.removeById(id);
    }
}
