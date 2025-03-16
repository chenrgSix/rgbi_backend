package com.rg.smarts.application.user.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rg.smarts.application.user.ScoreApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.user.entity.Score;
import com.rg.smarts.domain.user.service.ScoreDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.interfaces.vo.ScoreVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service实现
* @createDate 2024-03-18 16:44:11
*/
@Service
public class ScoreApplicationServiceImpl implements ScoreApplicationService {
    @Resource
    private ScoreDomainService scoreDomainService;
    @Resource
    @Lazy
    private UserApplicationService userApplicationService;
    /**
     * 签到追加积分
     *
     * @param request
     * @return
     */
    @Override
    public void checkIn(HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        scoreDomainService.checkIn(loginUser.getId());
    }

    /**
     * 消耗积分
     *
     * @param userId
     * @param points 消耗的积分数量，根据业务需求调整
     */
    public void deductPoints(Long userId, Long points) {
        scoreDomainService.deductPoints(userId, points);
    }

    /**
     * 查看积分
     * @return
     */
    @Override
    public ScoreVO getUserPoints(Long userId) {
        Score score = scoreDomainService.getUserPoints(userId);
        ScoreVO scoreVO = new ScoreVO();
        BeanUtils.copyProperties(score, scoreVO);
        return scoreVO;
    }
    @Override
    public ScoreVO getUserPoints(HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        return this.getUserPoints(loginUser.getId());
    }
    @Override
    public Boolean save(Score score) {
        return scoreDomainService.save(score);
    }

    @Override
    public Boolean update(UpdateWrapper<Score> updateWrapper) {
        return scoreDomainService.update(updateWrapper);
    }
}




