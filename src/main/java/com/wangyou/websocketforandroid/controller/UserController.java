package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.GroupRelation;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.service.GroupRelationService;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GroupRelationService groupRelationService;

    @PostMapping("/signUp")
    public ResponseData<User> signUp(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userService.signUp(username, password);
    }

    @GetMapping("findUserById")
    public ResponseData<User> findUserById(@RequestParam("uid") Long uid) {
        return ResponseData.<User>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUid, uid).eq(User::getEnable, 1)))
                .build();
    }

    @GetMapping("/findUserByUsername")
    public ResponseData<User> findUserByUsername(@RequestParam("username") String username) {
        return ResponseData.<User>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.getOne(Wrappers.<User>lambdaQuery()
                        .eq(User::getEnable, 1)
                        .and(i -> i.eq(User::getUsername, username)
                                .or()
                                .eq(User::getPhone, username))))
                .build();
    }

    @GetMapping("/findFriends")
    public ResponseData<List<User>> findFriend(Principal principal) {
        List<User> friendList = userService.findFriend(principal.getName());
        return ResponseData.<List<User>>builder()
                .code("200")
                .msg("获取成功")
                .data(friendList)
                .build();
    }

    @GetMapping("/searchUser")
    public ResponseData<List<User>> searchUser(@RequestParam("searchKey") String searchKey) {
        return ResponseData.<List<User>>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.list(Wrappers.<User>lambdaQuery()
                        .eq(User::getEnable, 1)
                        .and(i -> i.like(User::getUsername, searchKey)
                                .or()
                                .like(User::getPhone, searchKey)
                                .or()
                                .like(User::getRealName, searchKey))))
                .build();
    }

    @GetMapping("/findUserByPrincipal")
    public ResponseData<User> findUserByPrincipal(Principal principal) {
        return ResponseData.<User>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.getOne(Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername, principal.getName())))
                .build();
    }

    @PostMapping("/findUserListByIds")
    public ResponseData<List<User>> findUserByIds(@RequestBody List<Long> ids) {
        return ResponseData.<List<User>>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.list(Wrappers.<User>lambdaQuery().eq(User::getEnable, 1).in(User::getUid, ids)))
                .build();
    }

    @GetMapping("/findLeader")
    public ResponseData<User> findLeader(@RequestParam("gid") Long gid){
        return ResponseData.<User>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.findLeader(gid))
                .build();
    }

    @GetMapping("/findMembers")
    public ResponseData<List<User>> findMembers(@RequestParam("gid") Long gid){
        return ResponseData.<List<User>>builder()
                .code("200")
                .msg("获取成功")
                .data(userService.findMembers(gid))
                .build();
    }


}
