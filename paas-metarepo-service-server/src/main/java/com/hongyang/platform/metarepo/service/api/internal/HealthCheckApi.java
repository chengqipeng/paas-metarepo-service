package com.hongyang.platform.metarepo.service.api.internal;

import com.hongyang.platform.metarepo.service.service.metadata.IEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthCheckApi {

    @Autowired
    private IEntityService entityService;

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/debug/entities")
    public Map<String, Object> debugEntities() {
        try {
            var list = entityService.listMerged();
            return Map.of("success", true, "count", list.size());
        } catch (Exception e) {
            return Map.of("success", false, "error", e.getClass().getName(), "message", e.getMessage() != null ? e.getMessage() : "null", "cause", e.getCause() != null ? e.getCause().getClass().getName() + ": " + e.getCause().getMessage() : "none");
        }
    }
}
