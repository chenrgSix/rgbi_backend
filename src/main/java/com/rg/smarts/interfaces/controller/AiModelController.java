package com.rg.smarts.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.user.constant.UserConstant;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.interfaces.dto.ai.AiModelAddRequest;
import com.rg.smarts.interfaces.dto.ai.AiModelQueryRequest;
import com.rg.smarts.interfaces.dto.ai.AiModelUpdateRequest;
import com.rg.smarts.interfaces.dto.ai.ChatRequest;
import com.rg.smarts.interfaces.vo.ai.AiModelVO;
import com.rg.smarts.interfaces.vo.ai.LLMModelVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @Author: czr
 * @CreateTime: 2025-03-09
 * @Description: ai模型相关功能
 */
@RestController
@RequestMapping("/aimodel")
@Slf4j
public class AiModelController {
    @Resource
    private AiModelApplicationService aiModelApplicationService;
    @PostMapping(value = "chat/steam")
    public SseEmitter chatStream(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        // langchain4j已经默认帮我们引入RxJava了，我们就不要自己引入了
        SseEmitter sseEmitter = new SseEmitter(0L);// 0L表示不设置超时时间
        sseEmitter.onCompletion(() -> {
            log.info("onCompletion:{} 结束", chatRequest.getMemoryId());
        });
        aiModelApplicationService.chatStream(chatRequest, sseEmitter, request);
        return sseEmitter;
    }
    @GetMapping(value = "llm/list")
    public BaseResponse<List<LLMModelVo>> getSupportLLMModel() {
        List<LLMModelVo> supportLLMModel = aiModelApplicationService.getSupportLLMModel();
        return ResultUtils.success(supportLLMModel);
    }
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<AiModel>> listAiModelByPage(@RequestBody AiModelQueryRequest aiModelQueryRequest) {
        Page<AiModel> aiModelPage=aiModelApplicationService.listAiModelByPage(aiModelQueryRequest);
        return ResultUtils.success(aiModelPage);
    }
    @PostMapping(value = "/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addAiModel(@RequestBody AiModelAddRequest aiModelUpdateRequest,HttpServletRequest request) {
        AiModel aiModel = aiModelApplicationService.addOne(aiModelUpdateRequest,request);
        return ResultUtils.success(aiModel.getId());
    }

    @PostMapping(value = "/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAiModel(@RequestBody AiModelUpdateRequest aiModelUpdateRequest) {
        Boolean b = aiModelApplicationService.updateAiModel(aiModelUpdateRequest);
        return ResultUtils.success(b);
    }
    @GetMapping(value = "/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<AiModelVO> getAiModelById(Long modelId) {
        AiModelVO aiModelById = aiModelApplicationService.getAiModelById(modelId);
        return ResultUtils.success(aiModelById);
    }

}
