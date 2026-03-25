package com.hongyang.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 元模型聚合响应（对象+字段+关系+选项值+校验规则）
 */
@Data
public class MetaModelDTO implements Serializable {
    private Long entityId;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer objectType;
    private String dbTable;
    private Integer customFlg;
    private Integer enableFlg;
    private Integer enableSharing;
    private String typeProperty;
    private String description;
    /** 字段列表 */
    private List<ItemDTO> items;
    /** 关联关系列表 */
    private List<LinkDTO> links;
    /** 校验规则列表 */
    private List<CheckRuleDTO> checkRules;
    /** 选项值映射：itemId -> List */
    private Map<Long, List<PickOptionDTO>> pickOptionsMap;
}
