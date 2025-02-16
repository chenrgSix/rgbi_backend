package com.rg.smarts.infrastructure.repository.score;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.chart.entity.Chart;
import com.rg.smarts.domain.chart.repository.ChartRepository;
import com.rg.smarts.domain.score.entity.Score;
import com.rg.smarts.domain.score.repository.ScoreRepository;
import com.rg.smarts.infrastructure.mapper.ChartMapper;
import com.rg.smarts.infrastructure.mapper.ScoreMapper;
import org.springframework.stereotype.Service;

@Service
public class ChartRepositoryImpl extends ServiceImpl<ChartMapper, Chart> implements ChartRepository {
}
