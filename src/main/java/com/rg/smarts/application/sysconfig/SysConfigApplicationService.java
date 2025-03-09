package com.rg.smarts.application.sysconfig;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.interfaces.dto.sysconfig.SysConfigQueryRequest;
import com.rg.smarts.interfaces.dto.sysconfig.SysConfigUpdateRequest;
import com.rg.smarts.interfaces.vo.SysConfigVO;

public interface SysConfigApplicationService {

    void loadAndCache();

    void updateConfig(SysConfigUpdateRequest sysConfigUpdateRequest);

    Page<SysConfigVO> listSysConfigByPage(SysConfigQueryRequest searchReq);
}
