package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IMetaService;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaOption;

import java.util.List;

/**
 * 元模型选项值 Service 接口（p_meta_option）
 */
public interface IMetaOptionService extends IMetaService<MetaOption> {

    /**
     * 根据字段 apiKey 查询选项值列表
     */
    List<MetaOption> listByItemApiKey(String metamodelApiKey, String itemApiKey);

    /**
     * 根据字段 apiKey 删除所有选项值
     */
    boolean deleteByItemApiKey(String metamodelApiKey, String itemApiKey);

    /**
     * 校验 optionCode 是否在合法范围内
     */
    boolean isValidCode(String metamodelApiKey, String itemApiKey, Integer optionCode);
}
