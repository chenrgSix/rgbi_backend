package com.rg.intelligent.service;

import com.rg.intelligent.model.vo.BiResponse;
import com.rg.intelligent.model.dto.chart.ChartRetry;
import com.rg.intelligent.model.dto.chart.GenChartByAiRequest;
import com.rg.intelligent.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
* @author czr
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-03-07 15:31:39
*/
public interface ChartService extends IService<Chart> {

    BiResponse retryGenChart(ChartRetry chartRetryController);

    @Transactional(rollbackFor = Exception.class)
    BiResponse getChartMQ(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest);
}
