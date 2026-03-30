package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局选项集（p_tenant_global_pickitem）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_global_pickitem")
public class GlobalPickItem extends BaseMetaTenantEntity {

    private Integer customFlg;
    private String description;
    private String descriptionKey;
}
