package com.rg.smarts.interfaces.dto.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChartRetryRequest implements Serializable {

    private static final long serialVersionUID = -4015423666971233788L;
    /**
     * 图标的 ID
     */
    private Long id;

}