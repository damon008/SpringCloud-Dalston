package com.leinao;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author  wangshoufa 
 * @date 2018年10月9日 上午9:58:21
 *
 */

@EnableHystrixDashboard
@SpringCloudApplication
public class HystrixDashboardServer {
	public static void main(String[] args) {
		SpringApplication.run(HystrixDashboardServer.class, args);
	}
}
