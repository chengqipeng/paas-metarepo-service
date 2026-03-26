package com.hongyang.platform.metarepo.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongyang.platform.metarepo.service.entity.metamodel.common.CommonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Common 级对象 Service（p_custom_entity_common，无 tenant_id）
 */
@Slf4j
@Service
public class CommonEntityService extends ServiceImpl<BaseMapper<CommonEntity>, CommonEntity> {
}
