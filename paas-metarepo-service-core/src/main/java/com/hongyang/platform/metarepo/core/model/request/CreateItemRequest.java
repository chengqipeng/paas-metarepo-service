package com.hongyang.platform.metarepo.core.model.request;

import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建自定义字段请求
 * 继承 XItem 元模型，额外增加操作人字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateItemRequest extends XItem {
    /** 操作人ID */
    private Long operatorId;
}
