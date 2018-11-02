package com.leinao.file.controller;

import com.leinao.file.service.FileService;
import com.leinao.util.FileUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @Description 文件下载控制类
 * @author lizi
 * @date 2018/10/16 上午11:19
 * @Version 1.0
 */
@RestController
@CrossOrigin
public class FileDownloadController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileService fileService;

    @ApiOperation(value="文件下载", notes="文件下载")
    @GetMapping(value = "/file/download")
    public void fileDownload(@ApiParam(value = "目录名", required = true) @RequestParam String dir,
                             @ApiParam(value = "文件名", required = true) @RequestParam String filename,
                             @ApiParam(value = "用户token", required = true) @RequestParam String token,
                             HttpServletResponse response) {
        try {
            Map<String, Object> requestMap = new HashMap<String, Object>();
            dir = new String(dir.getBytes("ISO-8859-1"), "UTF-8");
            filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
            token = new String(token.getBytes("ISO-8859-1"), "UTF-8");
            requestMap.put("token", token);
            // 通知浏览器以下载的方式打开
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));

            long fileSize = FileUtil.getFileSize(dir + File.separator + filename);
            byte[] buffer = null;
            InputStream fis = new BufferedInputStream(new FileInputStream(dir + File.separator + filename));
            buffer = new byte[fis.available()];
            fis.read(buffer);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);

            fileService.updateFailureTime(token);
            fileService.insertUserDownBandWidth(token, new BigDecimal(fileSize));
            fis.close();
            toClient.flush();
            toClient.close();
        } catch (Exception e) {// 抛出异常时才会进入这个方法
            logger.error("file download failed");
            logger.error(e.getMessage(), e);
        }
    }
}
