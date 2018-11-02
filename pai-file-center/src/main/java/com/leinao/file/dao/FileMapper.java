package com.leinao.file.dao;


import com.leinao.file.model.BigScreenUserFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description 文件处理中心
 * @author lizi
 * @date 2018/10/16 上午10:48
 * @Version 1.0
 */
@Repository
public interface FileMapper {
	/**
	 * @Description 插入用户任务下载文件的带宽
	 * @param map : 参数
	 * @author sofa
	 * @date 2018/10/16 下午4:03
	 */
	void insertUserJobBandwidth(Map<String, Object> map);

	/**
	 * @Description 插入用户下载文件的带宽
	 * @param map : 参数
	 * @author sofa
	 * @date 2018/10/16 下午4:03
	 */
	void insertUserDownBandWidth(Map<String, Object> map);

	/**
	 * @Description 获取用户创建文件的父id
	 * @param path : 路径
	 * @author lizi
	 * @date 2018/10/16 上午10:16
	 */
	Long getParentId(@Param("path") String path);

	/**
	 * @Description 新建或者上传文件插入数据到表里
	 * @param map :
	 * @author cuidongdong
	 * @date 2018/10/16 上午10:18
	 */
	int insertBigScreenUserFile(Map map);

	/**
	 * @Description 根据path和user_id查询文件在表里的id
	 * @param userId : 用户ID
	 * @param path : 路径
	 * @author lizi
	 * @date 2018/10/16 上午10:19
	 */
	Long getFileId(@Param("userId") String userId, @Param("path") String path);

	/**
	 * @Description 更新已有文件的时间
	 * @param map :
	 * @author lizi
	 * @date 2018/10/16 上午10:19
	 */
	void updateFileTime(Map map);

	/**
	 * @Description 根据字段path	删除文件表里的记录
	 * @param path : 目录
	 * @author cuidongdong
	 * @date 2018/10/16 上午10:20
	 */
	int deleteFolderFile(@Param("path") String path);

	/**
	 * @Description 重命名的时候查询表里需要同步重命名的所有记录的路径名
	 * @author cuidongdong
	 * @date 2018/10/16 上午10:20
	 */
	List<String> queryPathByRenameFiles(@Param("dirPath") String dirPath, @Param("dir") String dir);

	/**
	 * @Description 根据字段path更新字段
	 * @param reName : 更新后的名字
	 * @param file : 原路径名称
	 * @author lizi
	 * @date 2018/10/16 上午10:21
	 */
	int updateRenameFile(@Param("reName") String reName, @Param("file") String file);

	/**
	 * @Description 根据path查询记录的相关字段
	 * @param oldFile : 路径
	 * @author cuidongdong
	 * @date 2018/10/16 上午10:24
	 */
	BigScreenUserFile getOldFile(@Param("oldFile") String oldFile);

	/**
	 * @Description 查询要复制文件下的所有文件字段
	 * @param path : 路径
	 * @param dir : 文件夹
	 * @author cuidongdong
	 * @date 2018/10/16 上午10:26
	 */
	List<BigScreenUserFile> selectCopySunFiles(@Param("path") String path, @Param("dir") String dir);
}
