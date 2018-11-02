package com.leinao.file.service;

import com.leinao.commons.Response;

/**
 *
 * @Description 基础服务处理
 * @author lizi
 * @date 2018/10/16 上午10:37
 * @Version 1.0
 */
public interface BaseInfoService {

	/**
	 * @Description 获取GPU类型分布数
	 * @return
	 * @author  suqiliang
	 * @Date    2018年6月11日  下午2:44:22
	 */
	 Response getGPUNumOfCategory();

	/**
	 * @Description 获取活跃用户比例数
	 * @return
	 * @author  suqiliang
	 * @Date    2018年6月11日  下午2:43:23
	 */
	 Response getActiveUserRatio();

	/**
	 * @Description 获取作业类型统计数
	 * @return
	 * @author  suqiliang
	 * @Date    2018年6月11日  下午2:42:43
	 */
	 Response getTaskNumOfCategory();
	
	/**
	 * @Description 获取作业执行比例（排队  或 运行）
	 * @return
	 * @author  suqiliang
	 * @Date    2018年6月11日  下午2:42:25
	 */
	 Response getTaskExecutionRatio();

	/**
	 * @Description 获取各时段平均资源利用率
	 * @return
	 * @author  suqiliang
	 * @Date    2018年6月11日  下午2:42:09
	 */
	 Response getResourceUtilizationRatio();

	/**
	 * @Description 获取数据资源信息
	 * @return
	 * @author  suqiliang
	 * @Date    2018年6月11日  下午2:41:47
	 */
	 Response getDataResourceInfo();

	/**
	 * @Description 获取网络运行负载情况
	 * @return
	 * @author  suqiliang
	 * @Date    2018年6月11日  下午2:41:20
	 */
	 Response getNetworkLoad();
	
}

