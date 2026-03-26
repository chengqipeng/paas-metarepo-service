package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * Common 级字段选项值（p_common_pickoption，无 tenant_id）
 */
@Data
@TableName("p_common_pickoption")
public class CommonPickOption implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long entityId;
    private Long itemId;
    private String apiKey;
    private Integer optionCode;
    @TableField("option_label")
    private String label;
    @TableField("option_label_key")
    private String labelKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer customFlg;
    @TableLogic(value = "0", delval = "1")
    private Integer deleteFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
}
