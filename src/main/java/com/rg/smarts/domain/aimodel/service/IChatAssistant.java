package com.rg.smarts.domain.aimodel.service;

import dev.langchain4j.service.*;

/**
 * @Author: czr
 * @CreateTime: 2025-02-24
 * @Description: 定义AI模板
 */
public interface IChatAssistant {
    /**
     * AI 生成问题的预设条件
     */
    String GEN_CHART_TEMPLATE = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
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
//    @SystemMessage(fromResource = "my-prompt-template.txt")  //从资源文件中加载提示模板
    @SystemMessage(GEN_CHART_TEMPLATE)
    String genChart(String content);

    @SystemMessage("你的身份是{{cosplay}}，你的性格是{{individual}}，你有喜怒哀乐，能够根据消息给出正常人应有的情绪" +
            "，允许使用不礼貌的口头禅，不要带这种语气词['哼','啦']," +
            "现在请你带入{{cosplay}}的角色进行回复，努力扮演好{{cosplay}}，" +
            "控制回复字数在20个字以内，严禁在结尾带上标点符号，避免被发现AI的真实身份")
    String chat(@MemoryId Long memoryId, @UserMessage String content, @V("cosplay") String cosplay,@V("individual")String individual);

    String chat(@UserMessage String content,@MemoryId Long memoryId);

    TokenStream chatStream(@UserMessage String content,@MemoryId Long memoryId);
}