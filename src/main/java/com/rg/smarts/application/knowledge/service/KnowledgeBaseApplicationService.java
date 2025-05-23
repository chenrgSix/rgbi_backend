package com.rg.smarts.application.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.knowledge.dto.DocumentKnn;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeAddDocumentRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseAddRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseQueryRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeDocumentQueryRequest;
import com.rg.smarts.interfaces.vo.KBSimpleVO;
import com.rg.smarts.interfaces.vo.knowledge.DocumentInfoVO;
import com.rg.smarts.interfaces.vo.knowledge.KnowledgeBaseVO;
import com.rg.smarts.interfaces.vo.knowledge.KnowledgeDocumentVO;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: czr
 * @CreateTime: 2025-03-16
 * @Description: 知识库操作的domain层
 */
public interface KnowledgeBaseApplicationService {
    List<KnowledgeBaseVO> getSelectableKnowledgeBaseByUserId(HttpServletRequest request);

    Boolean addKnowledgeBase(KnowledgeBaseAddRequest knowledgeBaseAddRequest, HttpServletRequest request);

    ContentRetriever getContentRetriever(Long kbId,Long userId);

    Page<KnowledgeBaseVO> listKnowledgeBaseByPage(KnowledgeBaseQueryRequest knowledgeBaseQueryRequest, HttpServletRequest request);

    KnowledgeBaseVO getKnowledgeBaseById(Long id, HttpServletRequest request);

    Boolean addDocument(MultipartFile multipartFile, KnowledgeAddDocumentRequest knowledgeAddDocumentRequest, HttpServletRequest request);

    //  进行向量化
    Boolean loadDocument(Long docId,HttpServletRequest request);

    Page<KnowledgeDocumentVO> listDocByPage(KnowledgeDocumentQueryRequest knowledgeDocumentQueryRequest, HttpServletRequest request);

    DocumentInfoVO getDocumentInfo(KnowledgeDocumentQueryRequest knowledgeDocumentQueryRequest, HttpServletRequest request);

    Boolean deleteDocument(long docId, HttpServletRequest request);

    List<DocumentKnn> searchDocumentChunk(Long userId, List<Long> kbIds, String search);

    List<KBSimpleVO> getKbSimples(List<Long> kbIds);
}
