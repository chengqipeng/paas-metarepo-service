package com.hongyang.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class PickOptionDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long entityId;
    private Long itemId;
    private String apiKey;
    private Integer optionCode;
    private String optionLabel;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer enableFlg;
}
