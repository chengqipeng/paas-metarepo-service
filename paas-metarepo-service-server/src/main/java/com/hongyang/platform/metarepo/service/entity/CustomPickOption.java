package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import com.hongyang.framework.dao.split.CommonTenantSplit;
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
@CommonTenantSplit(commonTable = "p_common_pickoption")
public class CustomPickOption extends BaseEntity {

    private Long entityId;
    private Long itemId;
    private String apiKey;
    private Integer optionCode;
    private String label;
    private String labelKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
}
