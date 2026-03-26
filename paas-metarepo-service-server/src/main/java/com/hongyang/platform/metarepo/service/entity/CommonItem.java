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
 * Common 级字段定义（p_common_item，无 tenant_id）
 */
@Data
@TableName("p_common_item")
public class CommonItem implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long entityId;
    private String name;
    private String nameKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer itemType;
    private Integer dataType;
    private String typeProperty;
    private String helpText;
    private String helpTextKey;
    private String description;
    private String descriptionKey;
    private Integer customItemseq;
    private String defaultValue;
    private Integer requireFlg;
    @TableLogic(value = "0", delval = "1")
    private Integer deleteFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private Integer creatable;
    private Integer updatable;
    private Integer uniqueKeyFlg;
    private Integer enableHistoryLog;
    private Long enableConfig;
    private Long enablePackage;
    private Integer readonlyStatus;
    private Integer visibleStatus;
    private Integer hiddenFlg;
    private Long referEntityId;
    private Long referLinkId;
    private String dbColumn;
    private Integer itemOrder;
    private Integer sortFlg;
    private String columnName;
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
}
