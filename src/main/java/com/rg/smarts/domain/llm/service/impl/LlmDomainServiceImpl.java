package com.rg.smarts.domain.llm.service.impl;

import com.rg.smarts.domain.llm.provider.AIProvider;
import com.rg.smarts.domain.llm.service.LlmDomainService;
import com.rg.smarts.infrastructure.manager.AiManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class LlmDomainServiceImpl implements LlmDomainService {
    @Resource
    private AIProvider aiProvider;
    /**
     * AI 生成图表
     * @param message
     * @return
     */
    @Override
    public String genChart(String message, Long userId) {
        return aiProvider.genChart(message, userId);
    }
}
