package com.rg.smarts.interfaces.dto.chart;

import com.rg.smarts.domain.user.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求

 */
@Data
public class GenChartByAiRequest implements Serializable {
    /**
     * 图表名称
     */
    private String name;
    /**
     * 目标
     */
    private String goal;
    /**
     * 类型
     */
    private String chartType;

    /**
     * 登录的用户
     */
    private User loginUser;
    private static final long serialVersionUID = 1L;
}