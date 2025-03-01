package com.rg.smarts.infrastructure.repository.chart;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.chart.entity.Chart;
import com.rg.smarts.domain.chart.repository.ChartRepository;
import com.rg.smarts.infrastructure.mapper.ChartMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChartRepositoryImpl extends ServiceImpl<ChartMapper, Chart> implements ChartRepository {
}
