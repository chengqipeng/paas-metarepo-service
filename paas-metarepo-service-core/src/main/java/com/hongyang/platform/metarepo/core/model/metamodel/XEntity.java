package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义对象元模型
 */
@Data
public class XEntity implements Serializable {
    private String apiKey;
    private String label;
    private String labelKey;
    private String namespace;
    private Integer entityType;
    private String svgApiKey;
    private String svgColor;
    private String description;
    private String descriptionKey;
    private Integer customEntitySeq;
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
