package com.rg.smarts.interfaces.vo.knowledge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rg.smarts.application.knowledge.dto.DocumentChunk;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 知识文档
 */

@Data
public class DocumentInfoVO implements Serializable{
    private List<DocumentChunk> chunks;

    /**
     * id
     */
    private Long id;


    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文档类型
     */
    private String docType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 总页数
     */
    private int pages ;
    private int size ;
    private int current ;
    private long total ;

    private static final long serialVersionUID = 1L;
}