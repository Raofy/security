package com.ryan.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.springsecurity.entity.User;
import com.ryan.springsecurity.mapper.UserMapper;
import com.ryan.springsecurity.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.AccountExpiredException;

/**
 * <p>
 *  用户服务实现类
 * </p>
 *
 * @author fuyi
 * @since 2020-09-17
 */
@Slf4j
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public User findByUserName(String name) {

//        return userMapper.selectOne(Wrappers.<User>lambdaQuery()
//                .select(User::getId, User::getUsername, User::getPassword)
//                .eq(User::getUsername, name));
        return null;
    }

    @Override
    public boolean login(User user) {
//        Authentication authentication;
//        try {
//            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                log.info("用户名或密码错误");
//                return false;
//            } else if (e instanceof DisabledException) {
//                log.info("账户被禁用");
//                return false;
//            } else if (e instanceof AccountExpiredException) {
//                log.info("账户过期无法验证");
//                return false;
//            } else {
//                log.info("账户被锁定,无法登录");
//                return false;
//            }
//        }
//
        return true;
    }
}
