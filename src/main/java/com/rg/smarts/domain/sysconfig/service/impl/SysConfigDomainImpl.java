package com.rg.smarts.domain.sysconfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rg.smarts.domain.aimodel.utils.LocalCache;
import com.rg.smarts.domain.sysconfig.entity.SysConfig;
import com.rg.smarts.domain.sysconfig.repository.SysConfigRepository;
import com.rg.smarts.domain.sysconfig.service.SysConfigDomainService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author czr
 * @description 针对表【SysConfig(配置信息表)】的数据库操作Service实现
 */
@Service
@Slf4j
public class SysConfigDomainImpl  implements SysConfigDomainService {
    @Resource
    private SysConfigRepository sysConfigRepository;
    @Override
    public void loadAndCache() {
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        List<SysConfig> sysConfigList = sysConfigRepository.list(queryWrapper);
        if (LocalCache.CONFIGS.isEmpty()) {
            sysConfigList.forEach(item -> LocalCache.CONFIGS.put(item.getName(), item.getValue()));
        } else {
            //remove deleted config
            List<String> deletedKeys = new ArrayList<>();
            LocalCache.CONFIGS.forEach((k, v) -> {
                boolean deleted = sysConfigList.stream().noneMatch(sysConfig -> sysConfig.getName().equals(k));
                if (deleted) {
                    deletedKeys.add(k);
                }
            });
            if (!deletedKeys.isEmpty()) {
                deletedKeys.forEach(LocalCache.CONFIGS::remove);
            }
            //add or update config
            for (SysConfig item : sysConfigList) {
                String key = item.getName();
                LocalCache.CONFIGS.put(key, item.getValue());
            }
        }

    }

}




