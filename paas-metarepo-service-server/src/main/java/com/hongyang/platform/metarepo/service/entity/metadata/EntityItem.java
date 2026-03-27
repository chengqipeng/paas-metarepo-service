package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.platform.metarepo.service.common.annotation.CommonTenantSplit;
import com.hongyang.platform.metarepo.service.common.constants.MetamodelApiKeyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段定义（Common/Tenant 共用）
 * Tenant 表：p_tenant_item（有 tenant_id）
 * Common 数据：统一存储在 p_common_metadata（metamodel_api_key = "item"）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_item")
@CommonTenantSplit(metamodelApiKey = MetamodelApiKeyEnum.K_ITEM)
public class EntityItem extends BaseMetaTenantEntity {

    private String entityApiKey;
    private Integer itemType;
    private Integer dataType;
    private String typeProperty;
    private String helpText;
    private String helpTextKey;
    private String description;
    private String descriptionKey;
    private Integer customItemSeq;
    private String defaultValue;
    private Integer requireFlg;
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
    private String referEntityApiKey;
    private String referLinkApiKey;
    private String dbColumn;
    private Integer itemOrder;
    private Integer sortFlg;
    private String columnName;
}
