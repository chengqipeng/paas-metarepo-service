package com.hongyang.platform.metarepo.service.api.internal;

import com.hongyang.platform.metarepo.service.entity.MetaLog;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 元数据变更日志 API（内部运维接口）
 */
@Slf4j
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class MetaLogApiService {

    private final IMetaLogService metaLogService;

    /** 查询元数据变更日志 */
    @GetMapping("/meta-log/{tenantId}")
    public List<MetaLog> listMetaLogs(@PathVariable Long tenantId,
                                             @RequestParam(required = false) Long metadataId) {
        if (metadataId != null) {
            return metaLogService.listByMetadataId(tenantId, metadataId);
        }
        return metaLogService.listByTenant(tenantId);
    }
}
