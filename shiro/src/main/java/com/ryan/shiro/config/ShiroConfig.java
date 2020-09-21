package com.ryan.shiro.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //shiroFilterFactoryBean （第三步）
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean (DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro内置过滤器
        /**
         * 常见配置参数
         * “anon” : 无需认证即可访问
         * “authc” : 必须认证后方可访问
         * “user” : 必须拥有我功能才能使用
         * “perms” : 拥有某个资源的权限才能访问
         * “role” : 拥有某个角色权限才能访问
         */
        Map<String, String> filterMap = new LinkedHashMap<>();

        //拦截配置
        filterMap.put("/home", "anon");

        //授权配置
        filterMap.put("/hello", "perms[hello]");

        bean.setFilterChainDefinitionMap(filterMap);
        bean.setLoginUrl("/login");
        bean.setUnauthorizedUrl("/unauthorized");
        return bean;
    }

    //DefaultWebSecurityBean （第二步）
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(UserRealm userRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 关联userRealm对象
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    //创建userRealm对象，需要自定义类实现(第一步)
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }
}
