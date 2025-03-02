package com.rg.smarts.infrastructure.manager;

import com.rg.smarts.domain.llm.provider.AIProvider;
import com.rg.smarts.infrastructure.aiservice.AiChatTemplate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AiManager implements AIProvider {

    @Resource
    private AiChatTemplate aiChatTemplate;

    /**
     * 向 AI 发送请求
     * @return
     */
    @Override
    public String genChart(final String content, Long userId) {
        String chat = aiChatTemplate.genChart(content);
        // 同步调用
        log.info("智普 AI 返回的结果 {}", chat);
        return chat;
    }

//    /**
//     * 对话，暂时只支持一个工具
//     * @param content
//     * @param sessionId
//     * @return
//     * @throws NoSuchMethodException
//     * @throws InvocationTargetException
//     * @throws IllegalAccessException
//     */
//    public String chat(String content, Long sessionId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(BaseTools.class);
//        List<ChatMessage> chatMessages = new ArrayList<>();
//        //构造请求参数
//        UserMessage userMessage = UserMessage.from(content);
//        chatMessages.add(userMessage);
//        Response<AiMessage> aiMessageResponse = zhiPuAiChatModel.generate(chatMessages, toolSpecifications);
//        log.info("zhiPuAiChatModel返回的结果 {}", aiMessageResponse);
//        AiMessage aiMessage = aiMessageResponse.content();
//        System.out.println(aiMessage);
//        List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();
//        if (!toolExecutionRequests.isEmpty()) {
//            for (ToolExecutionRequest toolExecutionRequest : toolExecutionRequests) {
//                String methodName = toolExecutionRequest.name();
//                String arguments = toolExecutionRequest.arguments();
//                System.out.println("methodName:" + methodName + ",arguments:" + arguments);
//            }
//        }
//        chatMessages.add(aiMessage);
//
//        toolExecutionRequests.forEach(toolExecutionRequest -> {
//            DefaultToolExecutor defaultToolExecutor = new DefaultToolExecutor(baseTools, toolExecutionRequest);
//            String result = defaultToolExecutor.execute(toolExecutionRequest, UUID.randomUUID().toString());
//            System.out.println(result);
//            ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage
//                    .from(toolExecutionRequest, result);
//            chatMessages.add(toolExecutionResultMessage);
//        });
//        Response<AiMessage> generate = zhiPuAiChatModel.generate(chatMessages);
//        return generate.content().text();
//    }

    /**
     * 对话，暂时只支持一个工具
     * @param content
     * @param sessionId
     */

    public String chatAiService(String content,String cosplay,String individua, Long sessionId){
        String chat = aiChatTemplate.chat(sessionId, content, cosplay, individua);
        return chat;
    }
}