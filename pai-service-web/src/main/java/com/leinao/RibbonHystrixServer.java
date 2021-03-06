package com.leinao;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * 
 * 实现客户端负载均衡
 * Hystrix依赖隔离、服务降级
 * @author wangshoufa
 * @date 2018年2月2日 下午7:13:36
 */
//@EnableCircuitBreaker
@EnableHystrix
@SpringBootApplication
@EnableDiscoveryClient
//@RibbonClient(name = "pai-business-web", configuration = com.leinao.config.BeansConfig.class)
public class RibbonHystrixServer {
	public static void main(String[] args) {
		new SpringApplicationBuilder(RibbonHystrixServer.class).web(true).run(args);
	}
}
