package com.rg.smarts.interfaces.dto.sysconfig;

import lombok.Data;

@Data
public class SysConfigUpdateRequest {
    private String name;
    private String value;
}