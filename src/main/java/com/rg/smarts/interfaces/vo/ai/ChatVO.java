package com.rg.smarts.interfaces.vo.ai;

import lombok.Data;

/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description: 对话信息的返回
 */
@Data
public class ChatVO {
    /**
     * id
     */
    private Long id;

    /**
     * 聊天返回的内容
     */
    private String chatResponse;


}
