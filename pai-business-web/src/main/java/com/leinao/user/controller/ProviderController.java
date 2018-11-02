package com.leinao.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leinao.user.model.User;
import com.leinao.user.service.UserService;

/**
 * 服务提供者控制器
 * @author Mr.Null
 * @date 2018年2月1日 下午7:11:32
 */
@RestController
@CrossOrigin
public class ProviderController {
	Logger log  = LoggerFactory.getLogger(ProviderController.class);
	
	@Value("${content}")
    String content;
	
	@Autowired
	DiscoveryClient discoveryClient;
	
	@Autowired
	private UserService userService;
	
	
	@RefreshScope
	@GetMapping("/doGet")
	public String doGet() throws InterruptedException {
		//Thread.sleep(5000L);
		log.info("======properties:" + this.content);
		try {
			//Thread.currentThread().join();
		} catch (Exception e) {
			log.error(e.getMessage() , e);
		}
		String services = "====Services: " + discoveryClient.getServices();
		log.info("provider services:" + services);
		return services;
	}
	
	@GetMapping("/getUser")
	public List<User> getUser() {
		List<User> user = userService.getUser();
		int size = user.size();
		return user;
	}
	
	
	@GetMapping("/updateUser")
	public List<User> updateUser(@RequestParam(required = true) Long id,@RequestParam(required = true) String name) {
		userService.update(id, name);
		return null;
	}
	
	
	@GetMapping("/getInfo")
	public List<User> getInfo() {
		List<User> ll = userService.getInfo();
		return ll;
	}
	

}
