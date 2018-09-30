package com.leinao;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
*
*@author wangshoufa
*@date 2017年3月18日 下午6:31:08
*@SpringcloudApplication 等同于@SpringBootApplication、@EnableDiscoveryClient、@EnableCircuitBreaker
*
*/

@EnableZuulProxy
@SpringCloudApplication
public class ZuulServer {
	
	public static void main(String[] args) {
		SpringApplication.run(ZuulServer.class, args);
	}

}
