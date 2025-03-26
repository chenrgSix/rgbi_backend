package com.rg.smarts.application.knowledge.dto;

import com.rg.smarts.domain.knowledge.entity.KnowledgeDocument;
import com.rg.smarts.interfaces.vo.knowledge.DocumentInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentInfoDTO implements Serializable {

    public DocumentInfoDTO(KnowledgeDocument document) {
        this.id = document.getId();
        this.kbId = document.getKbId();
        this.fileId = document.getFileId();
        this.docType = document.getDocType();
        this.status = document.getStatus();
        this.createTime = document.getCreateTime();
        this.updateTime = document.getUpdateTime();
    }
    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 知识库ID
     */
    private Long kbId;


    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文档类型
     */
    private String docType;

    /**
     * 文档状态 -1：解析失败 0：未解析 1：解析中 2：解析完成
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 总页数
     */
    private int pages ;
    private int size ;
    private int current ;
    private long total ;

    private List<DocumentChunk> chunks;

    private static final long serialVersionUID = 1L;

    public DocumentInfoVO toVO(){
        DocumentInfoVO documentInfoVO = new DocumentInfoVO();
        documentInfoVO.setChunks(this.chunks);
        documentInfoVO.setId(this.id);
        documentInfoVO.setFileId(this.fileId);
        documentInfoVO.setDocType(this.docType);
        documentInfoVO.setCreateTime(this.createTime);
        documentInfoVO.setUpdateTime(this.updateTime);
        documentInfoVO.setPages(this.pages);
        documentInfoVO.setSize(this.size);
        documentInfoVO.setCurrent(this.current);
        documentInfoVO.setTotal(this.total);
        return documentInfoVO;
    }

}