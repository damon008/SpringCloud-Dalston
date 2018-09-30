package com.leinao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
*
*
* created by wangshoufa
* 2018年9月29日 上午9:44:06
*
*/
@RestController
public class CustomerController {
	
	Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
	private LoadBalancerClient loadBalancerClient;
    

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

}
