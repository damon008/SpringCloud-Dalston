package com.leinao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
*
*
* created by wangshoufa
* 2018年9月28日 下午6:00:07
*
*/


@RestController
@RequestMapping(value="")
@CrossOrigin
@RefreshScope
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${content}")
    String content;
	
	@RequestMapping(value="/")
	public String getInfo() {
		return content;
	}
	

}
