package com.leinao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
*
*
* created by wangshoufa
* 2018年9月28日 下午5:40:32
*
*/

@EnableEurekaClient
@EnableConfigServer
@SpringBootApplication
public class ConfigServer {
	
	public static void main(String[] args) {
		SpringApplication.run(ConfigServer.class, args);
	}

}
