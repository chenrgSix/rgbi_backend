package com.rg.smarts.interfaces.dto.ai;

import lombok.Data;

/**
 * @Author: czr
 * @CreateTime: 2025-03-04
 * @Description: 前端发起请求对话
 */
@Data
public class ChatRequest {

    private Long memoryId;
    private String content;
}
