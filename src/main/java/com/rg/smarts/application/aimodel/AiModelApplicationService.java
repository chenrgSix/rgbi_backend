package com.rg.smarts.application.aimodel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.interfaces.dto.ai.AiModelAddRequest;
import com.rg.smarts.interfaces.dto.ai.AiModelQueryRequest;
import com.rg.smarts.interfaces.dto.ai.AiModelUpdateRequest;
import com.rg.smarts.interfaces.dto.ai.ChatRequest;
import com.rg.smarts.interfaces.vo.ai.AiModelVO;
import com.rg.smarts.interfaces.vo.ai.LLMModelVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
* @author czr
* @description 针对表【AiModel(Ai模型表)】的数据库操作Service
*/
public interface AiModelApplicationService {

    String genChart(String message, Long userId);
    AiModel addOne(AiModelAddRequest aiModelAddRequest);

    Boolean updateAiModel(AiModelUpdateRequest aiModelUpdateRequest);

    AiModelVO getAiModelById(Long aiModelId);

    List<LLMModelVo> getSupportLLMModel();

    void chatStream(ChatRequest chatRequest, SseEmitter sseEmitter, HttpServletRequest request);

    void init();

    Page<AiModel> listAiModelByPage(AiModelQueryRequest aiModelQueryRequest);
}
