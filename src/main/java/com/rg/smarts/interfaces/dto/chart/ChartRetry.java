package com.rg.smarts.interfaces.dto.chart;

import com.rg.smarts.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChartRetry implements Serializable {


    private static final long serialVersionUID = 2645307609377346713L;
    /**
     * chartId
     */
    private Long chartId;

    /**
     * 登录的用户
     */
    private User loginUser;

    public Long getLoginUserId() {
        return loginUser.getId();
    }
}
