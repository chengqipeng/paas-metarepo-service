package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.MetaOption;

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
    List<MetaOption> listByItemId(Long metamodelId, Long itemId);

    /**
     * 获取指定元模型字段项的合法 option_code 集合
     */
    Set<Integer> getValidCodes(Long metamodelId, Long itemId);

    /**
     * 校验 option_code 是否在合法范围内
     * @return true=合法
     */
    boolean isValidCode(Long metamodelId, Long itemId, Integer code);
}
