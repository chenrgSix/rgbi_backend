package com.rg.smarts.domain.aimodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssistantChatParams {
    // 会话的id
    private Long memoryId;
    // 本次的消息内容
    private String context;
}
