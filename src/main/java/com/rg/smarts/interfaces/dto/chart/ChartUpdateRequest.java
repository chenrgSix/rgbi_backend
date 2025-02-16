package com.rg.smarts.interfaces.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class ChartUpdateRequest implements Serializable {
    /**
     * 图表名称
     */
    private String name;
    /**
     * id
     */
    private Long id;
    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 生成的图表数据
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;

    /**
     * 创建用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}