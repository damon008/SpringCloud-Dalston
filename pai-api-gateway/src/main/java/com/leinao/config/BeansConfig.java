package com.leinao.config;

import javax.annotation.Resource;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mr.Null
 * @date 2018年2月2日 下午7:15:53
 */
@Configuration
public class BeansConfig {
	
	@Resource
	private Environment environment;
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setReadTimeout(environment.getProperty("client.http.request.readTimeout", Integer.class, 15000));
		requestFactory.setConnectTimeout(environment.getProperty("client.http.request.connectTimeout", Integer.class, 3000));
		RestTemplate rt = new RestTemplate(requestFactory);
		return rt;
	}

}
