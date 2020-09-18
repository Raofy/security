package com.ryan.springsecurity.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryan.springsecurity.entity.User;
import com.ryan.springsecurity.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/security")
public class RouteController {

    @GetMapping("home")
    public String home() {
        return "home";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

}

//@Controller
//@RequestMapping("/security")
//public class RouteController {
//
//    @Autowired
//    private IUserService iUserService;
//
//    @Autowired
//    private AuthenticationManagerBuilder auth;
//
//    @GetMapping("home")
//    public String home() {
//        return "home";
//    }
//
//    @GetMapping("login")
//    public String login(@RequestBody User user) {
//        User byUserName = iUserService.findByUserName(user.getUsername());
//        if (Objects.isNull(byUserName)) {
//            return "没有该用户";
//        }
//        if (iUserService.login(byUserName)) {
//            return "hello";
//        }
//        return "login";
//    }
//
//    @GetMapping("hello")
//    public String hello() {
//        return "hello";
//    }
//}
