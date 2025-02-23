package com.rg.smarts.application.llm.impl;

import com.rg.smarts.application.llm.LlmApplicationService;
import com.rg.smarts.domain.llm.service.LlmDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LlmApplicationServiceImpl implements LlmApplicationService {
    @Resource
    private LlmDomainService llmDomainService;
    @Override
    public String genChart(String message, Long userId) {
        return llmDomainService.genChart(message, userId);
    }
}
