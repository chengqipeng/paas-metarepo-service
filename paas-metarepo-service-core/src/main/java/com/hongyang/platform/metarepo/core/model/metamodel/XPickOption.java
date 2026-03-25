package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 字段选项值元模型
 */
@Data
public class XPickOption implements Serializable {
    private Long id;
    private Long tenantId;
    private Long entityId;
    private Long itemId;
    private String apiKey;
    private Integer optionCode;
    private String optionLabel;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer enableFlg;
}
