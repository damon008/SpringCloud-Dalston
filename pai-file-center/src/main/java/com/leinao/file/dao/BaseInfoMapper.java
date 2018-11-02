package com.leinao.file.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description 基础信息操作类
 * @author lizi
 * @date 2018/10/16 上午10:09
 * @Version 1.0
 */
@Repository
public interface BaseInfoMapper {

	/**
	 * @Description 获取任务数
	 * @author lizi
	 * @date 2018/10/16 下午4:14
	 */
	List<Map<String,String>> getTaskNumOfCategory();

	/**
	 * @Description 获取GPU数
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	List<Map<String,Object>> getGPUNumOfCategory();

	/**
	 * @Description 获取活跃用户数
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	int getActiveUserNumber();

	/**
	 * @Description 获取非活跃用户数
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	int getNotActiveUserNumber();

	/**
	 * @Description 获取等待中的任务数
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	int getWaitingTaskNumber();

	/**
	 * @Description 获取运行中的任务数
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	int getRunningTaskNumber();

	/**
	 * @Description TODO
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	List<Map<String, String>> getDataResourceInfo();

	/**
	 * @Description 获取网络负载
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	List<Map<String, String>> getNetworkLoad();

	/**
	 * @Description TODO
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	List<Map<String,Integer>> getResourceUtilizationRatio();

	/**
	 * @Description 获取用户token有效时间
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	Timestamp getFailureTime(@Param("token") String token);

	/**
	 * @Description 获取用户token
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	String getUserId(@Param("token") String token);

	/**
	 * @Description 更新用户有效时间
	 * @author lizi
	 * @date 2018/10/16 下午4:15
	 */
	int updateFailureTime(@Param("token") String token, @Param("failureTime") Timestamp failureTime);
}
