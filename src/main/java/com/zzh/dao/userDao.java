package com.zzh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzh.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface userDao extends BaseMapper<User> {

}
