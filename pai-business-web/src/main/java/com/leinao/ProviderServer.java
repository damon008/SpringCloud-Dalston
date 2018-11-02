package com.leinao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
*
*
* created by wangshoufa
* 2018年9月28日 下午5:56:41
*
*/
@EnableEurekaClient
//(scanBasePackages = { "com.leinao" })
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class ProviderServer {
	
	public static void main(String[] args) {
		SpringApplication.run(ProviderServer.class, args);
	}

}
