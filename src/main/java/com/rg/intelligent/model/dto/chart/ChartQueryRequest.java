package com.rg.intelligent.model.dto.chart;

import com.rg.intelligent.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {
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
     * 创建用户id
     */
    private Long userId;
    /**
     * 图表类型
     */
    private String chartType;


    private static final long serialVersionUID = 1L;
}