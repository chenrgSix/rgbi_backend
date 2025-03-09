package com.rg.smarts.interfaces.dto.sysconfig;

import com.rg.smarts.infrastructure.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 配置查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysConfigQueryRequest extends PageRequest implements Serializable {

}