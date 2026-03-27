package com.hongyang.platform.metarepo.service.entity.metamodel;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Common 级通用元数据大宽表（p_common_metadata）。
 * 所有 Common 级业务元数据统一存储在此表中，通过 metamodelApiKey 区分类型。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_common_metadata")
public class CommonMetadata extends BaseMetaCommonEntity {

    private String metamodelApiKey;
    private String parentEntityApiKey;
    private Integer customFlg;
    private Integer metadataOrder;
    private String ownerApiKey;
    private String description;

    private String dbcVarchar1;
    private String dbcVarchar2;
    private String dbcVarchar3;
    private String dbcVarchar4;
    private String dbcVarchar5;
    private String dbcVarchar6;
    private String dbcVarchar7;
    private String dbcVarchar8;
    private String dbcVarchar9;
    private String dbcVarchar10;
    private String dbcVarchar11;
    private String dbcVarchar12;
    private String dbcVarchar13;
    private String dbcVarchar14;
    private String dbcVarchar15;
    private String dbcVarchar16;
    private String dbcVarchar17;
    private String dbcVarchar18;
    private String dbcVarchar19;
    private String dbcVarchar20;

    private String dbcTextarea1;
    private String dbcTextarea2;
    private String dbcTextarea3;
    private String dbcTextarea4;
    private String dbcTextarea5;
    private String dbcTextarea6;
    private String dbcTextarea7;
    private String dbcTextarea8;
    private String dbcTextarea9;
    private String dbcTextarea10;

    private Integer dbcSelect1;
    private Integer dbcSelect2;
    private Integer dbcSelect3;
    private Integer dbcSelect4;
    private Integer dbcSelect5;
    private Integer dbcSelect6;
    private Integer dbcSelect7;
    private Integer dbcSelect8;
    private Integer dbcSelect9;
    private Integer dbcSelect10;

    private Long dbcInteger1;
    private Long dbcInteger2;
    private Long dbcInteger3;
    private Long dbcInteger4;
    private Long dbcInteger5;
    private Long dbcInteger6;
    private Long dbcInteger7;
    private Long dbcInteger8;
    private Long dbcInteger9;
    private Long dbcInteger10;

    private Double dbcReal1;
    private Double dbcReal2;
    private Double dbcReal3;
    private Double dbcReal4;
    private Double dbcReal5;
    private Double dbcReal6;
    private Double dbcReal7;
    private Double dbcReal8;
    private Double dbcReal9;
    private Double dbcReal10;

    private Long dbcDate1;
    private Long dbcDate2;
    private Long dbcDate3;
    private Long dbcDate4;
    private Long dbcDate5;
    private Long dbcDate6;
    private Long dbcDate7;
    private Long dbcDate8;
    private Long dbcDate9;
    private Long dbcDate10;

    private Long dbcRelation1;
    private Long dbcRelation2;
    private Long dbcRelation3;
    private Long dbcRelation4;
    private Long dbcRelation5;
    private Long dbcRelation6;
    private Long dbcRelation7;
    private Long dbcRelation8;
    private Long dbcRelation9;
    private Long dbcRelation10;

    private Integer dbcTinyint1;
    private Integer dbcTinyint2;
    private Integer dbcTinyint3;
    private Integer dbcTinyint4;
    private Integer dbcTinyint5;
    private Integer dbcTinyint6;
    private Integer dbcTinyint7;
    private Integer dbcTinyint8;
    private Integer dbcTinyint9;
    private Integer dbcTinyint10;
}
