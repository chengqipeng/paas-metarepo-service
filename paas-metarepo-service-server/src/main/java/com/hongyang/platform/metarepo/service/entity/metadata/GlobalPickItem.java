package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 全局选项集（x_global_pickitem）
 * 该表无 delete_flg，不继承 BaseEntity
 */
@Data
@TableName("x_global_pickitem")
public class GlobalPickItem implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long tenantId;
    private String name;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer customFlg;
    private String description;
    private String descriptionKey;
    private Long createdBy;
    private Long createdAt;
    private Long updatedBy;
    private Long updatedAt;
}
