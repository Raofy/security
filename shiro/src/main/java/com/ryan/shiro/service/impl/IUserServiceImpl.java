package com.ryan.shiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.shiro.entity.User;
import com.ryan.shiro.mapper.UserMapper;
import com.ryan.shiro.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author fuyi
 * @since 2020-09-17
 */
@Slf4j
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User findByUserName(String name) {
        QueryWrapper<User> username = new QueryWrapper<>();
        username.ge("username", name);
        return baseMapper.selectOne(username);
    }
}
