package com.rg.intelligent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.intelligent.exception.ThrowUtils;
import com.rg.intelligent.common.ErrorCode;
import com.rg.intelligent.model.entity.Score;
import com.rg.intelligent.service.ScoreService;
import com.rg.intelligent.mapper.ScoreMapper;
import org.springframework.stereotype.Service;

/**
* @author czr
* @description 针对表【score(积分表)】的数据库操作Service实现
* @createDate 2024-03-18 16:44:11
*/
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score>
    implements ScoreService{
    /**
     * 签到追加积分
     *
     * @param userId
     * @return
     */
    @Override
    public void checkIn(Long userId) {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        Score score = this.getOne(queryWrapper);
        ThrowUtils.throwIf(score == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(score.getIsSign() == 1, ErrorCode.PARAMS_ERROR, "领取失败，今日已领取");
        Long scoreTotal = score.getScoreTotal();
        ThrowUtils.throwIf(scoreTotal == 300, ErrorCode.PARAMS_ERROR, "领取失败，已达上线");
        UpdateWrapper<Score> updateWrapper = new UpdateWrapper();
        updateWrapper
                .set("scoreTotal", scoreTotal + 20)//此处暂时写死签到积分
                .set("isSign", 1)
                .eq("userId", userId);
        boolean r = this.update(updateWrapper);
        ThrowUtils.throwIf(!r, ErrorCode.OPERATION_ERROR, "更新签到数据失败");
    }

    /**
     * 消耗积分
     *
     * @param userId
     * @param points 消耗的积分数量，根据业务需求调整
     */
    public void deductPoints(Long userId, Long points) {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        Score score = this.getOne(queryWrapper);
        ThrowUtils.throwIf(score.getScoreTotal() < points, ErrorCode.OPERATION_ERROR, "积分不足，请联系管理员！");
        Long scoreTotal = score.getScoreTotal();
        UpdateWrapper<Score> updateWrapper = new UpdateWrapper();
        updateWrapper
                .set("scoreTotal", scoreTotal - points)
                .eq("userId", userId);
        boolean r = this.update(updateWrapper);
        ThrowUtils.throwIf(!r, ErrorCode.OPERATION_ERROR);
    }

    /**
     * 查看积分
     *
     * @param userId
     * @return
     */
    public Long getUserPoints(Long userId) {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        Score score = this.getOne(queryWrapper);
        return score.getScoreTotal();
    }
}




