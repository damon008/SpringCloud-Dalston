package com.leinao.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "defaultStores")
    @GetMapping(value = "/hello")
    public String hello() {
        return restTemplate.getForEntity("http://pai-business-web/", String.class).getBody();
    }

    public String defaultStores() {
        return "Ribbon + hystrix Dashboard ,提供者服务挂了";
    }

}
