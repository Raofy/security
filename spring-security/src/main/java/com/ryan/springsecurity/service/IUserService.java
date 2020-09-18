package com.ryan.springsecurity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryan.springsecurity.entity.User;

public interface IUserService extends IService<User> {

    /**
     * 通过用户名进行查找用户
     *
     * @param name
     * @return
     */
    User findByUserName(String name);

    /**
     * 用户登录操作
     *
     * @param user
     * @return
     */
    boolean login(User user);
}
