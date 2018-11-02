package com.leinao.interceptor;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leinao.annotation.OperationLog;
import com.leinao.annotation.PermissionAnnotationParse;
import com.leinao.commons.Response;

@Component
@Aspect
public class ControllerLoginInterceptor {
	
	
	private Logger logger= LoggerFactory.getLogger(ControllerLoginInterceptor.class);
	
	
	@Autowired
	private HttpServletRequest request;
	
	/*@Autowired
	private ServiceMonitorInfoService serviceMonitorInfoService;
	@Autowired
	private S3UserInfoMapper s3UserInfoMapper;*/
	

	
	 @Pointcut("execution(* com.iflytek.objectStorage..*.controller.*.*(..))")  
	 public void ServiceMethodPointcut(){}  
	 
	 
	
	   /**
    * aop中的环绕通知
    * 在这个方法中检查用户的权限和目标方法的需要的权限是否匹配
    * 如果匹配则调用目标方法，不匹配则不调用
    * @param joinPoint　连接点
	 * @return 
    * @throws Throwable
    */
   @SuppressWarnings("unchecked")
@Around("ServiceMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里  
   public Object isAccessMethod(ProceedingJoinPoint joinPoint) throws Throwable {
       /**
        * 1.获取访问目标方法应该具备的权限
        *  为解析目标方法的PrivilegeInfo注解，根据我们定义的解析器，需要得到：目标类的class形式　方法的名称
        */
       Class targetClass = joinPoint.getTarget().getClass();
       String methodName = joinPoint.getSignature().getName();
       
       Object[] args = joinPoint.getArgs();
       
       
       Method method = this.getMethod(joinPoint);
       //得到该方法的访问权限
       boolean methodAccess = PermissionAnnotationParse.parse(method);
       /*
        * 2.遍历用户的权限，看是否拥有目标方法对应的权限
        */
       if(methodAccess) {
    	   

      		/*String header = request.getHeader("X-PROJECT-ID");
      		String project = null;
      		try {
      			project = java.net.URLDecoder.decode(header,"utf-8");
      			
      			String userHeader = request.getHeader("X-AUTH-ID");
    			String userString = java.net.URLDecoder.decode(userHeader,"utf-8");
    			SysUserVO sysUser = JSON.parseObject(userString, SysUserVO.class);
    			HttpSession session = request.getSession();
    			session.setAttribute("sysUser", sysUser);
    			 MDC.put("userName", sysUser.getUserName());
      		} catch (UnsupportedEncodingException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
      		List<ProjectInfo> projectList = (List<ProjectInfo>) JSON.parseArray(project, ProjectInfo.class);
      		String projectId="";
      		for (ProjectInfo projectInfo : projectList) {
      			if(projectInfo.isChecked()) {
      				projectId=projectInfo.getId().toString();
      			}
      		}
      		
      	   
            MDC.put("projectId", projectId);

    	   
      		S3UserInfo s3UserInfo = s3UserInfoMapper.queryS3UserInfoByUid(projectId);
    	    request.getSession().setAttribute("s3UserInfo",s3UserInfo);*/
	   	  
       }
       
       
       	/*Date startTime = new Date();
		Response<Object> returnMsg = new Response<Object>();
		// 入参日志记录 TODO
		String parmAbbr = "IN PARAM-------" + methodName;
		try {
			for (Object parm : args) {
				if (null != parm) {
					parmAbbr = ":" + parm.getClass().getName() + "=" + JSON.toJSONString(parm);
				} else {
					parmAbbr = ":参数NULL";
				}
			}
			logger.debug(parmAbbr);
		} catch (Exception e) {

			logger.debug(parmAbbr + e.getMessage());
		}
		
		Object proceed = null;
		try {
			returnMsg = (Response<Object>) joinPoint.proceed(args);//调用目标方法
			logger.debug("OUT PARAM------" + methodName + ":" + proceed);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// service层面的操作日志记录
		//insertOperationLog(joinPoint, returnMsg, startTime);
     
      
	//return returnMsg;
      return null;
       
   }
   
	private void insertOperationLog(ProceedingJoinPoint pjp, Response<Object> returnMsg, Date startTime) {
		try {
			Method method = getMethod(pjp);
			OperationLog operationLog = method.getAnnotation(OperationLog.class);
			if (null == operationLog) {
				return;
			}
			/*ServiceMonitorInfo smi = new ServiceMonitorInfo();
			smi.setStartTime(startTime);
			smi.setEndTime(new Date());
			S3UserInfo s3UserInfo  = (S3UserInfo) request.getSession().getAttribute("s3UserInfo");
			
			SysUserVO user = (SysUserVO) request.getSession().getAttribute("sysUser");
			if (null != s3UserInfo) {

				smi.setProjectId(Long.valueOf(s3UserInfo.getUserId()));
			}
			if(null!=user) {
				smi.setOperator(user.getUserName());
			}
			if (null == returnMsg || null == returnMsg.getMessage()) {
				smi.setStatus("未知");
			} else if (0 == returnMsg.getMessage().getCode()) {
				smi.setStatus("成功");
			} else {
				smi.setStatus("失败");
			}
			smi.setTaskName(operationLog.value());
			smi.setType(operationLog.type());*/
			// service 层面的日志入库（暂时实时入库，后期改为定时批量入库 减少数据库连接池占用）
			//serviceMonitorInfoService.saveOperationLog(smi);
		} catch (Exception es) {
			es.printStackTrace();
		}
	}
	
	
	 private Method getMethod(ProceedingJoinPoint joinPoint){
	        Object target = joinPoint.getTarget();
	        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
	        try {
	            method = target.getClass().getMethod(method.getName(), method.getParameterTypes());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return method;
	    }


}
