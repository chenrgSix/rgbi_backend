package com.rg.smarts.domain.knowledge.constant;

import java.util.Arrays;
import java.util.List;

public interface EmbeddingConstant {


    /**
     * 默认文档分割长度
     */
    int DOCUMENT_MAX_SEGMENT_SIZE_IN_TOKENS = 512;
    // 默认的文档返回最大条数
    int DOCUMENT_MAX_RESPONSE_SIZE = 5;
    float DOCUMENT_MIN_RESPONSE_SCORE = 0.6f;
    Double DOCUMENT_MIN_RESPONSE_SCORE_DOUBLE = 0.6;
    int DEFAULT_INGEST_OVERLAP = 0;
    String KB_ID = "kb_id";
    String DOC_ID = "doc_id";
    String KB_INDEX = "metadata.kb_id";

    List<String> DOC_TYPES =  Arrays.asList("doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf") ;
}
