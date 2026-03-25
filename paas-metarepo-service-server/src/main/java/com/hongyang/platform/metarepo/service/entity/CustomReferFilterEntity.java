package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关联过滤条件（p_custom_refer_filter）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_refer_filter")
public class CustomReferFilterEntity extends BaseEntity {

    private Long entityId;
    private Long itemId;
    private Long linkId;
    private String filterField;
    private String filterOperator;
    private String filterValue;
    private Integer filterOrder;
    private String description;
}
