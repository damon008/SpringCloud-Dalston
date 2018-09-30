package com.leinao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
*
*
* created by wangshoufa
* 2018年9月28日 下午5:56:41
*
*/
@EnableEurekaClient
@SpringBootApplication
public class ProviderServer {
	
	public static void main(String[] args) {
		SpringApplication.run(ProviderServer.class, args);
	}

}
