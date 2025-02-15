package com.rg.intelligent.controller;


import com.rg.intelligent.common.BaseResponse;
import com.rg.intelligent.common.ResultUtils;
import com.rg.intelligent.model.entity.User;
import com.rg.intelligent.service.ScoreService;
import com.rg.intelligent.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    private UserService userService;
    @Resource
    private ScoreService scoreService;

    /**
     * 用于签到时添加积分
     * @param request
     * @return
     */
    @PostMapping("/checkIn")
    public BaseResponse<String> checkIn(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        scoreService.checkIn(loginUser.getId());
        return ResultUtils.success("签到成功");
    }

    /**
     * 查询积分
     *
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Long> getUserById(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long totalPoints = scoreService.getUserPoints(loginUser.getId());
        return ResultUtils.success(totalPoints);
    }
}
