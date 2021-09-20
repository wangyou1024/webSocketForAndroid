package com.wangyou.websocketforandroid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangyou.websocketforandroid.entity.Group;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.service.GroupRelationService;
import com.wangyou.websocketforandroid.service.GroupService;
import com.wangyou.websocketforandroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    GroupRelationService groupRelationService;

    @PostMapping("/createGroup")
    public ResponseData<Group> createGroup(Principal principal, @RequestBody Group group) {
        return groupService.create(principal.getName(), group);
    }

    @GetMapping("findGroupById")
    public ResponseData<Group> findGroupById(@RequestParam("gid") Long gid){
        return ResponseData.<Group>builder()
                .code("200")
                .msg("获取成功")
                .data(groupService.getById(gid))
                .build();
    }

    @GetMapping("/findJoinedGroups")
    public ResponseData<List<Group>> findJoinedGroups(Principal principal) {
        return ResponseData.<List<Group>>builder()
                .code("200")
                .msg("获取成功")
                .data(groupService.findGroupList(principal.getName()))
                .build();
    }

    @GetMapping("/searchGroups")
    public ResponseData<List<Group>> searchGroups(@RequestParam("searchKey") String searchKey) {
        return ResponseData.<List<Group>>builder()
                .code("200")
                .msg("获取成功")
                .data(groupService.list(Wrappers.<Group>lambdaQuery()
                        .eq(Group::getEnable, 1)
                        .and(i -> i.like(Group::getGroupName, searchKey)
                                .or()
                                .like(Group::getGroupNum, searchKey))
                ))
                .build();
    }

    @PostMapping("/findGroupListByIds")
    public ResponseData<List<Group>> findGroupListByIds(@RequestBody List<Long> ids) {
        return ResponseData.<List<Group>>builder()
                .code("200")
                .msg("获取成功")
                .data(groupService.list(Wrappers.<Group>lambdaQuery()
                        .eq(Group::getEnable, 1)
                        .in(Group::getGid, ids)))
                .build();
    }

    @PostMapping("/updateGroup")
    public ResponseData<Group> updateGroup(@RequestBody Group group) {
        groupService.updateById(group);
        return ResponseData.<Group>builder()
                .code("200")
                .msg("修改成功")
                .data(group)
                .build();
    }
}
