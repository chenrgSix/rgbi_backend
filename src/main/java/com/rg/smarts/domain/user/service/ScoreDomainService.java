package com.rg.smarts.domain.user.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rg.smarts.domain.user.entity.Score;

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
    Score getUserPoints(Long userId);

    Boolean save(Score score);

    Boolean update(UpdateWrapper<Score> updateWrapper);
}
