package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.GithubClubUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private GithubClubUtil githubClubUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);

        //获取原始文件名
        String originalFilename = file.getOriginalFilename();

        //构造唯一的文件名（不能重复） -uuid
        int index = originalFilename.lastIndexOf(".");
        String extname = originalFilename.substring(index);
        String newFileName = UUID.randomUUID().toString() + extname;
        log.info("新的文件名：{}",newFileName);

        //处理图片格式
        Base64 base64Encoder = new Base64();
        byte[] imageBytes = null;
        String base64EncoderImg="";

        try{
            imageBytes = file.getBytes();
            base64EncoderImg = base64Encoder.encodeToString(imageBytes);
            HashMap map =  githubClubUtil.contentFile(newFileName,base64EncoderImg);
            if((Boolean)map.get("success")){

                String filePath = (String) map.get("data");

                return Result.success(filePath);
            }
        }catch (Exception e){
            log.error("文件上传失败：{}", e);
        }
        return Result.error("上传失败");
    }


}