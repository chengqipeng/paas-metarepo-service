package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义对象元模型
 * 字段与 p_custom_entity 表完全对齐
 */
@Data
public class XEntity implements Serializable {
    private Long id;
    private Long tenantId;
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
    private Integer deleteFlg;
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
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
