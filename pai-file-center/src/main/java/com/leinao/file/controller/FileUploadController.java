package com.leinao.file.controller;

import com.leinao.commons.Response;
import com.leinao.commons.ResponseMessage;
import com.leinao.constant.FileErrorEnum;
import com.leinao.file.model.FileUploadBean;
import com.leinao.file.service.FileService;
import com.leinao.util.FileUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 *
 * @Description 文件上传控制类
 * @author lizi
 * @date 2018/10/16 上午11:19
 * @Version 1.0
 */
@RestController
@CrossOrigin
public class FileUploadController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileService fileService;

    @ApiOperation(value="文件或文件夹上传", notes="文件或文件夹上传结果")
    @PostMapping(value = "/file/upload")
    public Response fileUpload(FileUploadBean fileUploadBean) {
        try {
            String token = fileUploadBean.getToken();
            String dir = fileUploadBean.getDir();
            if (StringUtils.isBlank(token) || StringUtils.isBlank(dir)) {
                return new Response(ResponseMessage.error(FileErrorEnum.PARAM_ERROR.getCode(), FileErrorEnum.PARAM_ERROR.getMessage()));
            }

            // 文件必传
            MultipartFile[] uploadFileList = fileUploadBean.getFile();
            if (uploadFileList == null || uploadFileList.length == 0) {
                return new Response(ResponseMessage.error(FileErrorEnum.FILE_EMPTY.getCode(), FileErrorEnum.FILE_EMPTY.getMessage()));
            }
            for(MultipartFile uploadFile : uploadFileList) {
                // 文件保存
                String originalFilename = uploadFile.getOriginalFilename().trim();
                BigDecimal all = new BigDecimal(0);
                String fileName = dir + File.separator + originalFilename;
                String dirPath = fileName.substring(0, fileName.lastIndexOf('/'));
                File dirPathFile = new File(dirPath);
                if (!dirPathFile.exists()) {
                    fileService.addDir(token, dirPath);
                }
                File dirFile = new File(fileName);
                // 存在同名文件则覆盖，不用判断文件是否存在
                InputStream in = uploadFile.getInputStream();
                uploadFile.transferTo(dirFile);
                long fileSize = FileUtil.getFileSize(dir + File.separator + fileName);
                all = new BigDecimal(fileSize).add(all);
                in.close();

                fileService.updateFailureTime(token);
                fileService.insertUserJobBandwidth(token, all);
                fileService.insertUploadFile(token, dirFile);
            }
            return Response.ok();
        } catch (Exception e) {
            logger.error("file upload failed");
            logger.error(e.getMessage(), e);
            return new Response(ResponseMessage.error(FileErrorEnum.FILE_UPLOAD_ERROR.getCode(), FileErrorEnum.FILE_UPLOAD_ERROR.getMessage()));
        }
    }
}
