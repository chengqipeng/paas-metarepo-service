package com.hongyang.platform.metarepo.service.entity.metadata.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * Common 级对象关联关系（p_common_entity_link，无 tenant_id）
 */
@Data
@TableName("p_common_entity_link")
public class CommonEntityLink implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private String nameKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private String typeProperty;
    private Integer linkType;
    private Long parentEntityId;
    private Long childEntityId;
    private Integer detailLink;
    private Integer cascadeDelete;
    private Integer accessControl;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
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
