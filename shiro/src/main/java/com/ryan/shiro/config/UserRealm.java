package com.ryan.shiro.config;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ryan.shiro.entity.User;
import com.ryan.shiro.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IUserService iUserService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("执行授权方法：doGetAuthorizationInfo");

        //授权

        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("执行认证方法：doGetAuthenticationInfo");
        //内存中的用户名和密码
//        String name = "root";
//        String password = "123456";

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //连接数据库中的数据
        User user = iUserService.findByUserName(token.getUsername());
        if (ObjectUtils.isNull(user)) {
            return null;
        }

        //校验用户名是否存在
        if (!token.getUsername().equals(user.getUsername())) {
            return null;
        }

        //校验密码，shiro自身帮忙进行加密认证，
        return new SimpleAuthenticationInfo(user,user.getPassword(), "");
    }
}
