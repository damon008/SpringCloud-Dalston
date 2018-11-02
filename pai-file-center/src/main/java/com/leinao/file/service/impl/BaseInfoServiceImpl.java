package com.leinao.file.service.impl;

import com.leinao.commons.Response;
import com.leinao.file.dao.BaseInfoMapper;
import com.leinao.file.model.EchartBarData;
import com.leinao.file.model.Series;
import com.leinao.file.service.BaseInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseInfoServiceImpl implements BaseInfoService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BaseInfoMapper baseInfoMapper;
	
	/**
	 * <p>Title: getGPUNumOfCategory</p>
	 * @Description 获取GPU类型分布数
	 * @return
	 * @Version 1.0
	 */
	@Override
	public Response getGPUNumOfCategory() {
		try {
			List<Map<String,Object>> GPUNumOfCategory = baseInfoMapper.getGPUNumOfCategory();
			EchartBarData bar = new EchartBarData();
			
			List<String> legend = new ArrayList<String>();
			legend.add("平台GPU类型分布");//图例
			bar.setLegend(legend);
			
			List<String> category = new ArrayList<String>();//纵坐标
			Map<String, Object> gpu = null;
			
			List<Series> seriesList = new ArrayList<Series>();
			Series series = new Series();
			series.setName("GPU统计");
			series.setType("bar");
			List<Integer> data = new ArrayList<Integer>();
			
			for(int i=0;i<GPUNumOfCategory.size();i++){
				gpu= GPUNumOfCategory.get(i);
				category.add((String) gpu.get("category"));
				long numberL = (Long) gpu.get("number");
				int numberI = (int) numberL;
				data.add(numberI);
			}
			bar.setCategory(category);
			series.setData(data);
			seriesList.add(series);
			bar.setSeries(seriesList);
			return Response.ok(bar);
		} catch (Exception e) {
			logger.error("getGPUNumOfCategory error");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
	
	/**
	 * <p>Title: getActiveUserRatio</p>
	 * @Description 获取活跃用户比例数
	 * @return
	 * @Version 1.0
	 */
	@Override
	public Response getActiveUserRatio() {
		try {
			List<Map<String,String>> activeUserRatio = new ArrayList<Map<String,String>>();
			Map<String,String> activeUserMap = new HashMap<String,String>();
			activeUserMap.put("name", "活跃用户");
			activeUserMap.put("value", Integer.valueOf(baseInfoMapper.getActiveUserNumber()).toString());
			activeUserRatio.add(activeUserMap);
			
			Map<String,String> notActiveUserMap = new HashMap<String,String>();
			notActiveUserMap.put("name", "非活跃用户");
			notActiveUserMap.put("value", Integer.valueOf(baseInfoMapper.getNotActiveUserNumber()).toString());
			activeUserRatio.add(notActiveUserMap);

			return Response.ok(activeUserRatio);
		} catch (Exception e) {
			logger.error("getActiveUserRatio error");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}

	/**
	 * <p>Title: getTaskNumOfCategory</p>
	 * @Description 获取作业类型统计数
	 * @return
	 * @Version 1.0
	 */
	@Override
	public Response getTaskNumOfCategory() {
		try {
			List<Map<String,String>> taskNumOfCategory = baseInfoMapper.getTaskNumOfCategory();
			return Response.ok(taskNumOfCategory);
		} catch (Exception e) {
			logger.error("getTaskNumOfCategory error");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
	
	/**
	 * <p>Title: getTaskExecutionRatio</p>
	 * @Description 获取作业执行比例（排队  或 运行）
	 * @return
	 * @Version 1.0
	 */
	@Override
	public Response getTaskExecutionRatio() {
		try {
			List<Map<String,String>> taskExecutionRatio = new ArrayList<Map<String,String>>();
			Map<String,String> waitingTask = new HashMap<String,String>();
			waitingTask.put("name", "正在排队的作业");
			waitingTask.put("value", Integer.valueOf(baseInfoMapper.getWaitingTaskNumber()).toString());
			taskExecutionRatio.add(waitingTask);
			
			Map<String,String> runningTask = new HashMap<String,String>();
			runningTask.put("name", "正在运行的作业");
			runningTask.put("value", Integer.valueOf(baseInfoMapper.getRunningTaskNumber()).toString());
			taskExecutionRatio.add(runningTask);
			
			return Response.ok(taskExecutionRatio);
		} catch (Exception e) {
			logger.error("getTaskExecutionRatio error");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
	
	/**
	 * <p>Title: getResourceUtilizationRatio</p>
	 * @Description 获取各时段平均资源利用率
	 * @return
	 * @Version 1.0
	 */
	@Override
	public Response getResourceUtilizationRatio() {
		try {
			List<Map<String,Integer>> resourceUtilizationRatio = baseInfoMapper.getResourceUtilizationRatio();
			EchartBarData bar = new EchartBarData();
			
			List<String> legend = new ArrayList<String>();
			legend.add("各时段平均资源利用率");//图例
			bar.setLegend(legend);
			
			List<String> time = new ArrayList<String>();//横坐标
			Map<String, Integer> timeMap = null;
			
			List<Series> seriesList = new ArrayList<Series>();
			Series series = new Series();
			series.setName("资源利用率");
			series.setType("bar");
			List<Integer> data = new ArrayList<Integer>();
			
			for(int i=0;i<resourceUtilizationRatio.size();i++){
				timeMap= resourceUtilizationRatio.get(i);
				time.add(timeMap.get("time")+"");
				data.add(timeMap.get("ratio"));
			}
			bar.setCategory(time);
			series.setData(data);
			seriesList.add(series);
			bar.setSeries(seriesList);
			return Response.ok(bar);
		} catch (Exception e) {
			logger.error("getResourceUtilizationRatio error");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
	
	/**
	 * <p>Title: getDataResourceInfo</p>
	 * @Description 获取数据资源信息
	 * @return
	 * @Version 1.0
	 */
	@Override
	public Response getDataResourceInfo() {
		try {
			List<Map<String,String>> dataResource = baseInfoMapper.getDataResourceInfo();
			return Response.ok(dataResource);
		} catch (Exception e) {
			logger.error("getDataResourceInfo error");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
	
	/**
	 * <p>Title: getNetworkLoad</p>
	 * @Description 获取网络运行负载情况
	 * @return
	 * @Version 1.0
	 */
	@Override
	public Response getNetworkLoad() {
		try {
			List<Map<String,String>> networkLoad = baseInfoMapper.getNetworkLoad();
			return Response.ok(networkLoad);
		} catch (Exception e) {
			logger.error("getNetworkLoad error");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
}
