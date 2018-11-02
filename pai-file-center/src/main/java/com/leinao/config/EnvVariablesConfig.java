package com.leinao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 
 * 配置环境变量
 * @author  wangshoufa 
 * @date 2018年10月24日 上午9:10:05
 *
 */
@RefreshScope
@Component
public class EnvVariablesConfig {
	
	@Value("${vals}")
    private String content;

	public String getContent() {
		return content;
	}

}
