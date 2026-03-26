package com.hongyang.platform.metarepo.service.entity.metamodel.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import java.io.Serializable;

/**
 * Common 级字段选项值（p_common_pickoption，无 tenant_id）
 */
@Data
@TableName("p_common_pick_option")
public class CommonPickOption extends BaseEntity {

    private String entityApiKey;
    private String itemApiKey;
    private String apiKey;
    private String namespace;
    private String label;
    private String labelKey;
    private Integer optionCode;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
}
