package com.rg.smarts.application.embedding.service.impl;

import com.rg.smarts.application.embedding.service.EmbeddingApplicationService;
import com.rg.smarts.domain.embedding.service.EmbeddingDomainService;
import dev.langchain4j.data.document.Document;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EmbeddingApplicationServiceImpl implements EmbeddingApplicationService {
    @Resource
    private EmbeddingDomainService embeddingDomainService;
    @Override
    public void loadDocument(String filePath,String fileSuffix) {
        try {
            //解析文档
            Document document = embeddingDomainService.loadDocument(fileSuffix,filePath);
            System.out.println(document);
        } catch (Exception e) {
            log.error("文档解析失败", e);
        }
    }

}
