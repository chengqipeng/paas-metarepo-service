package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;

/**
 * Common 级自定义对象（p_custom_entity_common，无 tenant_id）
 */
@Data
@TableName("p_custom_entity_common")
public class CustomEntityCommon implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String nameSpace;
    private Long entityId;
    private String name;
    private String nameKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer objectType;
    private Long svgId;
    private String svgColor;
    private String description;
    private String descriptionKey;
    private Integer customEntityseq;
    @TableLogic(value = "0", delval = "1")
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
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
}
