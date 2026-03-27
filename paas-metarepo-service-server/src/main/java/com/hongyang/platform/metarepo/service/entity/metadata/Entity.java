package com.hongyang.platform.metarepo.service.entity.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义对象定义（Common/Tenant 共用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
