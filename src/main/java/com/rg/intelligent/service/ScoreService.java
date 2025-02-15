package com.rg.intelligent.service;

import com.rg.intelligent.model.entity.Score;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service
* @createDate 2024-03-18 16:44:12
*/
public interface ScoreService extends IService<Score> {
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
}
