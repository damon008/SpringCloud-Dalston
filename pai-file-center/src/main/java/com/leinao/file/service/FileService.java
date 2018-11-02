package com.leinao.file.service;

import com.leinao.commons.Response;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @Description 文件处理中心
 * @author lizi
 * @date 2018/10/16 上午10:36
 * @Version 1.0
 */
public interface FileService {

	/**
	 * @Description 获取根目录
	 * @param token 用户Token
	 * @return
	 * @author suqiliang
	 * @Date 2018年7月2日 上午11:28:24
	 */
	 Response getBaseDir(String token);

	/**
	 * @Description 获取文件列表
	 * @param token 用户Token
	 * @param dir 目录
	 * @return
	 * @author suqiliang
	 * @Date 2018年7月2日 上午11:28:35
	 */
	 Response getFileList(String token, String dir);

	/**
	 * @Description 新建文件夹
	 * @param token 用户Token
	 * @param dir 目录
	 * @return
	 * @author suqiliang
	 * @Date 2018年7月2日 上午11:28:49
	 */
	 Response addDir(String token, String dir);

	/**
	 * @Description 删除文件
	 * @param token 用户Token
	 * @param dir 目录
	 * @return
	 * @author suqiliang
	 * @Date 2018年7月2日 上午11:28:58
	 */
	 Response deleteFile(String token, String dir);

	/**
	 * @Description 文件重命名
	 * @param token 用户Token
	 * @param dir 旧目录名称
	 * @param newDir 新目录名称
	 * @return
	 * @author suqiliang
	 * @Date 2018年7月2日 上午11:29:05
	 */
	 Response renameFile(String token, String dir, String newDir);

	/**
	 * @Description 文件复制
	 * @param token 用户Token
	 * @param dir 旧目录名称
	 * @return
	 * @author  suqiliang
	 * @Date    2018年7月2日  上午11:31:22
	 */
	 Response copyFile(String token, String dir);
	
	/**
	 * @Description 判断token是否失效
	 * @param token 用户Token
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月29日 下午2:44:22
	 */
	 boolean getFailureTime(String token);

	/**
	 * @Description 更新失效时间
	 * @param token 用户Token
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月29日 下午2:43:23
	 */
	 int updateFailureTime(String token);

	 /**
	  * @Description 插入用户下载文件的带宽
	  * @param token : 用户Token
	  * @param bandWidth : 带宽
	  * @author sofa
	  * @date 2018/10/16 下午3:49
	  */
	 int insertUserDownBandWidth(String token, BigDecimal bandWidth);

	/**
	 * @Description 插入用户任务下载文件的带宽
	 * @param token : 用户Token
	 * @param bandWidth : 带宽
	 * @author sofa
	 * @date 2018/10/16 下午3:49
	 */
	 int insertUserJobBandwidth(String token, BigDecimal bandWidth);
	
	 /**
	  * @Description 用户上传文件的记录保存到表中
	  * @param token : 用户Token
	  * @param dirPath : 目录
	  * @author cuidongdong
	  * @date 2018/10/16 下午3:53
	  */
	 int insertUploadFile(String token, File dirPath);
    
	/**
	 * @Description 删除文件夹记录同步到文件表里
	 * @param path : 路径
	 * @author cuidongdong
	 * @Date 2018年10月10日 18:23:00
	 */
	 void deleteFolderFile(String path);

	/**
	 * @Description 定时任务扫面未同步到big_screen_user_file表里的文件以及文件夹
	 * @author cuidongdong
	 * @Date 2018年10月8日 11:28:00
	 */
	  void fileTask();
	
	/**
	 * @Description 根据oldFile复制新的文件copyFile到文件表里
	 * @param map
	 * @author cuidongdong
	 * @Date 2018年10月10日 18:22:00
	 */
	 void insertCopyFiles(Map<String, Object> map);
	
	/**
	 * @Description 复制文件夹下的所有文件
	 * @param paramMap
	 * @author cuidongdong
	 * @Date 2018年10月10日 21:56:00
	 */
	 void copySunFiles(Map<String, Object> paramMap);
}
