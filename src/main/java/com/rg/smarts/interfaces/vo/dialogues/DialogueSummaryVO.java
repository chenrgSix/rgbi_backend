package com.rg.smarts.interfaces.vo.dialogues;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 对话响应
 * @TableName dialogues
 */
@Data
public class DialogueSummaryVO implements Serializable {
    /**
     * memoryId
     */
    private Long id;
    /**
     * 消息主题
     */
    private List<Long> kbIds;


    /**
     * 消息主题
     */
    private String chatTitle;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}