package com.wangyou.websocketforandroid.controller;

import com.wangyou.websocketforandroid.entity.Group;
import com.wangyou.websocketforandroid.entity.ResponseData;
import com.wangyou.websocketforandroid.entity.User;
import com.wangyou.websocketforandroid.service.GroupService;
import com.wangyou.websocketforandroid.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    final static String dir = "D:/code/file/";
    // final static String dir = "/root/webapp/file/androidChat/";

    @RequestMapping("/imageHeader/{type}/{id}")
    public void getImage(HttpServletResponse httpServletResponse, @PathVariable("type") String type, @PathVariable("id") String id) throws IOException {
        OutputStream out = null;
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
        try {
            byte[] byteArray;
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName + " ");
            InputStream input = new FileInputStream(new File(dir+fileName));
            int lenth = input.available();
            byteArray = new byte[lenth];
            input.read(byteArray);
            input.close();
            out = httpServletResponse.getOutputStream();
            out.write(byteArray);
            out.flush();
            out.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
            if (out != null)
                out.close();
        }
    }

    @RequestMapping("/uploadImage")
    public ResponseData<String> uploadImage(@RequestParam("imageFile") MultipartFile imageFile,
                                            @RequestParam("type") Integer type,
                                            @RequestParam("id") Long id) throws IOException {
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
