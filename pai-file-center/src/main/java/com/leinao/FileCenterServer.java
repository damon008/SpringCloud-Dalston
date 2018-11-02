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
@SpringBootApplication(scanBasePackages = { "com.leinao" })
public class FileCenterServer {
	
	public static void main(String[] args) {
		SpringApplication.run(FileCenterServer.class, args);
	}

}
