package com.wangyou.websocketforandroid.controller;

import com.wangyou.websocketforandroid.entity.Group;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.service.GroupService;
import com.wangyou.websocketforandroid.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;


    @RequestMapping("/imageHeader/{type}/{id}")
    public ResponseEntity<?> getImage(@PathVariable("type") String type, @PathVariable("id") String id) throws FileNotFoundException {
        String fileName = "";
        if (Objects.equals(type, "0")) {
            User user = userService.getById(id);
            if (user != null) {
                fileName = user.getImageUrl();
            }
        } else {
            Group group = groupService.getById(id);
            if (group != null) {
                fileName = group.getGroupImage();
            }
        }
        if (Strings.isEmpty(fileName)) {
            fileName = "Default.jpg";
        }
        String dir = ResourceUtils.getURL("classpath:").getPath() + "static" + File.separator + fileName;
        Resource resource = resourceLoader.getResource("file:" + dir);
        return ResponseEntity.ok(resource);
    }

    @RequestMapping("/uploadImage")
    public ResponseData<String> uploadImage(@RequestParam("imageFile") MultipartFile imageFile,
                                            @RequestParam("type") Integer type,
                                            @RequestParam("id") Long id) throws IOException {
        String dir = ResourceUtils.getURL("classpath:").getPath() + "static" + File.separator;
        String fileName = imageFile.getOriginalFilename();
        ResponseData<String> responseData = ResponseData.<String>builder()
                .code("200")
                .msg("进行上传")
                .data("上传成功")
                .build();
        //判断是否有文件且是否为图片
        if (!Strings.isEmpty(fileName) && isImageFile(fileName)) {
            String saveFilename = UUID.randomUUID().toString() + getFileType(fileName);
            if (type == 0) {
                User user = User.builder().uid(id).imageUrl(saveFilename).build();
                userService.updateById(user);
            } else {
                Group group = Group.builder().gid(id).groupImage(saveFilename).build();
                groupService.updateById(group);
            }
            //创建输出文件对象
            try {
                File outFile = new File(dir + saveFilename);
                imageFile.transferTo(outFile);
            } catch (IOException e) {
                e.printStackTrace();
                responseData.setData("上传失败");
            }
        } else {
            responseData.setData("格式错误");
        }
        return responseData;
    }


    private boolean isImageFile(String fileName) {
        String[] img_type = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        if (Strings.isEmpty(fileName)) {
            return false;
        }
        fileName = fileName.toLowerCase();

        for (String type : img_type) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }

        return false;
    }


    private String getFileType(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }
}
