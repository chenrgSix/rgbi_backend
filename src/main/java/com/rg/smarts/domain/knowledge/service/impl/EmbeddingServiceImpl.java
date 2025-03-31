package com.rg.smarts.domain.knowledge.service.impl;

import com.rg.smarts.domain.knowledge.constant.EmbeddingConstant;
import com.rg.smarts.domain.knowledge.service.EmbeddingService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import dev.langchain4j.store.embedding.filter.Filter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.rg.smarts.domain.knowledge.constant.EmbeddingConstant.*;
import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

/**
 * @Author: czr
 * @CreateTime: 2025-03-13
 * @Description: 定义向量服务的接口实现类
 */
@Slf4j
//@AllArgsConstructor
@Service
public class EmbeddingServiceImpl implements EmbeddingService {
    // TODO 后续可自配置
    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    /**
     * 获取内容检索器
     * @param kbId 知识库id
     * @return 内容检索器
     */
    @Override
    public ContentRetriever getContentRetriever(Long kbId){
        Filter filter = metadataKey(EmbeddingConstant.KB_ID).isEqualTo(kbId);
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(DOCUMENT_MAX_RESPONSE_SIZE)
                .minScore(DOCUMENT_MIN_RESPONSE_SCORE_DOUBLE)
                .filter(filter)
                .build();
    }
    /**
     * <a href="https://docs.langchain4j.dev/tutorials/rag#embedding-store-ingestor">...</a>
     * 进行数据分块
     *
     * @param document 文档
     * @param overlap  token重叠部分 即比如一段话分成三个部分，每相邻两部分之间可以共享的最大内容量（交集）
     */
    @Override
    public void ingest(Document document, int overlap) {
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(DOCUMENT_MAX_SEGMENT_SIZE_IN_TOKENS, overlap, new OpenAiTokenizer(OpenAiChatModelName.GPT_3_5_TURBO));
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter) //对文档的处理
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        IngestionResult ingest = embeddingStoreIngestor.ingest(document);
        log.info(ingest.toString());
    }
    @Override
    public Embedding getVectorBySearch(String search) {
        Embedding questionAsVector = embeddingModel.embed(search).content();
        return questionAsVector;
    }

}
