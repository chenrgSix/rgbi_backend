package com.rg.smarts.application;

import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.application.sysconfig.SysConfigApplicationService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


@Component
public class InitializationService {
    @Resource
    private AiModelApplicationService aiModelApplicationService;
    @Resource
    private SysConfigApplicationService sysConfigApplicationService;
    @PostConstruct
    public void init() {
        sysConfigApplicationService.loadAndCache();
        aiModelApplicationService.init();
    }
}
