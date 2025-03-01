package com.rg.smarts.infrastructure.repository.dialogues;

import com.rg.smarts.domain.dialogues.entity.Dialogues;
import com.rg.smarts.domain.dialogues.repository.DialoguesRepository;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

@Repository
public class PersistentChatMemoryStore implements ChatMemoryStore {
    @Resource
    private DialoguesRepository dialoguesRepository;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        Dialogues dialogues = dialoguesRepository.getById((Long) memoryId);
        String json = dialogues.getChatContent();
        return messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String json = messagesToJson(messages);
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