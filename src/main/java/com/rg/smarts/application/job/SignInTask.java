package com.rg.smarts.application.job;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.domain.score.entity.Score;
import com.rg.smarts.application.score.ScoreApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
@Slf4j
public class SignInTask {
    @Resource
    private ScoreApplicationService scoreApplicationService;

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

        boolean updateResult = scoreApplicationService.update(updateWrapper);
        ThrowUtils.throwIf(!updateResult, ErrorCode.OPERATION_ERROR,"没有可更新的签到数据");
        log.info("Check-in status has been updated!");
    }
}
