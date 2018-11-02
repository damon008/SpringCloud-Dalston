package com.leinao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;


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

public class UserController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${content}")
    String content;
	
	@RefreshScope
	@ApiOperation(value="消费者", notes="消费者获取信息")
    //@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    //@ApiParam(value = "服务id", required = true) @RequestParam(required = true) String serverIds
	@GetMapping(value="/")
	public String getInfo() {
		return content;
	}
	

}
