package com.leinao.constant;

/**
 *
 * @Description 文件中心错误码
 * @author lizi
 * @date 2018/10/17 上午11:15
 * @Version 1.0
 */
public enum FileErrorEnum {
    FILE_EXIST(10000, "file already exist"),   // 文件已经存在
    FILE_NOT_EXIST(10001, "file not exist"), // 文件不存在
    FILE_EMPTY(10002, "file is empty"), // 文件必传
    PARAM_ERROR(20000, "param error"), // 请求参数错误
    TOKEN_ERROR(30000, "token error"),   // Token无效
    FILE_SIZE_ERROR(40000, "file size overflow max value"), // 文件超出大小限制
    FILE_UPLOAD_ERROR(40001, "file upload error"),// 文件上传失败
    FILE_DOWNLOAD_ERROR(40002, "file download error");// 文件下载失败


    private int code;
    private String message;

    FileErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
