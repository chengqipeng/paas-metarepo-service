package com.hongyang.platform.metarepo.core.model.request;

import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建关联关系请求
 * 继承 XLink 元模型，额外增加操作人字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateLinkRequest extends XLink {
    /** 操作人ID */
    private Long operatorId;
}
