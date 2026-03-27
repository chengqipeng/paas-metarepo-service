package com.hongyang.platform.metarepo.service.entity.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段选项值（Common/Tenant 共用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
