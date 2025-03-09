package com.rg.smarts.infrastructure.utils;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: czr
 * @CreateTime: 2025-02-23
 * @Description: 为AI提供工具
 */
@Component
public class BaseTools {

    @Tool
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }

}
