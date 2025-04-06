package com.rg.smarts.application.aimodel.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.application.aimodel.DialoguesApplicationService;
import com.rg.smarts.application.knowledge.dto.DocumentKnn;
import com.rg.smarts.application.knowledge.service.KnowledgeBaseApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.helper.LLMContext;
import com.rg.smarts.domain.aimodel.model.AssistantChatParams;
import com.rg.smarts.domain.aimodel.model.SseAskParams;
import com.rg.smarts.domain.aimodel.service.AiModelDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.constant.CommonConstant;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.utils.SqlUtils;
import com.rg.smarts.interfaces.dto.ai.AiModelAddRequest;
import com.rg.smarts.interfaces.dto.ai.AiModelQueryRequest;
import com.rg.smarts.interfaces.dto.ai.AiModelUpdateRequest;
import com.rg.smarts.interfaces.dto.ai.ChatRequest;
import com.rg.smarts.interfaces.vo.ai.AiModelVO;
import com.rg.smarts.interfaces.vo.ai.LLMModelVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Comparator;
import java.util.List;

/**
 * @author czr
 * @description 针对表【AiModel(AiModel表)】的数据库操作Service实现
 */
@Service
@Slf4j
public class AiModelApplicationServiceImpl implements AiModelApplicationService {
    @Resource
    private AiModelDomainService aiModelDomainService;

    @Resource
    private UserApplicationService userApplicationService;
    @Resource
    private DialoguesApplicationService dialoguesApplicationService;
    @Resource
    private KnowledgeBaseApplicationService knowledgeBaseApplicationService;
    @Override
    public String genChart(String message, Long userId) {
        return aiModelDomainService.genChart(message, userId);
    }

    @Override
    public AiModel addOne(AiModelAddRequest aiModelAddRequest,HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        AiModel aiModel = new AiModel();
        BeanUtils.copyProperties(aiModelAddRequest, aiModel);
        aiModel.setUserId(loginUser.getId());
        aiModelDomainService.addOne(aiModel);
        return aiModel;
    }

    @Override
    public Boolean updateAiModel(AiModelUpdateRequest aiModelUpdateRequest) {
        AiModel aiModel = new AiModel();
        BeanUtils.copyProperties(aiModelUpdateRequest,aiModel);
        return aiModelDomainService.updateAiModel(aiModel);
    }
    @Override
    public AiModelVO getAiModelById(Long aiModelId) {
        AiModel aiModel = aiModelDomainService.getAiModelByIdOrThrow(aiModelId);
        AiModelVO aiModelVO = new AiModelVO();
        BeanUtils.copyProperties(aiModel, aiModelVO);
        return aiModelVO;
    }
    @Override
    public List<LLMModelVo> getSupportLLMModel() {
        return LLMContext.getAllServices().values()
                .stream()
                .map(item -> {
                    AiModel aiModel = item.getAiModel();
                    LLMModelVo modelInfo = new LLMModelVo();
                    modelInfo.setModelId(aiModel.getId());
                    modelInfo.setModelName(aiModel.getName());
                    modelInfo.setModelPlatform(aiModel.getPlatform());
                    modelInfo.setIsEnable(aiModel.getIsEnable());
                    modelInfo.setIsFree(aiModel.getIsFree());
                    return modelInfo;
                })
                .sorted(Comparator.comparingInt(item -> (item.getIsFree() == 1 && item.getIsEnable() == 1) ? 0 : 1))
                .toList();
        }
    @Transactional
    @Override
    public void chatStream(ChatRequest chatRequest, SseEmitter sseEmitter, HttpServletRequest request) {
        SseAskParams sseAskParams = getSseAskParams(chatRequest, sseEmitter, request);
        List<Long> kbIds = chatRequest.getKbIds();
        if (kbIds==null || kbIds.isEmpty()) {
            aiModelDomainService.commonChat(sseAskParams);
            return;
        }
        Long userId = userApplicationService.getLoginUser(request).getId();
        List<DocumentKnn> documentKnns = knowledgeBaseApplicationService.searchDocumentChunk(userId, kbIds, chatRequest.getContent());
//        List<DocumentKnn> documentKnns = knowledgeBaseApplicationService.searchDocumentChunk(userId, kbId, chatRequest.getContent());
        sseAskParams.getAssistantChatParams().setSearchResult(documentKnns.toString());
        ragChatStream(sseAskParams);
    }

    private void ragChatStream(SseAskParams sseAskParams) {
        aiModelDomainService.ragChat(sseAskParams);
    }
    protected SseAskParams getSseAskParams(ChatRequest chatRequest, SseEmitter sseEmitter, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        SseAskParams sseAskParams = new SseAskParams();
        sseAskParams.setUserId(loginUser.getId());
        sseAskParams.setModelName(LlmConstant.DEFAULT_MODEL);
        if (StrUtil.isNotBlank(chatRequest.getModelName())){
            AiModel aiModel = LLMContext.getAllServices().get(chatRequest.getModelName()).getAiModel();
            if (!aiModel.getEnable()){
                throw new BusinessException(ErrorCode.B_LLM_SERVICE_DISABLED);
            }
            sseAskParams.setModelName(chatRequest.getModelName());
        }
        sseAskParams.setSseEmitter(sseEmitter);
        Long validMemoryId = dialoguesApplicationService.getMemoryIdOrAdd(
                chatRequest.getContent(),
                loginUser.getId(),
                chatRequest.getMemoryId(),
                chatRequest.getKbIds());
        AssistantChatParams assistantChatParams = new AssistantChatParams();
        assistantChatParams.setMemoryId(validMemoryId);
        assistantChatParams.setContext(chatRequest.getContent());
        sseAskParams.setAssistantChatParams(assistantChatParams);
        return sseAskParams;
    }
    @Override
    public void init() {
        aiModelDomainService.init();
    }

    @Override
    public Page<AiModel> listAiModelByPage(AiModelQueryRequest aiModelQueryRequest) {
        int current = aiModelQueryRequest.getCurrent();
        int size = aiModelQueryRequest.getPageSize();
        return aiModelDomainService.listAiModelByPage(new Page<>(current, size),getAiModelQueryWrapper(aiModelQueryRequest));
    }

    private QueryWrapper<AiModel> getAiModelQueryWrapper(AiModelQueryRequest aiModelQueryRequest) {

        Long id = aiModelQueryRequest.getId();
        Long userId = aiModelQueryRequest.getUserId();
        String name = aiModelQueryRequest.getName();
        String type = aiModelQueryRequest.getType();
        String platform = aiModelQueryRequest.getPlatform();
        Integer isFree = aiModelQueryRequest.getIsFree();
        Integer isEnable = aiModelQueryRequest.getIsEnable();
        String sortField = aiModelQueryRequest.getSortField();
        String sortOrder = aiModelQueryRequest.getSortOrder();
        QueryWrapper<AiModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(id),"id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"userId", userId);
        queryWrapper.like(StringUtils.isNotBlank(name),"name", name);
        queryWrapper.eq(StringUtils.isNotBlank(type),"type", type);
        queryWrapper.eq(StringUtils.isNotBlank(platform),"platform", platform);
        queryWrapper.eq(ObjectUtils.isNotEmpty(isFree),"isFree", isFree);
        queryWrapper.eq(ObjectUtils.isNotEmpty(isEnable),"isEnable", isEnable);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}




