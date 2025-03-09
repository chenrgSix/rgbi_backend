package com.rg.smarts.domain.aimodel.service.adi;


import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.helper.LLMContext;
import com.rg.smarts.domain.aimodel.service.AbstractLLMService;
import com.rg.smarts.infrastructure.llm.DeepSeekLLMService;
import com.rg.smarts.infrastructure.llm.OllamaLLMService;
import com.rg.smarts.infrastructure.llm.OpenAiLLMService;
import com.rg.smarts.infrastructure.llm.ZhiPuLLMService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.rg.smarts.domain.aimodel.utils.LocalCache.MODEL_ID_TO_OBJ;


@Slf4j
@Service
public class AiModelSettingService {

    @Value("${adi.proxy.enable:false}")
    protected boolean proxyEnable;

    @Value("${adi.proxy.host:0}")
    protected String proxyHost;

    @Value("${adi.proxy.http-port:0}")
    protected int proxyHttpPort;

    private Proxy proxy;

    private List<AiModel> all = new ArrayList<>();

    /**
     * 模型及其配置初始化
     *
     * @param allModels
     */
    public void init(List<AiModel> allModels) {
        this.all = allModels;
        for (AiModel model : all) {
            if (Boolean.TRUE.equals(model.getEnable())) {
                MODEL_ID_TO_OBJ.put(model.getId(), model);
            } else {
                MODEL_ID_TO_OBJ.remove(model.getId());
            }
        }
        if (proxyEnable) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyHttpPort));
        } else {
            proxy = null;
        }

        initLLMServiceList();

    }

    /**
     * 初始化大语言模型列表
     */
    private synchronized void initLLMServiceList() {

        //deepseek
        initLLMService(LlmConstant.ModelPlatform.DEEPSEEK, DeepSeekLLMService::new);

        //openai
        initLLMService(LlmConstant.ModelPlatform.OPENAI, model -> new OpenAiLLMService(model).setProxy(proxy));
        
        //ollama
        initLLMService(LlmConstant.ModelPlatform.OLLAMA, OllamaLLMService::new);

        //zhipu
        initLLMService(LlmConstant.ModelPlatform.ZHIPU, ZhiPuLLMService::new);
    }

 

    private void initLLMService(String platform, Function<AiModel, AbstractLLMService> function) {
        List<AiModel> models = all.stream().filter(item -> item.getType().equals(LlmConstant.ModelType.TEXT) && item.getPlatform().equals(platform)).toList();
        if (CollectionUtils.isEmpty(models)) {
            log.warn("{} service is disabled", platform);
        }
        LLMContext.clearByPlatform(platform);
        for (AiModel model : models) {
            log.info("add llm model,model:{}", model);
            LLMContext.addLLMService(function.apply(model));
        }
    }



    public void delete(AiModel aiModel) {
        MODEL_ID_TO_OBJ.remove(aiModel.getId());
    }

    public void addOrUpdate(AiModel aiModel) {
        AiModel existOne = all.stream().filter(item -> item.getId().equals(aiModel.getId())).findFirst().orElse(null);
        if (null == existOne) {
            all.add(aiModel);
        } else {
            BeanUtils.copyProperties(aiModel, existOne);
        }
        if (LlmConstant.ModelType.TEXT.equalsIgnoreCase(aiModel.getType())) {
            initLLMServiceList();
        }
        MODEL_ID_TO_OBJ.put(aiModel.getId(), aiModel);
    }
}
