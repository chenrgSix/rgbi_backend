package com.rg.smarts.domain.dialogues.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.dialogues.entity.Dialogues;
import com.rg.smarts.domain.dialogues.repository.DialoguesRepository;
import com.rg.smarts.domain.dialogues.service.DialoguesDomainService;
import com.rg.smarts.infrastructure.aiservice.AiChatTemplate;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.infrastructure.manager.RedisLimiterManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author czr
 * @description 聊天会话的服务实现类
 * @createDate 2024-03-18 16:44:12
 */
@Service
public class DialoguesDomainServiceImpl implements DialoguesDomainService {
    @Resource
    private DialoguesRepository dialoguesRepository;
    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private AiChatTemplate aiChatTemplate;

    @Override
    public String chat(String content, Long userId, Long memoryId) {
        redisLimiterManager.doRateLimit("chat_" + userId);
        if (memoryId == null) {
            // 获取会话id，没有的话就生成
            memoryId = this.addDialoguesByUserId(content, userId);
        } else {
            Boolean b = this.existDialoguesByUserId(memoryId, userId);
            ThrowUtils.throwIf(!b, ErrorCode.PARAMS_ERROR, "对话不存在");
        }

        return aiChatTemplate.chat(
                content, memoryId
        );
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
    public Boolean existDialoguesByUserId(Long memoryId, Long userId) {
        // 确保字符串长度大于或等于10
        LambdaQueryWrapper<Dialogues> dialoguesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dialoguesLambdaQueryWrapper.eq(Dialogues::getId, memoryId);
        dialoguesLambdaQueryWrapper.eq(Dialogues::getUserId, userId);
        Dialogues dialogues = dialoguesRepository.getOne(dialoguesLambdaQueryWrapper);
        return dialogues != null;
    }

    @Override
    public Dialogues getDialoguesById(Long memoryId) {
        return dialoguesRepository.getById(memoryId);
    }

    @Override
    public List<Dialogues> getBatchOfChatList(Long userId) {
        LambdaQueryWrapper<Dialogues> dialoguesLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dialoguesLambdaQueryWrapper.orderByDesc(Dialogues::getCreateTime);
        Page<Dialogues> dialoguesPage = dialoguesRepository.page(new Page<>(1, 12),
                dialoguesLambdaQueryWrapper);
        return dialoguesPage.getRecords();
    }
}
