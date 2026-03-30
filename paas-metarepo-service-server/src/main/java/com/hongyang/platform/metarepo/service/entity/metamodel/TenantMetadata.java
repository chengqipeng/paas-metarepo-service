package com.hongyang.platform.metarepo.service.entity.metamodel;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Tenant 级通用元数据大宽表（p_tenant_metadata）。
 * <p>
 * 所有 Tenant 级业务元数据统一存储在此表中，通过 metamodelApiKey 区分类型。
 * dbc_xxx_N 列的语义由 p_meta_item.db_column 定义。
 * <p>
 * 列类型设计（按底层数据库类型合并）：
 * - dbc_varchar_1~30   VARCHAR(255)     短文本、api_key 引用
 * - dbc_textarea_1~10  TEXT             长文本（JSON、公式等）
 * - dbc_bigint_1~20    BIGINT           整数（合并原 integer + relation + date）
 * - dbc_int_1~15       INTEGER          枚举/选择值（原 select）
 * - dbc_smallint_1~15  SMALLINT         布尔/开关（原 tinyint）
 * - dbc_decimal_1~5    DECIMAL(20,4)    精确小数（原 real → decimal）
 * <p>
 * 总计 95 个扩展列。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_metadata")
public class TenantMetadata extends BaseMetaTenantEntity {

    /** 所属元模型 api_key（关联 p_meta_model.api_key） */
    private String metamodelApiKey;
    /** 元数据实例 ID（大宽表 p_meta_metamodel_data 中的记录 ID） */
    private String metadataApiKey;
    /** 所属对象 api_key（关联业务对象，子表如 item/checkRule 通过此字段关联父对象） */
    private String entityApiKey;
    /** 父元数据 ID（层级关系：选项值→字段、布局组件→布局） */
    private String parentMetadataApiKey;
    /** 自定义标记：0=标准（平台预置），1=自定义 */
    private Integer customFlg;
    /** 排序序号 */
    private Integer metadataOrder;
    /** 描述信息 */
    private String description;
    /** 元数据版本号（如 "1.0.0"） */
    private String metaVersion;

    // ==================== dbc_varchar_1~30 VARCHAR(500) ====================
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
    private String dbcVarchar21;
    private String dbcVarchar22;
    private String dbcVarchar23;
    private String dbcVarchar24;
    private String dbcVarchar25;
    private String dbcVarchar26;
    private String dbcVarchar27;
    private String dbcVarchar28;
    private String dbcVarchar29;
    private String dbcVarchar30;

    // ==================== dbc_textarea_1~10 TEXT ====================
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

    // ==================== dbc_bigint_1~20 BIGINT（合并 integer+relation+date） ====================
    private Long dbcBigint1;
    private Long dbcBigint2;
    private Long dbcBigint3;
    private Long dbcBigint4;
    private Long dbcBigint5;
    private Long dbcBigint6;
    private Long dbcBigint7;
    private Long dbcBigint8;
    private Long dbcBigint9;
    private Long dbcBigint10;
    private Long dbcBigint11;
    private Long dbcBigint12;
    private Long dbcBigint13;
    private Long dbcBigint14;
    private Long dbcBigint15;
    private Long dbcBigint16;
    private Long dbcBigint17;
    private Long dbcBigint18;
    private Long dbcBigint19;
    private Long dbcBigint20;

    // ==================== dbc_int_1~15 INTEGER（原 select） ====================
    private Integer dbcInt1;
    private Integer dbcInt2;
    private Integer dbcInt3;
    private Integer dbcInt4;
    private Integer dbcInt5;
    private Integer dbcInt6;
    private Integer dbcInt7;
    private Integer dbcInt8;
    private Integer dbcInt9;
    private Integer dbcInt10;
    private Integer dbcInt11;
    private Integer dbcInt12;
    private Integer dbcInt13;
    private Integer dbcInt14;
    private Integer dbcInt15;

    // ==================== dbc_smallint_1~50 SMALLINT（原 tinyint） ====================
    private Integer dbcSmallint1;
    private Integer dbcSmallint2;
    private Integer dbcSmallint3;
    private Integer dbcSmallint4;
    private Integer dbcSmallint5;
    private Integer dbcSmallint6;
    private Integer dbcSmallint7;
    private Integer dbcSmallint8;
    private Integer dbcSmallint9;
    private Integer dbcSmallint10;
    private Integer dbcSmallint11;
    private Integer dbcSmallint12;
    private Integer dbcSmallint13;
    private Integer dbcSmallint14;
    private Integer dbcSmallint15;
    private Integer dbcSmallint16;
    private Integer dbcSmallint17;
    private Integer dbcSmallint18;
    private Integer dbcSmallint19;
    private Integer dbcSmallint20;
    private Integer dbcSmallint21;
    private Integer dbcSmallint22;
    private Integer dbcSmallint23;
    private Integer dbcSmallint24;
    private Integer dbcSmallint25;
    private Integer dbcSmallint26;
    private Integer dbcSmallint27;
    private Integer dbcSmallint28;
    private Integer dbcSmallint29;
    private Integer dbcSmallint30;
    private Integer dbcSmallint31;
    private Integer dbcSmallint32;
    private Integer dbcSmallint33;
    private Integer dbcSmallint34;
    private Integer dbcSmallint35;
    private Integer dbcSmallint36;
    private Integer dbcSmallint37;
    private Integer dbcSmallint38;
    private Integer dbcSmallint39;
    private Integer dbcSmallint40;
    private Integer dbcSmallint41;
    private Integer dbcSmallint42;
    private Integer dbcSmallint43;
    private Integer dbcSmallint44;
    private Integer dbcSmallint45;
    private Integer dbcSmallint46;
    private Integer dbcSmallint47;
    private Integer dbcSmallint48;
    private Integer dbcSmallint49;
    private Integer dbcSmallint50;

    // ==================== dbc_decimal_1~5 DECIMAL(20,4)（原 real → decimal） ====================
    private BigDecimal dbcDecimal1;
    private BigDecimal dbcDecimal2;
    private BigDecimal dbcDecimal3;
    private BigDecimal dbcDecimal4;
    private BigDecimal dbcDecimal5;
}
