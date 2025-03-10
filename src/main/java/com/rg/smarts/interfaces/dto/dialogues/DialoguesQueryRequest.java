package com.rg.smarts.interfaces.dto.dialogues;

import com.baomidou.mybatisplus.annotation.*;
import com.rg.smarts.infrastructure.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 对话表
 * @TableName dialogues
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class DialoguesQueryRequest extends PageRequest implements Serializable  {


    private static final long serialVersionUID = 1L;
}