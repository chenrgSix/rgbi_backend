package com.rg.smarts.infrastructure.repository.aimodel;

import com.rg.smarts.domain.aimodel.entity.Dialogues;
import com.rg.smarts.domain.aimodel.repository.DialoguesRepository;
import com.rg.smarts.infrastructure.utils.RedisUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

@Repository
public class PersistentChatMemoryStore implements ChatMemoryStore {
    @Resource
    private DialoguesRepository dialoguesRepository;
    @Resource
    private RedisUtil redisUtil;

    private static final Integer STORE_CACHE_TIME = 5;  //分钟为单位
    private static final String STORE_CACHE_PREFIX = "rg:memory:store:";  //分钟为单位


    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String json = (String)redisUtil.get(STORE_CACHE_PREFIX + memoryId);
        if (json == null) {
            Dialogues dialogues = dialoguesRepository.getById((Long) memoryId);
            json = dialogues.getChatContent();
        }
        return messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        /**
         *   自己实现RAG如果将SystemMessage去掉会导致手动拼凑的RAG检索消息会在上下文中丢失
         *   langchain4j提供的RAG直接拼接在用户信息上，需要手动进行字符串裁剪，所以我选择的自己实现
         */
        if (messages.size() > 0 && messages.get(0) instanceof AiMessage) {
            messages.remove(0);
        }
        List<ChatMessage> usefulData = new ArrayList<>();
        for (ChatMessage chatMessage : messages) {
            // TODO 是否保存在此引用的知识文档ID
            if (!(chatMessage instanceof SystemMessage)) {
                usefulData.add(chatMessage);
            }
        }
        redisUtil.set(STORE_CACHE_PREFIX + memoryId,
                messagesToJson(messages),
                STORE_CACHE_TIME
        );
        String json = messagesToJson(usefulData);
        System.out.println("====================="+messages);
        Dialogues dialogues = new Dialogues();
        dialogues.setId((Long) memoryId);
        dialogues.setChatContent(json);
        dialoguesRepository.updateById(dialogues);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        dialoguesRepository.removeById((Long) memoryId);
    }
}