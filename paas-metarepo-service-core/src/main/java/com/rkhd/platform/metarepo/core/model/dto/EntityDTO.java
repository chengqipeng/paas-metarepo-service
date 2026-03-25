package com.rkhd.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义对象响应 DTO
 * 字段与 p_custom_entity 表完全对齐
 */
@Data
public class EntityDTO implements Serializable {
    /** 主键ID */
    private Long id;
    /** 租户ID */
    private Long tenantId;
    /** 命名空间 */
    private String nameSpace;
    /** 关联的元模型数据ID */
    private Long objectId;
    /** 内部名称 */
    private String name;
    /** API标识，创建后不可修改 */
    private String apiKey;
    /** 显示标签 */
    private String label;
    /** 多语言标签Key */
    private String labelKey;
    /** 对象类型（由 MetaOption 约束） */
    private Integer objectType;
    /** SVG图标ID */
    private Long svgId;
    /** SVG图标颜色 */
    private String svgColor;
    /** 描述 */
    private String description;
    /** 自定义对象序号 */
    private Integer customEntityseq;
    /** 删除标记：0=正常 1=已删除 */
    private Integer deleteFlg;
    /** 启用标记：0=停用 1=启用 */
    private Integer enableFlg;
    /** 自定义标记：0=标准 1=自定义 */
    private Integer customFlg;
    /** 业务分类（由 MetaOption 约束） */
    private Integer businessCategory;
    /** 类型扩展属性（JSON） */
    private String typeProperty;
    /** 对应物理数据库表名 */
    private String dbTable;
    /** 是否明细对象：0=否 1=是 */
    private Integer detailFlg;
    /** 是否启用团队 */
    private Integer enableTeam;
    /** 是否启用社交 */
    private Integer enableSocial;
    /** 功能开关位图 */
    private Long enableConfig;
    /** 隐藏标记：0=可见 1=隐藏 */
    private Integer hiddenFlg;
    /** 是否可搜索 */
    private Integer searchable;
    /** 是否启用共享 */
    private Integer enableSharing;
    /** 是否启用脚本触发器 */
    private Integer enableScriptTrigger;
    /** 是否启用动态 */
    private Integer enableActivity;
    /** 是否启用历史日志 */
    private Integer enableHistoryLog;
    /** 是否启用报表 */
    private Integer enableReport;
    /** 是否启用引用 */
    private Integer enableRefer;
    /** 是否启用API */
    private Integer enableApi;
    /** 是否启用流程 */
    private Long enableFlow;
    /** 是否可打包 */
    private Long enablePackage;
    /** 扩展属性 */
    private String extendProperty;
    /** 创建时间（毫秒时间戳） */
    private Long createdAt;
    /** 创建人ID */
    private Long createdBy;
    /** 最后修改时间（毫秒时间戳） */
    private Long updatedAt;
    /** 最后修改人ID */
    private Long updatedBy;
}
