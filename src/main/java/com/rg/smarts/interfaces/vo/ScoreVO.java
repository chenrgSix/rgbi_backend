package com.rg.smarts.interfaces.vo;

import lombok.Data;

/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description: 积分信息的返回
 */
@Data
public class ScoreVO {
    /**
     * 总积分
     */
    private Long scoreTotal;

    /**
     * 0表示未签到，1表示已签到
     */
    private Integer isSign;

}
