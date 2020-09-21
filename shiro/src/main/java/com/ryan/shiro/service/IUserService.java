package com.ryan.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryan.shiro.entity.User;

public interface IUserService extends IService<User> {


    User findByUserName(String name);
}
