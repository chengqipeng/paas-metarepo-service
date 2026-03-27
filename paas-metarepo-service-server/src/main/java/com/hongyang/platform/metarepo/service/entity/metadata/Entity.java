package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.platform.metarepo.service.common.annotation.CommonTenantSplit;
import com.hongyang.platform.metarepo.service.common.constants.MetamodelApiKeyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义对象定义（Common/Tenant 共用）
 * Tenant 表：p_tenant_entity（有 tenant_id）
 * Common 数据：统一存储在 p_common_metadata（metamodel_api_key = "entity"）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_entity")
@CommonTenantSplit(metamodelApiKey = MetamodelApiKeyEnum.K_ENTITY)
public class Entity extends BaseMetaTenantEntity {

    private Integer entityType;
    private String svgApiKey;
    private String svgColor;
    private String description;
    private String descriptionKey;
    private Integer customEntitySeq;
    private Integer enableFlg;
    private Integer customFlg;
    private Integer businessCategory;
    private String typeProperty;
    private String dbTable;
    private Integer detailFlg;
    private Integer enableTeam;
    private Integer enableSocial;
    private Long enableConfig;
    private Integer hiddenFlg;
    private Integer searchable;
    private Integer enableSharing;
    private Integer enableScriptTrigger;
    private Integer enableActivity;
    private Integer enableHistoryLog;
    private Integer enableReport;
    private Integer enableRefer;
    private Integer enableApi;
    private Long enableFlow;
    private Long enablePackage;
    private String extendProperty;
    private Integer enableDynamicFeed;
    private Integer enableGroupMember;
    private Integer isArchived;
    private Integer enableScriptExecutor;
    private Integer enableDuplicaterule;
    private Integer enableCheckrule;
    private Integer enableBusitype;
}
