package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段选项值
 * Tenant 表：p_custom_pickoption（有 tenant_id）
 * Common 表：p_common_pickoption（无 tenant_id）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_pickoption")
public class TenantPickOption extends BaseEntity {

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
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
}
