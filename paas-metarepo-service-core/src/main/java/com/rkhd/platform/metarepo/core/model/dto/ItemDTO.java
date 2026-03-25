package com.rkhd.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义字段响应 DTO
 * 字段与 p_custom_item 表完全对齐
 */
@Data
public class ItemDTO implements Serializable {
    /** 主键ID */
    private Long id;
    /** 租户ID */
    private Long tenantId;
    /** 所属对象ID */
    private Long entityId;
    /** 内部名称 */
    private String name;
    /** API标识，创建后不可修改 */
    private String apiKey;
    /** 显示标签 */
    private String label;
    /** 多语言标签Key */
    private String labelKey;
    /** 字段UI类型（由 MetaOption 约束） */
    private Integer itemType;
    /** 数据类型（由 MetaOption 约束） */
    private Integer dataType;
    /** 类型扩展属性（JSON） */
    private String typeProperty;
    /** 帮助文本 */
    private String helpText;
    /** 帮助文本多语言Key */
    private String helpTextKey;
    /** 描述 */
    private String description;
    /** 自定义字段序号 */
    private Integer customItemseq;
    /** 默认值 */
    private String defaultValue;
    /** 是否必填：0=否 1=是 */
    private Integer requireFlg;
    /** 删除标记：0=正常 1=已删除 */
    private Integer deleteFlg;
    /** 自定义标记：0=标准 1=自定义 */
    private Integer customFlg;
    /** 启用标记：0=停用 1=启用 */
    private Integer enableFlg;
    /** 创建时是否可填写 */
    private Integer creatable;
    /** 是否可更新 */
    private Integer updatable;
    /** 是否唯一键 */
    private Integer uniqueKeyFlg;
    /** 是否启用历史日志 */
    private Integer enableHistoryLog;
    /** 功能开关位图 */
    private Long enableConfig;
    /** 是否可打包 */
    private Long enablePackage;
    /** 只读状态 */
    private Integer readonlyStatus;
    /** 可见状态 */
    private Integer visibleStatus;
    /** 隐藏标记：0=可见 1=隐藏 */
    private Integer hiddenFlg;
    /** 关联对象ID（LOOKUP类型） */
    private Long referEntityId;
    /** 关联关系ID */
    private Long referLinkId;
    /** 对应物理数据库列名 */
    private String dbColumn;
    /** 字段排序序号 */
    private Integer itemOrder;
    /** 是否支持排序 */
    private Integer sortFlg;
    /** 列名 */
    private String columnName;
    /** 创建时间（毫秒时间戳） */
    private Long createdAt;
    /** 创建人ID */
    private Long createdBy;
    /** 最后修改时间（毫秒时间戳） */
    private Long updatedAt;
    /** 最后修改人ID */
    private Long updatedBy;
}
