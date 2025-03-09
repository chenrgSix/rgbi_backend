package com.rg.smarts.domain.sysconfig.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.sysconfig.entity.SysConfig;
import com.rg.smarts.interfaces.dto.sysconfig.SysConfigUpdateRequest;

/**
* @author 16152
* @description 针对表【sys_config(系统配置表)】的数据库操作Service
* @createDate 2025-03-09 15:16:52
*/
public interface SysConfigDomainService{

    void loadAndCache();

    void updateConfig(String configName,String configValue);

    Page<SysConfig> listSysConfigByPage(long current, long size, LambdaQueryWrapper<SysConfig> queryWrapper);
}
