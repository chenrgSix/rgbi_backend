package com.rg.intelligent.job.cycle;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rg.intelligent.common.ErrorCode;
import com.rg.intelligent.exception.ThrowUtils;
import com.rg.intelligent.model.entity.Score;
import com.rg.intelligent.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
@Slf4j
public class SignInTask {
    @Resource
    private ScoreService scoreService;

    /**
     * 每天凌晨0点更新积分表的isSign为0
     */
    //@Scheduled(cron = "0/2 * * * * ? ")
    @Scheduled(cron = "0 0 0 * * *")
    public void sendMessageToClient() {
        UpdateWrapper<Score> updateWrapper = new UpdateWrapper<>();
        //更新Score表中isSign为1的数据
        updateWrapper.set("isSign",0)
                .eq("isSign",1);

        boolean updateResult = scoreService.update(updateWrapper);
        ThrowUtils.throwIf(!updateResult, ErrorCode.OPERATION_ERROR,"没有可更新的签到数据");
        log.info("Check-in status has been updated!");
    }
}
