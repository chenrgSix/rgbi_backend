package com.rg.smarts.interfaces.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: czr
 * @CreateTime: 2025-04-01
 * @Description: 知识库的极简输出
 */
@Data
public class KBSimpleVO implements Serializable {
    /**
     * id
     */
    private Long id;

    private String title;
}
