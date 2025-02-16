package com.rg.intelligent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.intelligent.constant.BiMqConstant;
import com.rg.intelligent.constant.ChartConstant;
import com.rg.intelligent.exception.ThrowUtils;
import com.rg.intelligent.model.vo.BiResponse;
import com.rg.intelligent.mq.BiMessageProducer;
import com.rg.intelligent.common.ErrorCode;
import com.rg.intelligent.manager.AiManager;
import com.rg.intelligent.model.dto.chart.ChartRetry;
import com.rg.intelligent.model.dto.chart.GenChartByAiRequest;
import com.rg.intelligent.model.entity.Chart;
import com.rg.intelligent.model.entity.User;
import com.rg.intelligent.service.ChartService;
import com.rg.intelligent.mapper.ChartMapper;
import com.rg.intelligent.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;

/**
* @author czr
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-03-07 15:31:39
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{
    @Resource
    private BiMessageProducer biMessageProducer;
    @Override
    public BiResponse retryGenChart(ChartRetry chartRetryController) {
        ThrowUtils.throwIf(chartRetryController == null, ErrorCode.PARAMS_ERROR);
        Long chartId = chartRetryController.getChartId();
        this.updateById(new Chart(ChartConstant.CHART_STATUS_WAIT, "", "", "", chartId));
        biMessageProducer.sendMessage(BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY, String.valueOf(chartId));
        return new BiResponse(chartId);
    }
    /**
     * 向 RabbitMQ 发送消息
     *
     * @param multipartFile
     * @param genChartByAiRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BiResponse getChartMQ(final MultipartFile multipartFile, final GenChartByAiRequest genChartByAiRequest) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        User loginUser = genChartByAiRequest.getLoginUser();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
        // 构造用户输入\获取压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus("wait");
        chart.setUserId(loginUser.getId());
        boolean saveResult = this.save(chart);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        long newChartId = chart.getId();
        biMessageProducer.sendMessage(BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY, String.valueOf(newChartId));
        return new BiResponse(newChartId);
    }

    private String formatExcelData(final MultipartFile multipartFile,final GenChartByAiRequest genChartByAiRequest){
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append(AiManager.PRECONDITION);
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，需要生成图表的类型是" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String data = ExcelUtils.excelToCsv(multipartFile);
        userInput.append(data).append("\n");
        return String.valueOf(userInput);
    }
}




