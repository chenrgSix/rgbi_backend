package com.rg.smarts.infrastructure.repository.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.user.entity.Score;
import com.rg.smarts.domain.user.repository.ScoreRepository;
import com.rg.smarts.infrastructure.mapper.ScoreMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ScoreRepositoryImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreRepository {
}
