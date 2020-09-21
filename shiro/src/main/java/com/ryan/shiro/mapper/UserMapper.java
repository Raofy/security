package com.ryan.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryan.shiro.entity.User;


public interface UserMapper extends BaseMapper<User> {

    User findByUserName(String name);
}
