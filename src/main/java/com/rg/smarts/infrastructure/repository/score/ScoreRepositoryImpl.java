package com.rg.smarts.infrastructure.repository.score;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.score.entity.Score;
import com.rg.smarts.domain.score.repository.ScoreRepository;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.domain.user.repository.UserRepository;
import com.rg.smarts.infrastructure.mapper.ScoreMapper;
import com.rg.smarts.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class ScoreRepositoryImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreRepository {
}
