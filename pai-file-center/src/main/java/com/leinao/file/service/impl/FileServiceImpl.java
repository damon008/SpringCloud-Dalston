package com.leinao.file.service.impl;

import com.leinao.commons.Response;
import com.leinao.commons.ResponseMessage;
import com.leinao.constant.FileErrorEnum;
import com.leinao.file.dao.BaseInfoMapper;
import com.leinao.file.dao.FileMapper;
import com.leinao.file.model.BigScreenUserFile;
import com.leinao.file.service.FileService;
import com.leinao.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {

	Logger logger = LoggerFactory.getLogger(getClass());

	//过滤文件命名的特殊字符
	private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

	private HashMap<String, File> directoryMap = new HashMap<String, File>();

	@Autowired
    private FileMapper fileMapper;

	@Autowired
	private BaseInfoMapper baseInfoMapper;

	@Autowired
	private FileUtil fileUtil;

	@Value("${baseDir}")
	private String basePath; // 基本路径
	@Value("${failureTime}")
	private long failureTime; // token失效时间

	@Override
	public Response getBaseDir(String token) {
		logger.debug("获取根目录...");
		if (getFailureTime(token)) {
			String userId = getUserId(token);
			return fileUtil.getBaseDir(userId);
		} else {
			return new Response(ResponseMessage.error(FileErrorEnum.TOKEN_ERROR.getCode(), FileErrorEnum.TOKEN_ERROR.getMessage()));
		}
	}

	@Override
	public Response getFileList(String token, String dir) {
		logger.debug("获取文件目录下的文件列表...");
		if (getFailureTime(token)) {
			// 遍历文件目录
			return FileUtil.getFileList(dir);
		} else {
			return new Response(ResponseMessage.error(FileErrorEnum.TOKEN_ERROR.getCode(), FileErrorEnum.TOKEN_ERROR.getMessage()));
		}
	}

	@Override
	public Response addDir(String token, String dir) {
		logger.debug("创建文件夹...");
		if (getFailureTime(token)) {
			Response result = FileUtil.addDirectory(dir);
			updateFailureTime(token);
			if (result.getMessage().getCode() == ResponseMessage.SUCCESS_CODE) {// 创建文件成功后保存到文件表里
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String userId = getUserId(token);// 获取userId的值
				String parentPath = dir.substring(0, dir.lastIndexOf("/"));
				String[] paths = dir.split("/");
				String isUserId = paths[paths.length - 2];
				long parentId = 0;
				String fileType = "1";// 1代表创建的是文件夹,2代表上传的是文件
				String fileSize = "0";
				if (!userId.equals(isUserId)) {
					paramMap.put("path", parentPath);
					parentId = getParentId(parentPath);
				}
				if (parentId == -1) {
					logger.info("获取父文件id出错");
				} else {
					Date date = new Date();
					putParamsForMap(dir, paramMap, parentId, fileType,
							fileSize, date, userId);
					saveFile(paramMap);
				}
			}
			return result;
		} else {
			return new Response(ResponseMessage.error(FileErrorEnum.TOKEN_ERROR.getCode(), FileErrorEnum.TOKEN_ERROR.getMessage()));
		}
	}

	@Override
	public Response deleteFile(String token, String dir) {
		logger.debug("删除文件...");
		if (getFailureTime(token)) {
			Response result = fileUtil.deleteFolder(dir);
			updateFailureTime(token);
			return result;
		} else {
			return new Response(ResponseMessage.error(FileErrorEnum.TOKEN_ERROR.getCode(), FileErrorEnum.TOKEN_ERROR.getMessage()));
		}
	}

	@Override
	public Response renameFile(String token, String dir, String newDir) {
		logger.debug("文件重命名...");
		if (getFailureTime(token)) {
			Response result = FileUtil.renameFile(dir, newDir);
			updateFailureTime(token);
			//重命名记录同步到文件表里
			if(result.getMessage().getCode() == ResponseMessage.SUCCESS_CODE){
				Map<String, Object> paramMap = new HashMap<String,Object>();
				List<String> renameFiles = queryPathByrenameFiles(dir + "/", dir);
				for (String file : renameFiles) {
					String suffixName = file.substring(dir.length(),file.length());
					String reName=newDir+suffixName;
					updateRenameFile(file,reName);
				}
			}
			return result;
		} else {
			return new Response(ResponseMessage.error(FileErrorEnum.TOKEN_ERROR.getCode(), FileErrorEnum.TOKEN_ERROR.getMessage()));
		}
	}

	@Override
	public Response copyFile(String token, String dir) {
		logger.debug("文件复制...");
		if (getFailureTime(token)) {
			Response result = fileUtil.copyFile(dir);
			updateFailureTime(token);
			return result;
		} else {
			return new Response(ResponseMessage.error(FileErrorEnum.TOKEN_ERROR.getCode(), FileErrorEnum.TOKEN_ERROR.getMessage()));
		}
	}

	@Override
	public boolean getFailureTime(String token) {
		logger.debug("获取失效时间...");
		Timestamp failureTime = baseInfoMapper.getFailureTime(token);
		if (null == failureTime || failureTime.getTime() < new Date().getTime()) {
			return false;
		}
		return true;
	}

	@Override
	public int updateFailureTime(String token) {
		logger.debug("更新失效时间...");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp failureTimestamp = Timestamp.valueOf(df.format(new Date().getTime() + failureTime));
		return baseInfoMapper.updateFailureTime(token, failureTimestamp);
	}

	@Override
	public int insertUserDownBandWidth(String token, BigDecimal bandWidth) {
		String userId = getUserId(token);
		logger.info("userId: " + userId);

		Map<String, Object> paramMap = new HashMap<>();
		logger.info("user use bandWidth: " + bandWidth);
		paramMap.put("bandWidth", bandWidth.divide((new BigDecimal(1024)), 4, BigDecimal.ROUND_HALF_UP));
		paramMap.put("userId", userId);
		long nowDate = System.currentTimeMillis();
		paramMap.put("createTime", nowDate);
		paramMap.put("updateTime", nowDate);
		try {
			fileMapper.insertUserDownBandWidth(paramMap);
			logger.info("insert file_center Down bandWidth success");
		} catch (Exception e) {
			logger.error("insert file_center Down bandWidth failed");
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public int insertUserJobBandwidth(String token, BigDecimal bandWidth) {
		String userId = getUserId(token);
		logger.info("userId: " + userId);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		logger.info("userId: " + userId);
		paramMap.put("bandWidth", bandWidth.divide((new BigDecimal(1024)), 4, BigDecimal.ROUND_HALF_UP));
		logger.info("user use bandWidth: " +bandWidth);
		long nowDate = System.currentTimeMillis();
		paramMap.put("createTime", nowDate);
		paramMap.put("updateTime", nowDate);
		try {
			fileMapper.insertUserJobBandwidth(paramMap);
			logger.info("insert file_center bandWidth success");
		} catch (Exception e) {
			logger.error("insert file_center bandWidth failed");
			logger.error(e.getMessage(), e);
			return 0;
		}
		return 1;
	}

	@Override
	public int insertUploadFile(String token, File dirPath) {
		Map<String, Object> paramMap = new HashMap<>();
		String dir = dirPath.toString();
		dir = dir.replace("\\", "/");
		String fileSize = "0";
		fileSize = getFileSize(dirPath);
		String userId = getUserId(token);// 获取userId的值
		String parentPath = dir.substring(0, dir.lastIndexOf("/"));
		String[] paths = dir.split("/");
		String isUserId = paths[paths.length - 2];
		Long parentId = 0L;
		String fileType = "2";// 1代表创建的是文件夹,2代表上传的是文件

		if (!userId.equals(isUserId)) {
			paramMap.put("path", parentPath);
			parentId = getParentId(parentPath);
		}
		Date date = new Date();
		putParamsForMap(dir, paramMap, parentId, fileType, fileSize, date,
				userId);
		Long id = queryId(paramMap.get("userId").toString(), paramMap.get("path").toString());
		if (id != null) {
			updateFileTime(paramMap);
			return 1;
		}

		saveFile(paramMap);
		return 1;
	}

	@Override
	public void deleteFolderFile(String path) {
		try {
			int status = fileMapper.deleteFolderFile(path);
			if(!(status==1)) {
				logger.info("沒有找到要刪除的记录:" + path);
			} else {
				logger.info("deleteFolderFile success");
			}
		} catch (Exception e) {
			logger.error("deleteFolderFile failed");
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void fileTask() {
		logger.info("定时任务开始了===============================");
		getFiles(basePath);
		Set<String> ids = directoryMap.keySet();
		for (String id : ids) {
			if (directoryMap.get(id).isDirectory()) {
				// 获取路径下的所有文件
				File[] files = directoryMap.get(id).listFiles();
				for (int i = 0; i < files.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String directoryPath = files[i].getPath().replace("\\", "/");
					if (files[i].isFile()) {

						map.put("oldFile", directoryPath);
						BigScreenUserFile userFile = fileMapper.getOldFile(directoryPath);
						if (userFile == null) {
							if(id.contains("-")){
								Long parentId=0L;
								saveScannerFile(id, map, directoryPath,parentId);
							}else{
								Long parentId=Long.valueOf(id);
								saveScannerFile(id, map, directoryPath,parentId);
							}
						}
					}
				}
			}
		}
		directoryMap.clear();
		logger.info("清除后的目录的个数有:" + directoryMap.size());
		logger.info("定时任务结束了==================================");
	}

	@Override
	public void insertCopyFiles(Map<String, Object> map) {
		try{
			BigScreenUserFile file = fileMapper.getOldFile(map.get("oldFile").toString());
			if(file!=null){
				map.put("fileSize",file.getFileSize());
				map.put("parentId",file.getParentId());
				map.put("fileType", file.getFileType());
				map.put("userId", file.getUserId());
				Date date = new Date();
				map.put("createDate",date);
				map.put("updateDate", date);
				saveFile(map);
			}else{
				logger.info("复制的文件在表里未找到记录:"+map.get("oldFile"));
			}
		}catch(Exception e){
			logger.info("insertCopyFiles faild!");
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void copySunFiles(Map<String, Object> hMap) {
		String dir = (String) hMap.get("oldFile");
		String copyFile = (String) hMap.get("path");
		List<BigScreenUserFile> list;
		list = (List<BigScreenUserFile>) fileMapper.selectCopySunFiles(dir + "/", dir);
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				BigScreenUserFile file1 = (BigScreenUserFile) o1;
				BigScreenUserFile file2 = (BigScreenUserFile) o2;
				if (file1.getParentId() > file2.getParentId()) {
					return 1;
				} else if (file1.getParentId() == file2.getParentId()) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		String[] path1 = dir.split("/");
		for (BigScreenUserFile userFile : list) {
			String path = userFile.getPath();
			if (!path.contains(copyFile) && !path.equals(dir)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String[] path2 = path.split("/");
				if (path2.length - path1.length == 0) {
					break;
				} else {
					String file = copyFile + StringUtils.remove(path, dir);
					String parentId = file.substring(0, file.lastIndexOf("/"));
					paramMap.put("path", parentId);
					Long id = fileMapper.getParentId(parentId);
					Date date = new Date();
					putParamsForMap(file, paramMap, id, userFile.getFileType(),
							userFile.getFileSize(), date, userFile.getUserId());
					saveFile(paramMap);
				}
			}
		}
	}

	/**
     * @Description 更新文件表里的字段path
     * @param file
     * @param reName
     * @author cuidongdong
	 * @Date 2018年9月28日 15:40:16
     */
	private void updateRenameFile(String file,String reName) {
		try {
			int status = fileMapper.updateRenameFile(reName, file);
			if(status==1){
				logger.info("updateRenameFile success!");
			}else{
				logger.info("重命名失败:"+file);
			}
		} catch (Exception e) {
			logger.error("updateRenameFile failed");
			logger.error(e.getMessage(), e);
		}
		
	}
    
	/**
     * @Description 查询出所有需要重命名在文件表的记录
	 * @return
	 * @author cuidongdong
	 * @Date 2018年9月28日 11:05:34
     */
	private List<String> queryPathByrenameFiles(String dirPath, String dir) {
		List<String> list = fileMapper.queryPathByRenameFiles(dirPath, dir);
		if(list.size()==0){
			logger.info("文件表里沒有查找需要重命名的文件:"+ dir);
		}
		return list;
		
	}

	/**
	 * @Description 获取用户ID
	 * @param token
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月29日 下午2:44:22
	 */

	private String getUserId(String token) {
		logger.debug("获取用户ID...");
		return baseInfoMapper.getUserId(token);
	}
	
	/**
	 * @Description 获取父级的id 
     * @author cuidongdong
     * @param path
     * @return
     * @Date 2018年9月27日 下午10:03:05
     * @author cuidongdong
     */
	private Long getParentId(String path) {
		try {
		Long parentId = fileMapper.getParentId(path);
		logger.info("getParentId  success");
		return parentId;
		} catch (Exception e) {
			logger.error("getParentId failed");
			logger.error(e.getMessage(), e);
			return -1L;
		}
	}
	
	/**
     * @Description 用户新建文件夹结构数据保存到表中
     * @author cuidongdong
     * @param map
     * @return
     * @Date 2018年9月27日 下午09:12:35
     */
	private Long saveFile(Map<String, Object> map) {
		long id=-1L;
		try {
		    id = (long) fileMapper.insertBigScreenUserFile(map);
			logger.info("insert big_screen_user_file  success");
		} catch (Exception e) {
			logger.error("insert big_screen_user_file failed");
			logger.error(e.getMessage(), e);
			return id;
		}
		return id;
	}

	
	/**
	 * @Description 更新已有文件的时间
	 * @param paramMap
	 * @Date 2018年9月27日 下午14:27:55
     * @author cuidongdong
	 */
	private void updateFileTime(Map<String, Object> paramMap) {
		fileMapper.updateFileTime(paramMap);
	}
    
	/**
	 * @Description 查询文件的id
	 * @Date 2018年9月27日 下午14:36:23
     * @author cuidongdong
	 */
	private Long queryId(String userId, String path) {
		return fileMapper.getFileId(userId, path);
	}

    /**
     * @Description  公共方法，传参保存文件夹下的文件
     * @param id
     * @param map
     * @param directoryPath
     * @param parentId
     * @author cuidongdong
     * @Date 2018年10月10日 下午09:23:26
     */
	private void saveScannerFile(String id, Map<String, Object> map, String directoryPath,Long parentId) {
        //	File file = directoryMap.get(id);
		String fileSize="0";
		fileSize = getFileSize(new File(directoryPath));
		String userId = directoryPath.substring(7, 40);
		Date lastModifiedDate = new Date(new File(directoryPath).lastModified());
		String fileType="2";
		putParamsForMap(directoryPath, map, parentId,
				fileType, fileSize, lastModifiedDate,
				userId);
		saveFile(map);
		logger.info("扫描出需要保存的文件:" + directoryPath);
	}
    
	
	
    /**
     * @Description  递归获取某路径下的所有文件夹
     * @param path
     * @Date 2018年10月8日 下午15:09:23
     * @author cuidongdong
     */
	private void getFiles(String path) {
        try{
			File file = new File(path);
			// 如果这个路径是文件夹
			if (file.isDirectory()) {
				// 获取路径下的所有文件
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					// 如果还是文件夹 递归获取里面的文件 文件夹
					if (files[i].isDirectory()) {
						String directoryPath = files[i].getPath()
								.replace("\\", "/");

						if (directoryPath.length() ==40){
							directoryMap.put("-" + UUID.randomUUID().toString().replaceAll("-", ""),
									new File(directoryPath));

						} else if (directoryPath.length() > 40) {

							Map<String, Object> map = new HashMap<String, Object>();
							map.put("oldFile", directoryPath);
							BigScreenUserFile userFile = fileMapper.getOldFile(directoryPath);
							if (userFile == null) {
								//保存用户userid下的一级文件夹
								if (getSubCount(directoryPath, "/") == 3) {
									Long parentId=0L;
									saveScannerDirectory(directoryPath, map,
											parentId);
								} else {
									//保存用户userid下除了一级的文件夹
									String parentPath = directoryPath.substring(0,directoryPath.lastIndexOf("/"));
									map.put("path", parentPath);
									Long parentId = getParentId(parentPath);
									saveScannerDirectory(directoryPath, map, parentId);

								}
							}
						}
						getFiles(directoryPath);
					}
				}
			}
        } catch(Exception e){
        	logger.error("loop failed###########################################");
			logger.error(e.getMessage(), e);
        }

	}

	/**
     * @Description  公共方法，传参保存文件夹到文件表
     * @param directoryPath
     * @param map
     * @param parentId
     * @Date 2018年10月10日 下午9:37:45
     * @author cuidongdong
     */
	private void saveScannerDirectory(String directoryPath, Map<String, Object> map, Long parentId) {
		String fileType="1";
		String fileSize="0";
		Date lastModifiedDate = new Date(new File(directoryPath).lastModified());
		String userId=directoryPath.substring(7, 40);
		putParamsForMap(directoryPath, map, parentId,
				fileType, fileSize, lastModifiedDate,
				userId);
		Long id = saveFile(map);
		if (id != -1) {
			directoryMap.put(String.valueOf(id),new File(directoryPath));
			logger.info("需要保存的文件夹:" + directoryPath);
		}
	}
    
	/**
     * @Description 公共方法向map中添加参数
     * @author cuidongdong
     * @param directoryPath
     * @param map
     * @param parentId
     * @param fileType
     * @param fileSize
     * @param lastModifiedDate
     * @param userId
     * @Date 2018年10月9日 下午18:00:19
     */
	private void putParamsForMap(String directoryPath,
			Map<String, Object> map, Long parentId, String fileType,
			String fileSize, Date lastModifiedDate, String userId) {
		map.put("path", directoryPath);
		map.put("parentId", parentId);
		map.put("fileType", fileType);
		map.put("userId",userId);
		map.put("fileSize", fileSize);
		map.put("createDate", lastModifiedDate);
		map.put("updateDate", lastModifiedDate);
	}

	/**
	 *  @Description 计算文件大小
	 * @author cuidongdong
	 * @param file
	 * @return
	 * @Date 2018年10月10日 下午9:09:19
	 */
	private String getFileSize(File file) {
		String fileSize;
		long length = file.length();
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.DOWN);
		if(length >= 1024*1024 && length < 1024 * 1024 * 1024) {
			fileSize = df.format(length / 1024.0/1024.0)+"MB";
		} else if(length >= 1024 * 1024 * 1024) {
			fileSize = df.format(length / 1024.0/1024.0/1024.0) + "G";
		} else {
			fileSize = df.format(length / 1024.0) + "KB";
		}
		return fileSize;
	}

	/**
     * @Description 计算key在str中有多少个
     * @param str
     * @param key
     * @return
     * @Date 2018年10月8日 下午15:03:33
     * @author cuidongdong
     */
	private static int getSubCount(String str, String key) {
		int count = 0;
		int index = 0;
		while ((index = str.indexOf(key, index)) != -1) {
			index = index + key.length();
			count++;
		}
		return count;
	}

	/**
	 * @Description 过滤文件名的特殊字符
	 * @param str
	 * @return
	 * @Date 2018年10月11日 下午11:18:56
     * @author cuidongdong
	 */
	private static String filenameFilter(String str) {
		return str == null ? null : FilePattern.matcher(str).replaceAll("");
	}
}
