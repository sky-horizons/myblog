package com.myblog.web.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author: sky-horizons
 * @Date: 2022/2/13 19:58
 */
@RequestMapping("/admin")
@RestController
public class ImageController {
    @PostMapping("/uploadRawImage")
    public Map uploadRawImage(@RequestParam(value = "faceInfo") MultipartFile image, RedirectAttributes attributes){
//        文件名
        String fileName = image.getOriginalFilename();
//        存储位置
        String filePath = getClass().getClassLoader().getResource("static").getPath()+"/images/firstPicture/raw/"+fileName;
//        相对存储位置
        String relativePath ="/images/firstPicture/raw/"+fileName;
        File saveFile=new File(filePath);
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        try {
            image.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map map=new HashMap<>();
        map.put("url",relativePath);
        map.put("state",true);
        return map;
    }

    @PostMapping("/uploadCroppedImage")
    public Map uploadImage(@RequestParam(value = "faceInfo") MultipartFile image, RedirectAttributes attributes){
//        文件名
        String fileName = image.getOriginalFilename();
//        存储位置
        String filePath = getClass().getClassLoader().getResource("static").getPath()+"/images/firstPicture/cropped/"+fileName;
//        相对存储位置
        String relativePath ="/images/firstPicture/cropped/"+fileName;
        File saveFile=new File(filePath);
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        try {
            image.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map map=new HashMap<>();
        map.put("url",relativePath);
        map.put("state",true);
        return map;
    }

    @PostMapping("/uploadBlogImage")
    public Map uploadBlogImage(MultipartHttpServletRequest request){
//        单次只能上传一张图片
        Iterator<String> iterator=request.getFileNames();
        MultipartFile image= request.getFile(iterator.next());
        //        文件名
        String fileName = image.getOriginalFilename();
//        存储位置
        String filePath = getClass().getClassLoader().getResource("static").getPath()+"/images/blog/"+fileName;
//        相对存储位置
        String relativePath ="/images/blog/"+fileName;
        File saveFile=new File(filePath);
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        try {
            image.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map map=new HashMap<>();
        Map data=new HashMap();
        ArrayList list=new ArrayList();
        data.put("url",relativePath);
        data.put("alt","");
        data.put("href","");
        map.put("errno",0);
        list.add(data);
        map.put("data",list);
        return map;
    }
}
