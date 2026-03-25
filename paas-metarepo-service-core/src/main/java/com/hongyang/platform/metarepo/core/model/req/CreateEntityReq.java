package com.hongyang.platform.metarepo.core.model.req;

import lombok.Data;
import java.io.Serializable;

@Data
public class CreateEntityReq implements Serializable {
    private Long tenantId;
    private String nameSpace;
    private String name;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer objectType;
    private Long svgId;
    private String svgColor;
    private String description;
    private Integer customFlg;
    private Integer businessCategory;
    private String typeProperty;
    private String dbTable;
    private Integer enableTeam;
    private Integer enableSharing;
    private Integer enableHistoryLog;
    private Integer enableReport;
    private Integer enableApi;
    private Long enablePackage;
    private Long operatorId;
}
