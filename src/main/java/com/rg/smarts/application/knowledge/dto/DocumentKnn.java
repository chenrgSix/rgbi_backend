package com.rg.smarts.application.knowledge.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Map;

@Document(indexName = "default")
@Data
public class DocumentKnn {

    @Field(type = FieldType.Text)
    private String text;
    private Float score;

}
