package com.hongyang.platform.metarepo.core.model.request;

import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class SavePickOptionRequest implements Serializable {
    private Long tenantId;
    private Long entityId;
    private Long itemId;
    private List<XPickOption> options;
    private Long operatorId;
}
