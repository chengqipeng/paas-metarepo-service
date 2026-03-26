package com.hongyang.platform.metarepo.service.repository;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomEntity;
import com.hongyang.platform.metarepo.service.entity.MetaMetamodelData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 大宽表操作辅助 Service
 * 负责 p_custom_entity 与 p_meta_metamodel_data 的双向同步
 */
@Slf4j
@Service
public class MetaMetamodelDataServiceHelper
        extends SimpleBaseServiceImpl<MetaMetamodelData> {

    private static final Long METAMODEL_ID_ENTITY = 1L;

    /**
     * 创建对象时同步写入大宽表
     */
    public void syncEntityToMetamodelData(CustomEntity entity) {
        MetaMetamodelData data = new MetaMetamodelData();
        data.setTenantId(entity.getTenantId());
        data.setNamespace(entity.getNameSpace());
        data.setMetamodelId(METAMODEL_ID_ENTITY);
        data.setApiKey(entity.getApiKey());
        data.setLabel(entity.getLabel());
        data.setLabelKey(entity.getLabelKey());
        data.setCustomFlg(entity.getCustomFlg());
        data.setDescription(entity.getDescription());
        save(data);
        // 回写 entity_id
        entity.setEntityId(data.getId());
    }

    /**
     * 更新对象时同步更新大宽表
     */
    public void syncEntityUpdateToMetamodelData(CustomEntity entity) {
        if (entity.getEntityId() == null) {
            log.warn("对象 {} 无 entityId，跳过大宽表同步", entity.getId());
            return;
        }
        MetaMetamodelData data = getById(entity.getEntityId());
        if (data == null) {
            log.warn("大宽表记录不存在, entityId={}", entity.getEntityId());
            return;
        }
        data.setLabel(entity.getLabel());
        data.setLabelKey(entity.getLabelKey());
        data.setDescription(entity.getDescription());
        updateById(data);
    }

    /**
     * 删除对象时同步软删除大宽表记录
     */
    public void syncEntityDeleteToMetamodelData(CustomEntity entity) {
        if (entity.getEntityId() == null) return;
        removeById(entity.getEntityId());
    }
}
