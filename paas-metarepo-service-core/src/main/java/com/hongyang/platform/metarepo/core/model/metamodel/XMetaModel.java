package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 元模型聚合响应（对象+字段+关系+选项值+校验规则）
 */
@Data
public class XMetaModel implements Serializable {
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
    private String descriptionKey;
    private List<XItem> items;
    private List<XLink> links;
    private List<XCheckRule> checkRules;
    private Map<Long, List<XPickOption>> pickOptionsMap;
}
