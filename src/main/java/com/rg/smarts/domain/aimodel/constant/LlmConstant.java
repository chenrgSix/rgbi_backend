package com.rg.smarts.domain.aimodel.constant;

import java.util.List;

public interface LlmConstant {


    public static final String DEFAULT_MODEL = "GLM-4-Flash";

    public static final List<String> DALLE2_CREATE_IMAGE_SIZES = List.of("256x256", "512x512", "1024x1024");

    public static final List<String> DALLE3_CREATE_IMAGE_SIZES = List.of("1024x1024", "1024x1792", "1792x1024");


    public static class SysConfigKey {
        private SysConfigKey() {
        }
        public static final String DEEPSEEK_SETTING = "deepseek_setting";
        public static final String OPENAI_SETTING = "openai_setting";
        public static final String ZHHIPU_SETTING = "zhipu_setting";
        public static final String OLLAMA_SETTING = "ollama_setting";
    }

    public static final String[] POI_DOC_TYPES = {"doc", "docx", "ppt", "pptx", "xls", "xlsx"};

    public static class ModelPlatform {
        private ModelPlatform() {
        }
        public static final String DEEPSEEK = "deepseek";
        public static final String OPENAI = "openai";
        public static final String ZHIPU = "zhipu";
        public static final String OLLAMA = "ollama";
    }

    public static class ModelType {
        private ModelType() {
        }
        public static final String TEXT = "text";
        public static final String IMAGE = "image";
        public static final String EMBEDDING = "embedding";
        public static final String RERANK = "rerank";
    }
    /**
     * 默认的最大输入token数
     */
    public static final int LLM_MAX_INPUT_TOKENS_DEFAULT = 4096;

    public static final String LLM_INPUT_TYPE_TEXT = "text";
    public static final String LLM_INPUT_TYPE_IMAGE = "image";
    public static final String LLM_INPUT_TYPE_AUDIO = "audio";
    public static final String LLM_INPUT_TYPE_VIDEO = "video";
}
