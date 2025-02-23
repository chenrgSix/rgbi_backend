package com.rg.smarts.domain.llm.provider;

public interface AIProvider {
    String genChart(String message, Long userId);
}
