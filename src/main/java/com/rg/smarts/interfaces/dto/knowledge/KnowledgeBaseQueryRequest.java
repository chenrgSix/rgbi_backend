package com.rg.smarts.interfaces.dto.knowledge;

import com.rg.smarts.infrastructure.common.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class KnowledgeBaseQueryRequest extends PageRequest implements Serializable {

    /**
     * 标题
     */
    private String title;


    /**
     * 模型名称
     */
    private String ingestModelName;



    private static final long serialVersionUID = 1L;
}