package com.rg.smarts.domain.chart.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.chart.entity.Chart;

/**
* @author czr
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-03-07 15:31:39
*/
public interface ChartDomainService  {

    void retryGenChart(Long chartId);

    void handleChartUpdateError(long chartId, String execMessage);

    Long getChartMQ(Chart chart);

    Boolean updateChart(Long id, Chart chart);

    Boolean updateById(Chart chart);
    Boolean save(Chart chart);
    Boolean removeById(long id);
    Chart getById(long id);
    Page<Chart> page(Page<Chart> page, QueryWrapper<Chart> queryWrapper);
    long count(QueryWrapper<Chart> queryWrapper);
}
