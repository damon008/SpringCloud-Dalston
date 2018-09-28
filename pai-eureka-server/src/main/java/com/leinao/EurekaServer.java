package com.leinao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
*
*
* created by wangshoufa
* 2018年9月28日 下午5:20:38
*
*/
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer {
	
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer.class, args);
	}

}
