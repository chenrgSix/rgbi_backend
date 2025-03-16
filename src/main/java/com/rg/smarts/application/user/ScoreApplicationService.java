package com.rg.smarts.application.user;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rg.smarts.domain.user.entity.Score;
import com.rg.smarts.interfaces.vo.ScoreVO;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service
* @createDate 2024-03-18 16:44:12
*/
public interface ScoreApplicationService{
    /**
     * 签到
     *
     * @param request
     * @return
     */
    void checkIn(HttpServletRequest request);

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
     * @param request
     * @return
     */
    ScoreVO getUserPoints(HttpServletRequest request);

    ScoreVO getUserPoints(Long userId);

    /**
     * 添加
     *
     * @param score
     * @return
     */
    Boolean save(Score score);

    /**
     * 签到
     *
     * @param updateWrapper
     * @return
     */
    Boolean update(UpdateWrapper<Score> updateWrapper);
}
