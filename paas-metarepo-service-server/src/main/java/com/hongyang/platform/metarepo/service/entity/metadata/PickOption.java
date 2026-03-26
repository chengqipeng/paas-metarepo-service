package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.split.CommonTenantSplit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段选项值（Common/Tenant 共用）
 * Tenant 表：p_custom_pickoption（有 tenant_id）
 * Common 表：p_common_pick_option（无 tenant_id）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_pick_option")
@CommonTenantSplit(commonTable = "p_common_pick_option")
public class PickOption extends BaseMetaTenantEntity {

    private String entityApiKey;
    private String itemApiKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
}
