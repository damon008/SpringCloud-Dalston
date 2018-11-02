package com.leinao.file.model;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @Description 文件上传Bean
 * @author lizi
 * @date 2018/10/17 下午4:22
 * @Version 1.0
 */
public class FileUploadBean {
    /**
     * 用户Token
     */
    private String token;

    /**
     * 目录
     */
    private String dir;

    /**
     * 文件或文件夹上传
     */
    private MultipartFile[] file;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public MultipartFile[] getFile() {
        return file;
    }

    public void setFile(MultipartFile[] file) {
        this.file = file;
    }
}
