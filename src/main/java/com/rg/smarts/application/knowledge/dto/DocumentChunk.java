package com.rg.smarts.application.knowledge.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "default")
@Data
public class DocumentChunk {

    @Id
    private String id;

    @Field(type = FieldType.Float)
    private List<Float> vector;

    @Field(type = FieldType.Text)
    private String text;

//    @Field(type = FieldType.Object)
//    private Map<String, Object> metadata;

}
