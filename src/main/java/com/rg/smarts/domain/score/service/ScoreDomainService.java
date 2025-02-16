package com.rg.smarts.domain.score.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rg.smarts.domain.score.entity.Score;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service
* @createDate 2024-03-18 16:44:12
*/
public interface ScoreDomainService {
    /**
     * 签到
     *
     * @param userId
     * @return
     */
    void checkIn(Long userId);

    /**
     * 消耗积分
     *
     * @param userId
     * @param points 积分数
     * @return
     */
    void deductPoints(Long userId, Long points);

    /**
     * 获取积分
     *
     * @param userId
     * @return
     */
    Long getUserPoints(Long userId);

    Boolean save(Score score);

    Boolean update(UpdateWrapper<Score> updateWrapper);
}
