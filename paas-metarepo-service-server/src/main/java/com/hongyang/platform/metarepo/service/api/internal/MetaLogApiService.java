package com.hongyang.platform.metarepo.service.api.internal;

import com.hongyang.platform.metarepo.service.entity.metadata.MetaLog;
import com.hongyang.platform.metarepo.service.service.metadata.IMetaLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 元数据变更日志 API（内部运维接口）
 */
@Slf4j
@RestController
@RequestMapping("/metarepo/internal")
@RequiredArgsConstructor
public class MetaLogApiService {

    private final IMetaLogService metaLogService;

    @GetMapping("/meta-log")
    public List<MetaLog> listMetaLogs(@RequestParam(required = false) String metadataApiKey) {
        if (metadataApiKey != null) {
            return metaLogService.listByMetadataApiKey(metadataApiKey);
        }
        return metaLogService.listAll();
    }
}
