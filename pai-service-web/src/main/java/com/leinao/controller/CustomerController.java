package com.leinao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
*
*
* created by wangshoufa
* 2018年9月29日 上午9:44:06
*
*/
@RestController
@CrossOrigin
public class CustomerController {
	
	Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Value("${content}")
	String content;
	
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
	private LoadBalancerClient loadBalancerClient;
    
    

    @ApiOperation(value="消费者", notes="消费者获取信息")
    //@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    //@ApiParam(value = "服务id", required = true) @RequestParam(required = true) String serverIds
    @HystrixCommand(fallbackMethod = "defaultStores")
    @GetMapping(value = "/hello")
    public String hello() {
    	ServiceInstance serviceInstance = this.loadBalancerClient.choose("pai-business-web");
    	log.info(serviceInstance.getServiceId());
    	log.info(serviceInstance.getHost());
    	log.info(serviceInstance.getPort()+"");
        return restTemplate.getForEntity("http://pai-business-web/", String.class).getBody();
    }

    public String defaultStores() {
        return "Ribbon + hystrix Dashboard ,提供者服务挂了";
    }
    
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
		log.info("provider services:" + content);
		return content;
	}

}
