package com.leinao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志说明 注解
 * @author jianhu5
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OperationLog {
	/**
	 * 操作日志类型
	 * @return
	 */
	String type() ;
	/**
	 * 操作日志详细说明
	 * @return
	 */
	String value() ;
}
