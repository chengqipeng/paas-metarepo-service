package com.rkhd.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义对象响应 DTO
 */
@Data
public class EntityDTO implements Serializable {
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
    private Integer enableFlg;
    private Integer customFlg;
    private Integer businessCategory;
    private String typeProperty;
    private String dbTable;
    private Integer detailFlg;
    private Integer enableTeam;
    private Integer enableSharing;
    private Integer enableHistoryLog;
    private Integer enableReport;
    private Integer enableApi;
    private Long enablePackage;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
