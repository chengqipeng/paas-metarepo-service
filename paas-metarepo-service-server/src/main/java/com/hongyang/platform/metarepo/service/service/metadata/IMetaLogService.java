package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.platform.metarepo.service.entity.metadata.MetaLog;

import java.util.List;

/**
 * 元数据变更日志 Service 接口
 */
public interface IMetaLogService {

    /**
     * 记录元数据变更日志。
     *
     * @param entityApiKey    变更的元数据 apiKey
     * @param metamodelApiKey 所属元模型 apiKey
     * @param oldValue        变更前 JSON 快照（创建时为 null）
     * @param newValue        变更后 JSON 快照（删除时为 null）
     * @param opType          操作类型：1=创建 2=更新 3=删除
     */
    void log(String entityApiKey, String metamodelApiKey,
             Object oldValue, Object newValue, int opType);

    List<MetaLog> listAll();

    List<MetaLog> listByMetadataApiKey(String metadataApiKey);
}
