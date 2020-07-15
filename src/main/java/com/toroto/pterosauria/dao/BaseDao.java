package com.toroto.pterosauria.dao;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author yulinfu
 * @date 2018/9/4
 */
public interface BaseDao<T> extends Mapper<T>, ConditionMapper<T>, MySqlMapper<T> {
}
