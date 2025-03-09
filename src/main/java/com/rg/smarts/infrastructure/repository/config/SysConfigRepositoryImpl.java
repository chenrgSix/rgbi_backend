package com.rg.smarts.infrastructure.repository.config;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.sysconfig.entity.SysConfig;
import com.rg.smarts.domain.sysconfig.repository.SysConfigRepository;
import com.rg.smarts.infrastructure.mapper.SysConfigMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SysConfigRepositoryImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigRepository {
}
