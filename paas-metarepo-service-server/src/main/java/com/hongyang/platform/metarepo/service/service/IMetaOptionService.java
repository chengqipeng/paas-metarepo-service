package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.meta.MetaOption;

import java.util.List;
import java.util.Set;

/**
 * 元模型选项值 Service
 * 提供 MetaOption 查询和校验能力
 */
public interface IMetaOptionService extends IBaseService<MetaOption> {

    /**
     * 查询指定元模型字段项的所有选项值
     */
    List<MetaOption> listByItemApiKey(String metamodelApiKey, String itemApiKey);

    /**
     * 获取指定元模型字段项的合法 option_code 集合
     */
    Set<Integer> getValidCodes(String metamodelApiKey, String itemApiKey);

    /**
     * 校验 option_code 是否在合法范围内
     * @return true=合法
     */
    boolean isValidCode(String metamodelApiKey, String itemApiKey, Integer code);
}
