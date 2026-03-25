package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义对象定义（p_custom_entity）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_entity")
public class CustomEntityEntity extends BaseEntity {

    private String nameSpace;
    private Long objectId;
    private String name;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer objectType;
    private Long svgId;
    private String svgColor;
    private String description;
    private Integer customEntityseq;
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
}
