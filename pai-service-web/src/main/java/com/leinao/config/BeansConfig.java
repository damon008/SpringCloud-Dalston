package com.leinao.config;

import javax.annotation.Resource;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;

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
	
	@Bean
    public IRule ribbonRule(){
        //return new RoundRobinRule();//轮询
        //return new RetryRule();//重试
		//return new RandomRule();//这里配置策略，和配置文件对应
		//return new WeightedResponseTimeRule();//这里配置策略，和配置文件对应
        return new BestAvailableRule();//选择一个最小的并发请求的server
        //return new MyProbabilityRandomRule();//自定义
    }

}
