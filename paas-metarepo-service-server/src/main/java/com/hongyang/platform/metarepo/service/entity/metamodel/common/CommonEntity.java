package com.hongyang.platform.metarepo.service.entity.metamodel.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import java.io.Serializable;

/**
 * Common 级自定义对象（p_custom_entity_common，无 tenant_id）
 */
@Data
@TableName("p_common_entity")
public class CommonEntity extends BaseEntity {

    private String apiKey;
    private String label;
    private String labelKey;
    private String nameSpace;
    private Integer entityType;
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
