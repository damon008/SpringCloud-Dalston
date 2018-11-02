package com.leinao.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 
 *filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
 *pre：可以在请求被路由之前调用
 *routing：在路由请求时候被调用
 *post：在routing和error过滤器之后被调用
 *error：处理请求时发生错误时被调用
 *filterOrder：通过int值来定义过滤器的执行顺序
 *shouldFilter：返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效。
 *run：过滤器的具体逻辑。需要注意，这里我们通过ctx.setSendZuulResponse(false)令zuul过滤该请求，不对其进行路由，然后通过ctx.setResponseStatusCode(401)设置了其返回的错误码，当然我们也可以进一步优化我们的返回，比如，通过ctx.setResponseBody(body)对返回body内容进行编辑等。
 *
 *@author wangshoufa
 *@date 2017年3月18日 下午7:56:26
 *
 */
@Component
public class AccessFilter extends ZuulFilter {
	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
	
	@Value("${pai.zuul.accessToken}")
	private String webAccessToken;
	
	/*
	* 定义过滤器类型，决定该规则处理请求的哪个生命周期执行，有以下四个阶段：
	* pre: 在请求被路由之前执行
	* routing : 在路由请求调用
	* post : 在route和err之后被调用
	* error：在处理请求发生错误时调用
	*/
    @Override
    public String filterType() {
        return "pre";
    }
    
    /**
    * 过滤器执行顺序,数字越小优先级越高
    */
    @Override
    public int filterOrder() {
        return 0;
    }
    
    @Override
    public boolean shouldFilter() {
    	return false;
    	/*return !RequestContext.getCurrentContext().getZuulResponseHeaders().isEmpty() ||
                RequestContext.getCurrentContext().getResponseDataStream() != null ||
                RequestContext.getCurrentContext().getResponseBody() != null;*/
    }
    
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("accessToken");
        if(accessToken == null) {
            log.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return ctx;
        }
        else if (accessToken instanceof String) {
			String token = (String) accessToken;
			if (token.equals(webAccessToken)) {
				log.info("access token ok");
				return null;
			}
			else {
				log.error("access token error");
	            ctx.setSendZuulResponse(false);
	            ctx.setResponseStatusCode(401);
	            return ctx;
			}
		}
        else {
        	log.error("access token is invalid");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return ctx;
		}
    }
}