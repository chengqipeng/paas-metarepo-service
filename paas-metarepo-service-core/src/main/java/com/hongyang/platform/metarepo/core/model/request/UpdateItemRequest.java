package com.hongyang.platform.metarepo.core.model.request;

import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateItemRequest extends XItem {
    private Long operatorId;
}
