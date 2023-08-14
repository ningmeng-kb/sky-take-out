package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取文件上传与返回名称
 */
@Component
@ConfigurationProperties(prefix = "githubclub.config")
public class GithubClubUtil {
    @Autowired
    HttpUtils httpUtils;
    /**
     * 用户名
     */
    private String username;

    /**
     * token
     */
    private String token;

    /**
     * 仓库名
     */
    private String depository;

    /**
     * 统一提交信息
     */
    private String message;

    /**
     * 自定义域名
     */
    private String domain;

    private String GITHUBAPI = "https://api.github.com";

    private String DOWAPI = "https://raw.githubusercontent.com/ningmeng-kb/image/main/";

    /**
     * 文件推送到github
     * @param newFileName  文件名
     * @param content json 数据格式 {"message":"test commit","content":"bXkgbmV3IGZpbGUgY29udGVudHM="}
     * @return
     */
    public HashMap<String, Object> contentFile(String newFileName, String content){

        String url = GITHUBAPI + "/repos/"+username+"/"+depository+"/contents/"+newFileName;
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization","token "+token);
        header.put("Accept","Accept: application/vnd.github.v3+json");
        String json = "{\"message\":\""+message+"\",\"content\":\""+content+"\"}";
        String res = httpUtils.putJson(url, json, header);
        return filterResultForContentFile(res,newFileName);
    }

    /**
     * 处理调用接口后返回的值
     * @param res
     * @param newFileName
     * @return
     */
    private HashMap<String,Object> filterResultForContentFile(String res,String newFileName){
        Map<String, Object> map = JSONObject.parseObject(res);
        Map<String, Object> commit = (Map<String, Object>) map.get("commit");
        HashMap<String, Object> result = new HashMap<>();
        if(commit.get("message").equals(message)){
            result.put("success",true);
            result.put("data",DOWAPI+newFileName);
        }else{
            result.put("success",false);
        }
        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDepository() {
        return depository;
    }

    public void setDepository(String depository) {
        this.depository = depository;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}