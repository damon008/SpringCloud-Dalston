package com.leinao.file.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @Description 文件上传页面控制类
 * @author lizi
 * @date 2018/10/16 上午11:19
 * @Version 1.0
 */
@Controller
@CrossOrigin
public class PageController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/file")
    public String file() {
        return "file";
    }
}
