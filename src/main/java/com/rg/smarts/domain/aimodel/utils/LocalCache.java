package com.rg.smarts.domain.aimodel.utils;

import com.rg.smarts.domain.aimodel.entity.AiModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存模型的配置和模型对象
 */
public class LocalCache {
    public static final Map<String, String> CONFIGS = new ConcurrentHashMap<>();
    public static Map<Long, AiModel> MODEL_ID_TO_OBJ = new ConcurrentHashMap<>();
}
