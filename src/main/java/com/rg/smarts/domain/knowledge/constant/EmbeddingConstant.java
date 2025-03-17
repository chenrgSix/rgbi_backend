package com.rg.smarts.domain.knowledge.constant;

import java.util.Arrays;
import java.util.List;

public interface EmbeddingConstant {


    /**
     * 默认文档分割长度
     */
    int DOCUMENT_MAX_SEGMENT_SIZE_IN_TOKENS = 1024;
    int DEFAULT_INGEST_OVERLAP = 0;
    String KB_ID = "kb_id";
    String DOC_ID = "doc_id";

    List<String> DOC_TYPES =  Arrays.asList("doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf") ;
}
