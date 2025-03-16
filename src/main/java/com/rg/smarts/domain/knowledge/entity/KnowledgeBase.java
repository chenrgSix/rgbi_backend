package com.rg.smarts.domain.knowledge.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.rg.smarts.domain.knowledge.valueobject.KBStatusEnum;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import lombok.Data;

/**
 * 知识库
 * @TableName knowledge_base
 */
@TableName(value ="knowledge_base")
@Data
public class KnowledgeBase implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户id-标识谁创建的
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否公开，0：否；1：是
     */
    private Integer isPublic;

    /**
     * 文档数量
     */
    private Integer docNum;

    /**
     * 最大分割大小
     */
    private Integer ingestMaxSegment;

    /**
     * 最大重叠大小
     */
    private Integer ingestMaxOverlap;

    /**
     * 模型名称
     */
    private String ingestModelName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    public boolean isVisible(Long loginUserId) {
        if (this.isPublic.equals(KBStatusEnum.PUBLIC.getValue())) {
            return true;
        }
        return this.userId.equals(loginUserId);
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}