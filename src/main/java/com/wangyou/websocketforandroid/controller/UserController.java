package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signUp")
    public ResponseData<User> signUp(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userService.signUp(username, password);
    }

    @GetMapping("/findUserByUsername")
    public ResponseData<User> findUserByUsername(@RequestParam("username") String username) {
        return ResponseData.<User>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username).or().eq(User::getPhone, username)))
                .build();
    }

    @GetMapping("/findUserByPrincipal")
    public ResponseData<User> findUserByPrincipal(Principal principal) {
        return ResponseData.<User>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, principal.getName())))
                .build();
    }

}
