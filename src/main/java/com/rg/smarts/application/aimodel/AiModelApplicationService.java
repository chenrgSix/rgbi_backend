package com.rg.smarts.application.aimodel;

import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.interfaces.dto.ai.AiModelAddRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
* @author czr
* @description 针对表【AiModel(Ai模型表)】的数据库操作Service
*/
public interface AiModelApplicationService {

    String genChart(String message, Long userId);
    AiModel addOne(AiModelAddRequest aiModelAddRequest);

    void chatStream(Long memoryId, String content, SseEmitter sseEmitter, HttpServletRequest request);

    void init();
}
