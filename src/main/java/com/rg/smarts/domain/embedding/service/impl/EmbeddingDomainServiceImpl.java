package com.rg.smarts.domain.embedding.service.impl;

import com.rg.smarts.domain.embedding.constant.EmbeddingConstant;
import com.rg.smarts.domain.embedding.service.EmbeddingDomainService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.rg.smarts.domain.embedding.constant.EmbeddingConstant.DOCUMENT_MAX_SEGMENT_SIZE_IN_TOKENS;

/**
 * @Author: czr
 * @CreateTime: 2025-03-13
 * @Description: 定义向量服务的接口实现类
 */
@Slf4j
//@AllArgsConstructor
@Service
public class EmbeddingDomainServiceImpl implements EmbeddingDomainService {
    // TODO 后续可自配置
    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    /**
     * <a href="https://docs.langchain4j.dev/tutorials/rag#embedding-store-ingestor">...</a>
     * 进行数据分块
     * @param document 文档
     * @param overlap  token重叠部分
     */
    private void ingest(Document document, int overlap) {
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(DOCUMENT_MAX_SEGMENT_SIZE_IN_TOKENS, overlap, new OpenAiTokenizer(OpenAiChatModelName.GPT_3_5_TURBO));
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        embeddingStoreIngestor.ingest(document);
    }

    @Override
    public Document loadDocument(String type, String path) {
        Document result = null;
        if (type.equalsIgnoreCase("txt")) {
            result = UrlDocumentLoader.load(path, new TextDocumentParser());
        } else if (type.equalsIgnoreCase("pdf")) {
            result = UrlDocumentLoader.load(path, new ApachePdfBoxDocumentParser());
        } else if (EmbeddingConstant.DOC_TYPES.contains(type)) {
            result = UrlDocumentLoader.load(path, new ApachePoiDocumentParser());
        }
        // TODO overlap是知识库配置之一
        result.metadata().put("文档id","66666");
        result.metadata().put("kb_item_uuid","66666");
        this.ingest(result, EmbeddingConstant.DEFAULT_INGEST_OVERLAP);
        return result;
    }
}
