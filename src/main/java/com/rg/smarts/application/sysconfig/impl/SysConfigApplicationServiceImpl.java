package com.rg.smarts.application.sysconfig.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.sysconfig.SysConfigApplicationService;
import com.rg.smarts.domain.sysconfig.entity.SysConfig;
import com.rg.smarts.domain.sysconfig.service.SysConfigDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.interfaces.dto.sysconfig.SysConfigQueryRequest;
import com.rg.smarts.interfaces.dto.sysconfig.SysConfigUpdateRequest;
import com.rg.smarts.interfaces.vo.SysConfigVO;
import com.rg.smarts.interfaces.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class  SysConfigApplicationServiceImpl implements SysConfigApplicationService {
    @Resource
    private SysConfigDomainService sysConfigDomainService;
    @Override
    public void loadAndCache(){
        sysConfigDomainService.loadAndCache();
    }

    @Override
    public void updateConfig(SysConfigUpdateRequest req) {
        sysConfigDomainService.updateConfig(req.getName(),req.getValue());
    }

    @Override
    public Page<SysConfigVO> listSysConfigByPage(SysConfigQueryRequest searchReq) {
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        long current = searchReq.getCurrent();
        long size = searchReq.getPageSize();
        Page<SysConfig> sysConfigPage = sysConfigDomainService.listSysConfigByPage(current,size,queryWrapper);
        Page<SysConfigVO> sysConfigVOPage = new Page<>(current, size, sysConfigPage.getTotal());
        List<SysConfigVO> collect = sysConfigPage.getRecords().stream().map(sysConfig -> {
            SysConfigVO sysConfigVO = new SysConfigVO();
            BeanUtil.copyProperties(sysConfig, sysConfigVO);
            return sysConfigVO;
        }).toList();
        sysConfigVOPage.setRecords(collect);
        return sysConfigVOPage;
    }
}




