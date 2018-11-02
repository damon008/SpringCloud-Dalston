package com.leinao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author  wangshoufa 
 * @date 2018年10月22日 下午4:31:14
 *
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = { "com.leinao" })
public class DataSetServer {
	
	public static void main(String[] args) {
		SpringApplication.run(DataSetServer.class, args);
	}

}
