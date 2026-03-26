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
 * Common 级校验规则（p_common_check_rule，无 tenant_id）
 */
@Data
@TableName("p_common_check_rule")
public class CommonCheckRule implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long objectId;
    private String name;
    private String nameKey;
    private String apiKey;
    @TableField("rule_label")
    private String label;
    @TableField("rule_label_key")
    private String labelKey;
    private Integer activeFlg;
    private String description;
    private String descriptionKey;
    private String checkFormula;
    private String checkErrorMsg;
    private String checkErrorMsgKey;
    private Integer checkErrorLocation;
    private Long checkErrorItemId;
    private Integer checkAllItemsFlg;
    private Integer checkErrorWay;
    @TableLogic(value = "0", delval = "1")
    private Integer deleteFlg;
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
}
