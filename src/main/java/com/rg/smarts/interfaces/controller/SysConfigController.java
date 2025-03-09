package com.rg.smarts.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.sysconfig.SysConfigApplicationService;
import com.rg.smarts.domain.user.constant.UserConstant;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.interfaces.dto.sysconfig.SysConfigQueryRequest;
import com.rg.smarts.interfaces.dto.sysconfig.SysConfigUpdateRequest;
import com.rg.smarts.interfaces.vo.SysConfigVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图表接口
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class SysConfigController {

    @Resource
    private SysConfigApplicationService sysConfigApplicationService;
    @PostMapping("/search")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Page<SysConfigVO> listSysConfigByPage(@RequestBody SysConfigQueryRequest searchReq) {
        return sysConfigApplicationService.listSysConfigByPage(searchReq);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public void updateConfig(@RequestBody SysConfigUpdateRequest sysConfigUpdateRequest) {
        sysConfigApplicationService.updateConfig(sysConfigUpdateRequest);
    }


}
