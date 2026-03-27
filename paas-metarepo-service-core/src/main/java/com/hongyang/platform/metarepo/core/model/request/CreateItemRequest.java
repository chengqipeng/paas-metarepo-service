package com.hongyang.platform.metarepo.core.model.request;

import com.hongyang.platform.metarepo.core.model.metamodel.XEntityItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建自定义字段请求
 * 继承 XEntityItem 元模型，额外增加操作人字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateItemRequest extends XEntityItem {
    /** 操作人ID */
    private Long operatorId;
}
