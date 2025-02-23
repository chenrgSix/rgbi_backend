package com.rg.smarts.infrastructure.manager;

import dev.langchain4j.community.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AiManager {

    @Resource
    private ZhipuAiChatModel zhiPuAiChatModel;
    /**
     * AI 生成问题的预设条件
     */
    public static final String PRECONDITION = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
            "\"\n" +
            "Analysis goal: {数据分析的需求或者目标}\n" +
            "Raw data: \n" +
            "{csv格式的原始数据，用,作为分隔符}\n" +
            "\"\n" +
            "请根据这两部分内容，按照以下指定格式用英文或中文生成内容，此外不要输出任何多余的开头、结尾、注释：\n" +
            "\"\n" +
            "￥￥￥￥￥\n" +
            "{前端Echarts V5的option配置对象的JSON代码，不能生成开头的 \" options = \"，而是生成options花括号里面的内容代码，不要生成options的\"title\"= \"chart title\"。以及不要生成任何多余的注释，解释，介绍，开头，结尾。生成的Echarts能够合理地将数据进行可视化}\n" +
            "￥￥￥￥￥\n" +
            "{明确的数据分析结论、越详细越好，不要生成多余的注释}\n" +
            "\"";

    /**
     * AI 对话
     *
     * @param modelId
     * @param message
     * @return
     */
    public String doChat(long modelId, String message, Long userId) {
        return sendMesToAIUseZhiPu(message, userId);
    }

    /**
     * 向 AI 发送请求
     *
     * @return
     */
    public String sendMesToAIUseZhiPu(final String content, Long userId) {
        //构造请求参数
        List<ChatMessage> list = List.of(UserMessage.from(PRECONDITION), UserMessage.from(content));
        Response<AiMessage> generate = zhiPuAiChatModel.generate(list);
        String chat = generate.content().text();
        // 同步调用
        log.info("智普 AI 返回的结果 {}", chat);
        return chat;
    }


}
