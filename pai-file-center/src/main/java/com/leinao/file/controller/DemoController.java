package com.leinao.file.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leinao.annotation.OperationLog;
import com.leinao.config.EnvVariablesConfig;
import com.leinao.task.TaskService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Demo使用样例
 * @author Mr.Null
 * @date 2018年2月1日 下午7:11:32
 */
@RestController
@CrossOrigin
public class DemoController {
	Logger log  = LoggerFactory.getLogger(DemoController.class);
	
	@Autowired
	EnvVariablesConfig envConfig;
	
	@Autowired
	DiscoveryClient discoveryClient;
	
	@Autowired
    private TaskService taskService;
	
	@GetMapping("/doGet")
	@ApiOperation(value="消费者", notes="消费者获取信息")
    //@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    //@ApiParam(value = "服务id", required = true) @RequestParam(required = true) String serverIds
	@OperationLog(type="消费者",value="查询配置信息")
	public String doGet() throws InterruptedException {
		//Thread.sleep(5000L);
		log.info("======properties:" + envConfig.getContent());
		try {
			//Thread.currentThread().join();
			long start = System.currentTimeMillis();
			taskService.doTaskOne(start);
		} catch (Exception e) {
			log.error(e.getMessage() , e);
		}
		String services = "====Services: " + discoveryClient.getServices();
		log.info("provider services:" + services);
		return envConfig.getContent();
	}
	
	@PostMapping("/doPost")
	@ApiOperation(value="消费者", notes="消费者获取信息")
	public String doPost(@ApiParam(value = "服务id", required = true) @RequestParam(required = true) String serverIds) throws InterruptedException {
		//Thread.sleep(5000L);
		log.info("======properties:" + envConfig.getContent());
		try {
			String services = "====Services: " + discoveryClient.getServices();
			log.info("provider services:" + services);
		} catch (Exception e) {
			log.error(e.getMessage() , e);
		}
		return envConfig.getContent();
	}
	
}
