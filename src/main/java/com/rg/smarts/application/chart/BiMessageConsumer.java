package com.rg.smarts.application.chart;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;

import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.domain.chart.constant.BiMqConstant;
import com.rg.smarts.domain.chart.constant.ChartConstant;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.domain.chart.entity.Chart;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.IOException;

@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartApplicationService chartApplicationService;
    @Resource
    private AiModelApplicationService aiModelApplicationService;

    // 指定程序监听的消息队列和确认机制
    //@SneakyThrows
    //@RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    //public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
    //    log.info("receiveMessage message = {}", message);
    //    if (StringUtils.isBlank(message)) {
    //        // 如果失败，消息拒绝
    //        channel.basicNack(deliveryTag, false, false);
    //        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
    //    }
    //    long chartId = Long.parseLong(message);
    //    Chart chart = chartService.getById(chartId);
    //    if (chart == null) {
    //        channel.basicNack(deliveryTag, false, false);
    //        throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
    //    }
    //    // 先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
    //    Chart updateChart = new Chart();
    //    updateChart.setId(chart.getId());
    //    updateChart.setStatus("running");
    //    boolean b = chartService.updateById(updateChart);
    //    if (!b) {
    //        channel.basicNack(deliveryTag, false, false);
    //        handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
    //        return;
    //    }
    //    // 消息确认
    //    channel.basicAck(deliveryTag, false);
    //}


    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            throwExceptionAndNackMessage(channel, deliveryTag);
        }
        log.info("receiveMessage message = {}", message);
        Chart chart = chartApplicationService.getById(message);
        if (chart == null) {
            throwExceptionAndNackMessage(channel, deliveryTag);
        }
        Long userId = chart.getUserId();
        // 检查用户任务计数器
        int userTaskCount = (int) getRunningTaskCount(userId);
        try {
            if (userTaskCount <= BiMqConstant.MAX_CONCURRENT_CHARTS) {
                chartApplicationService.updateById(new Chart(Long.parseLong(message), ChartConstant.CHART_STATUS_RUNNING, ""));
                // 调用 AI
                String result = aiModelApplicationService.genChart(buildUserInput(chart),userId);
                log.info("AI信息生成={}",result);
                String[] splits = result.split("￥￥￥￥￥");
                if (splits.length < 2) {
                    channel.basicNack(deliveryTag, false, false);
                    handleChartUpdateError(chart.getId(), "AI 生成错误");
                    return;
                }
                String genChart = splits[1].trim().replaceAll("\\s+","");
                String genResult = splits[2].trim();
                Chart updateChartResult = new Chart();
                updateChartResult.setId(chart.getId());
                updateChartResult.setGenChart(genChart);
                updateChartResult.setGenResult(genResult);
                updateChartResult.setStatus(ChartConstant.CHART_STATUS_SUCCEED);
                boolean updateResult = chartApplicationService.updateById(updateChartResult);
                if (!updateResult) {
                    throwExceptionAndNackMessage(channel, deliveryTag);
                }
                channel.basicAck(deliveryTag, false);
                return;
            }
            channel.basicNack(deliveryTag, false, true);
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }



    /**
     * 消费异常消息
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @RabbitListener(queues = {BiMqConstant.BI_DLX_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveErrorMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            throwExceptionAndNackMessage(channel, deliveryTag);
        }
        log.info("receiveErrorMessage message = {}", message);
        Chart chart = chartApplicationService.getById(message);
        if (chart == null) {
            throwExceptionAndNackMessage(channel, deliveryTag);
        }
        chartApplicationService.updateById(new Chart(Long.parseLong(message), ChartConstant.CHART_STATUS_FAILED, ""));
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    /**
     * 构建用户输入
     * @param chart
     * @return
     */
    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        //userInput.append(AiManager.PRECONDITION).append("\n");
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage");
        boolean updateResult = chartApplicationService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }

    /**
     * 抛异常同时拒绝消息
     * @param channel
     * @param deliveryTag
     */
    private void throwExceptionAndNackMessage(Channel channel, long deliveryTag) {
        try {
            channel.basicNack(deliveryTag, false, false);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 获取当前用户正在运行的任务数量，就算服务器出现问题，数据已经持久化到硬盘之中
     *
     * @param userId
     * @return
     */
    private long getRunningTaskCount(Long userId) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId).eq("status", ChartConstant.CHART_STATUS_RUNNING);
        return chartApplicationService.count(queryWrapper);
    }
}
