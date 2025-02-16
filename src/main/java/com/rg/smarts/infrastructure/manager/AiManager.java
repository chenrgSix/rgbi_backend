package com.rg.smarts.infrastructure.manager;

import com.rg.smarts.application.score.ScoreApplicationService;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AiManager {
    //    @Resource
//    private YuCongMingClient yuCongMingClient;
//    private YuCongMingClient yuCongMingClient;
    @Resource
    private SparkClient sparkClient;
    @Resource
    private ScoreApplicationService scoreApplicationService;
    @Resource
    private ZhiPuAiChatModel zhiPuAiChatModel;
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
//        return sendMesToAIUseXingHuo(message,userId);

    }

    /**
     * 向 AI 发送请求
     *
     * @return
     */
    public String sendMesToAIUseZhiPu(final String content, Long userId) {
        //构造请求参数
        Prompt prompt = new Prompt(PRECONDITION + content, ZhiPuAiChatOptions
                .builder()
                .user(userId.toString())
                .build());
        // 构造请求
        ChatResponse call = this.zhiPuAiChatModel.call(prompt);
        AssistantMessage output = call.getResult().getOutput();
        String responseContent = output.getText();
        // 同步调用
        log.info("智普 AI 返回的结果 {}", responseContent);
        scoreApplicationService.deductPoints(userId, 20L);//调用成功后扣除积分
        return responseContent;
    }

    /**
     * 向 AI 发送请求
     *
     * @return
     */
    public String sendMesToAIUseXingHuo(final String content, Long userId) {
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(content));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传,取值为[1,4096],默认为2048
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
                // 指定请求版本，默认使用最新2.0版本
                .apiVersion(SparkApiVersion.V3_5)
                .build();
        // 同步调用
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        String responseContent = chatResponse.getContent();
        log.info("星火 AI 返回的结果 {}", responseContent);
        scoreApplicationService.deductPoints(userId, 20L);//调用成功后扣除积分
        return responseContent;
    }

}
