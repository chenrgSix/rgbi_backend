package com.rg.smarts.application.sysconfig.impl;

import com.rg.smarts.application.sysconfig.SysConfigApplicationService;
import com.rg.smarts.domain.sysconfig.service.SysConfigDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


@Service
public class  SysConfigApplicationServiceImpl implements SysConfigApplicationService {
    @Resource
    private SysConfigDomainService sysConfigDomainService;
    @Override
    public void loadAndCache(){
        sysConfigDomainService.loadAndCache();
    }
}




