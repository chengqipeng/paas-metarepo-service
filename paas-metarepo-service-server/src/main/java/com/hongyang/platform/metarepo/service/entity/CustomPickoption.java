package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_pickoption")
public class CustomPickoption extends BaseEntity {
    private Long entityId;
    private Long itemId;
    private String apiKey;
    private Integer optionCode;
    private String optionLabel;
    private String optionLabelKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private String description;
}
