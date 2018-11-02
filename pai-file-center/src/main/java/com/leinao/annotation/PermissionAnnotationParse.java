package com.leinao.annotation;

import java.lang.reflect.Method;
/**
 * 
 *  Class Name: PermissionAnnotationParse.java
 *  Function:
 *  
 *     Modifications:   
 *  
 *  @author lxy  DateTime 2018年1月4日 上午11:12:38    
 *  @version 1.0
 */
public class PermissionAnnotationParse {
	/**
     * 解析注解
     * @param targetClass　目标类的class形式
     * @param methodName　在客户端调用哪个方法,methodName就代表哪个方法　
     * @return
     * @throws Exception 
     */
    public static boolean parse( Method method) throws Exception {
        boolean methodAccess = false;
       
        //判断方法上是否有Privilege注解
        if (method.isAnnotationPresent(Permission.class)) {
            //得到方法上的注解
        	Permission permission = method.getAnnotation(Permission.class);
            methodAccess = permission.loginReqired();
        }
        return methodAccess;
    }
}
