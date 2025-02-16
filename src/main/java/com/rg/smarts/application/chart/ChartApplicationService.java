package com.rg.smarts.application.chart;

import com.rg.smarts.interfaces.vo.BiResponse;
import com.rg.smarts.interfaces.dto.chart.ChartRetry;
import com.rg.smarts.interfaces.dto.chart.GenChartByAiRequest;
import com.rg.smarts.domain.chart.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
* @author czr
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-03-07 15:31:39
*/
public interface ChartApplicationService extends IService<Chart> {

    BiResponse retryGenChart(ChartRetry chartRetryController);

    @Transactional(rollbackFor = Exception.class)
    BiResponse getChartMQ(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest);
}
