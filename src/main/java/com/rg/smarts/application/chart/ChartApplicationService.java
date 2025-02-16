package com.rg.smarts.application.chart;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.interfaces.dto.chart.*;
import com.rg.smarts.interfaces.vo.BiResponse;
import com.rg.smarts.domain.chart.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
* @author czr
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-03-07 15:31:39
*/
public interface ChartApplicationService {

    BiResponse retryGenChart(ChartRetry chartRetryController);
    BiResponse genChartBuAi(MultipartFile multipartFile,
                            GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);

    BiResponse genChartBuAiAsync(MultipartFile multipartFile,
                                 GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);

    BiResponse genChartBuAiAsyncMq(MultipartFile multipartFile,
                                 GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);
    BiResponse retryGenChart(final ChartRetryRequest chartQueryRequest, HttpServletRequest request);

    @Transactional(rollbackFor = Exception.class)
    BiResponse getChartMQ(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest);

    Boolean editChart(ChartEditRequest chartEditRequest, HttpServletRequest request);

    Page<Chart> listMyChartByPage(ChartQueryRequest chartQueryRequest,
                                  HttpServletRequest request);

    Page<Chart> listChartByPageVo(ChartQueryRequest chartQueryRequest,
                                HttpServletRequest request);

    Page<Chart> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest);

    Boolean updateChart(ChartUpdateRequest chartUpdateRequest);

    Chart getChartById(long id, HttpServletRequest request);

    Boolean deleteChart(DeleteRequest deleteRequest, HttpServletRequest request);

    Long addChart(ChartAddRequest chartAddRequest, HttpServletRequest request);

    long count(QueryWrapper<Chart> queryWrapper);
    Boolean updateById( Chart updateChartResult );
    Chart getById(String message);

}
