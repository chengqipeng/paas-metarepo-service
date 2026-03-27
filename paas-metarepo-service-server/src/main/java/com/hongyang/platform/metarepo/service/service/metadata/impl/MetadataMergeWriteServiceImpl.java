package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.platform.metarepo.service.common.converter.CommonMetadataConverter;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;
import com.hongyang.platform.metarepo.service.service.metadata.IMetaLogService;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeReadService;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeWriteService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaModelService;
import com.hongyang.platform.metarepo.service.service.metamodel.ITenantMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户级元数据写入 Service 实现。
 * <p>
 * 所有写操作均写入 p_tenant_metadata 大宽表：
 * 1. 通过 CommonMetadataConverter.toTenantMetadata() 将业务 Entity 转为 TenantMetadata
 * 2. 通过 ITenantMetadataService 执行 CRUD
 * 3. 通过 IMetaLogService 记录变更日志
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataMergeWriteServiceImpl implements IMetadataMergeWriteService {

    private final ITenantMetadataService tenantMetadataService;
    private final IMetaItemService metaItemService;
    private final IMetaModelService metaModelService;
    private final IMetaLogService metaLogService;
    private final IMetadataMergeReadService metadataMergeReadService;

    private static final int OP_CREATE = 1;
    private static final int OP_UPDATE = 2;
    private static final int OP_DELETE = 3;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseMetaTenantEntity> T create(String metamodelApiKey, T entity, Long operatorId) {
        validateMetaModel(metamodelApiKey);

        // 检查 apiKey 唯一性（合并后）
        String apiKey = entity.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, "apiKey 不能为空");
        }
        Object existing = metadataMergeReadService.getByApiKeyMerged(metamodelApiKey, apiKey, entity.getClass());
        if (existing != null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, apiKey);
        }

        // Entity → TenantMetadata
        Map<String, String> columnMapping = getColumnMapping(metamodelApiKey);
        TenantMetadata row = CommonMetadataConverter.toTenantMetadata(entity, metamodelApiKey, columnMapping);
        if (row == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, "Entity 转换失败");
        }
        row.setMetadataApiKey(apiKey);
        row.setCustomFlg(1);
        row.setDeleteFlg(0);
        long now = System.currentTimeMillis();
        row.setCreatedAt(now);
        row.setCreatedBy(operatorId);
        row.setUpdatedAt(now);
        row.setUpdatedBy(operatorId);

        tenantMetadataService.createMetadata(row);

        // 记录变更日志
        metaLogService.log(apiKey, metamodelApiKey, null, entity, OP_CREATE);

        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseMetaTenantEntity> T update(String metamodelApiKey, T entity, Long operatorId) {
        validateMetaModel(metamodelApiKey);

        String apiKey = entity.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, "apiKey 不能为空");
        }

        // 查询旧值（用于日志）
        T oldEntity = metadataMergeReadService.getByApiKeyMerged(metamodelApiKey, apiKey, (Class<T>) entity.getClass());
        if (oldEntity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, apiKey);
        }

        // 查找 Tenant 表中是否已有记录
        TenantMetadata existingRow = tenantMetadataService.getByMetamodelApiKeyAndApiKey(metamodelApiKey, apiKey);
        Map<String, String> columnMapping = getColumnMapping(metamodelApiKey);
        TenantMetadata row = CommonMetadataConverter.toTenantMetadata(entity, metamodelApiKey, columnMapping);
        if (row == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, "Entity 转换失败");
        }
        row.setMetadataApiKey(apiKey);
        long now = System.currentTimeMillis();
        row.setUpdatedAt(now);
        row.setUpdatedBy(operatorId);

        if (existingRow != null) {
            // Tenant 表已有记录 → 更新
            row.setId(existingRow.getId());
            row.setTenantId(existingRow.getTenantId());
            row.setCreatedAt(existingRow.getCreatedAt());
            row.setCreatedBy(existingRow.getCreatedBy());
            tenantMetadataService.updateMetadata(row);
        } else {
            // Common 级数据的 Tenant 覆盖 → 插入新记录
            row.setCustomFlg(1);
            row.setDeleteFlg(0);
            row.setCreatedAt(now);
            row.setCreatedBy(operatorId);
            tenantMetadataService.createMetadata(row);
        }

        // 记录变更日志
        metaLogService.log(apiKey, metamodelApiKey, oldEntity, entity, OP_UPDATE);

        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends BaseMetaTenantEntity> void delete(String metamodelApiKey, String apiKey,
                                                         Class<T> entityClass, Long operatorId) {
        validateMetaModel(metamodelApiKey);

        // 查询旧值（用于日志）
        T oldEntity = metadataMergeReadService.getByApiKeyMerged(metamodelApiKey, apiKey, entityClass);
        if (oldEntity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, apiKey);
        }

        TenantMetadata existingRow = tenantMetadataService.getByMetamodelApiKeyAndApiKey(metamodelApiKey, apiKey);

        if (existingRow != null) {
            // Tenant 表已有记录 → 软删除
            tenantMetadataService.softDeleteMetadata(existingRow, operatorId);
        } else {
            // Common 级数据的遮蔽删除 → 插入 delete_flg=1 的 Tenant 记录
            Map<String, String> columnMapping = getColumnMapping(metamodelApiKey);
            TenantMetadata row = CommonMetadataConverter.toTenantMetadata(oldEntity, metamodelApiKey, columnMapping);
            if (row != null) {
                row.setMetadataApiKey(apiKey);
                row.setDeleteFlg(1);
                long now = System.currentTimeMillis();
                row.setCreatedAt(now);
                row.setCreatedBy(operatorId);
                row.setUpdatedAt(now);
                row.setUpdatedBy(operatorId);
                tenantMetadataService.createMetadata(row);
            }
        }

        // 记录变更日志
        metaLogService.log(apiKey, metamodelApiKey, oldEntity, null, OP_DELETE);
    }

    // ==================== 内部方法 ====================

    private void validateMetaModel(String metamodelApiKey) {
        MetaModel metaModel = metaModelService.getByApiKey(metamodelApiKey);
        if (metaModel == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, metamodelApiKey);
        }
    }

    private Map<String, String> getColumnMapping(String metamodelApiKey) {
        List<MetaItem> items = metaItemService.listByMetamodelApiKey(metamodelApiKey);
        Map<String, String> map = new HashMap<>();
        for (MetaItem item : items) {
            if (item.getDbColumn() != null && item.getApiKey() != null) {
                map.put(item.getDbColumn(), snakeToCamel(item.getApiKey()));
            }
        }
        return map;
    }

    private static String snakeToCamel(String snake) {
        if (snake == null || !snake.contains("_")) return snake;
        StringBuilder sb = new StringBuilder();
        boolean upper = false;
        for (char c : snake.toCharArray()) {
            if (c == '_') {
                upper = true;
            } else {
                sb.append(upper ? Character.toUpperCase(c) : c);
                upper = false;
            }
        }
        return sb.toString();
    }
}
