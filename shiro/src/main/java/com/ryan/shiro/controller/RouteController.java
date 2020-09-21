package com.ryan.shiro.controller;

import com.sun.deploy.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class RouteController {

    @GetMapping({"/home","/"})
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/toLogin")
    public String toLogin(@RequestParam String username,
                          String password) {
        //1.获取当前用户
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        log.info("执行了toLogin方法");
        //2.登录
        try {
            subject.login(token);
            return "hello";
        } catch (UnknownAccountException u) {
            log.info("用户不存在");
            return "login";
        } catch (IncorrectCredentialsException i) {
            log.info("密码不正确");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }


    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

}
