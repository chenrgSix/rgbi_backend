package com.rg.smarts.interfaces.controller;


import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.application.score.ScoreApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.interfaces.vo.ScoreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 积分接口
 *
 * @author xing

 */
@RestController
@RequestMapping("/score")
@Slf4j
public class ScoreController {
    @Resource
    private UserApplicationService userApplicationService;
    @Resource
    private ScoreApplicationService scoreApplicationService;

    /**
     * 用于签到时添加积分
     * @param request
     * @return
     */
    @PostMapping("/checkIn")
    public BaseResponse<String> checkIn(HttpServletRequest request) {
        scoreApplicationService.checkIn(request);
        return ResultUtils.success("签到成功");
    }

    /**
     * 查询积分信息
     *
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ScoreVO> getUserById(HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        ScoreVO totalPoints = scoreApplicationService.getUserPoints(loginUser.getId());
        return ResultUtils.success(totalPoints);
    }
}
