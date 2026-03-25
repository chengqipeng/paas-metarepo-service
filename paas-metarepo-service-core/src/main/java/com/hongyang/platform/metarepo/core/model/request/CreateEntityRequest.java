package com.hongyang.platform.metarepo.core.model.request;

import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建自定义对象请求
 * 继承 XEntity 元模型，额外增加操作人字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateEntityRequest extends XEntity {
    /** 操作人ID */
    private Long operatorId;
}
