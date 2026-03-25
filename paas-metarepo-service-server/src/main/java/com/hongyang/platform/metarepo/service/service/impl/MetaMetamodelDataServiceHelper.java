package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomEntityEntity;
import com.hongyang.platform.metarepo.service.entity.MetaMetamodelDataEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 大宽表操作辅助 Service
 * 负责 p_custom_entity 与 p_meta_metamodel_data 的同步
 */
@Slf4j
@Service
public class MetaMetamodelDataServiceHelper
        extends SimpleBaseServiceImpl<MetaMetamodelDataEntity> {

    /**
     * 创建对象时同步写入大宽表
     */
    public void syncEntityToMetamodelData(CustomEntityEntity entity) {
        MetaMetamodelDataEntity data = new MetaMetamodelDataEntity();
        data.setTenantId(entity.getTenantId());
        data.setNamespace(entity.getNameSpace());
        data.setMetamodelId(1L); // metamodel_id=1 表示"对象"类型
        data.setApiKey(entity.getApiKey());
        data.setLabel(entity.getLabel());
        data.setLabelKey(entity.getLabelKey());
        data.setCustomFlg(entity.getCustomFlg());
        data.setDescription(entity.getDescription());
        save(data);

        // 回写 object_id 到 p_custom_entity
        entity.setObjectId(data.getId());
    }
}
