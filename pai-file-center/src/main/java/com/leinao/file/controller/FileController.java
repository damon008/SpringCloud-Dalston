package com.leinao.file.controller;

import com.leinao.commons.Response;
import com.leinao.file.service.FileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @Description 文件下载控制类
 * @author lizi
 * @date 2018/10/16 上午11:19
 * @Version 1.0
 */
@RestController
@CrossOrigin
public class FileController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileService fileService;

    @ApiOperation(value="获取用户根目录", notes="返回用户根目录")
    @GetMapping(value = "/file/getBaseDir")
    public Response getBaseDir(@ApiParam(value = "用户token", required = true) @RequestParam String token) {
        try {
            return fileService.getBaseDir(token);
        } catch (Exception e) {
            logger.error("file getBaseDir failed");
            logger.error(e.getMessage(), e);
            return Response.error();
        }
    }


    @ApiOperation(value="获取文件列表", notes="返回文件列表")
    @GetMapping(value = "/file/getFileList")
    public Response getFileList(@ApiParam(value = "目录", required = true) @RequestParam String dir,
                                @ApiParam(value = "用户token", required = true) @RequestParam String token) {
        try {
            return fileService.getFileList(token, dir);
        } catch (Exception e) {
            logger.error("file getFileList failed");
            logger.error(e.getMessage(), e);
            return Response.error();
        }
    }

    @ApiOperation(value="创建文件夹", notes="返回文件夹创建结果")
    @PostMapping(value = "/file/addDir")
    public Response addDir(@ApiParam(value = "目录", required = true) @RequestParam String dir,
                           @ApiParam(value = "用户token", required = true) @RequestParam String token) {
        try {
            return fileService.addDir(token, dir);
        } catch (Exception e) {
            logger.error("file addDir failed");
            logger.error(e.getMessage(), e);
            return Response.error();
        }
    }

    @ApiOperation(value="删除文件", notes="返回删除文件结果")
    @PostMapping(value = "/file/deleteFile")
    public Response deleteFile(@ApiParam(value = "目录", required = true) @RequestParam String dir,
                               @ApiParam(value = "用户token", required = true) @RequestParam String token) {
        try {
            return fileService.deleteFile(token, dir);
        } catch (Exception e) {
            logger.error("file deleteFile failed");
            logger.error(e.getMessage(), e);
            return Response.error();
        }
    }


    @ApiOperation(value="文件或文件夹重命名", notes="返回文件或文件夹重命名结果")
    @PostMapping(value = "/file/renameFile")
    public Response renameFile(@ApiParam(value = "旧目录名称", required = true) @RequestParam String dir,
                               @ApiParam(value = "新目录名称", required = true) @RequestParam String newDir,
                               @ApiParam(value = "用户token", required = true) @RequestParam String token) {
        try {
            return fileService.renameFile(token, dir, newDir);
        } catch (Exception e) {
            logger.error("file renameFile failed");
            logger.error(e.getMessage(), e);
            return Response.error();
        }
    }

    @ApiOperation(value="文件或文件夹复制", notes="返回文件或文件夹复制结果")
    @PostMapping(value = "/file/copyFile")
    public Response copyFile(@ApiParam(value = "旧目录名称", required = true) @RequestParam String dir,
                             @ApiParam(value = "用户token", required = true) @RequestParam String token) {
        try {
            return fileService.copyFile(token, dir);
        } catch (Exception e) {
            logger.error("file copyFile failed");
            logger.error(e.getMessage(), e);
            return Response.error();
        }
    }
}
