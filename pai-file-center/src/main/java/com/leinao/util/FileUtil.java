package com.leinao.util;

import com.leinao.commons.Response;
import com.leinao.commons.ResponseMessage;
import com.leinao.constant.FileErrorEnum;
import com.leinao.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	// 格式化日期时间，记录日志时使用
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private FileService fileService;

	@Value("${baseDir}")
	private String basePath; // 基本路径

	/**
	 * @Description 获取根目录
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月30日 上午10:33:15
	 */
	public Response getBaseDir(String userId) {
		try {
			File file = new File(basePath + userId);
			if (!file.exists()) {
				file.mkdirs();
			}
			return Response.ok(basePath + userId);
		} catch (Exception e) {
			logger.error("getBaseDir failed");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}

	/**
	 * @Description 遍历目录下的文件
	 * @param dir
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月29日 上午11:56:52
	 */
	public static Response getFileList(String dir) {
		Map<String, Object> contentMap = new HashMap<String, Object>();
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		try {
			File file = new File(dir);
			// File file = new File(basePath + dir);
			for (File f : file.listFiles()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileName", f.getName());
				boolean isDirectory = f.isDirectory();
				long length = f.length();
				DecimalFormat df = new DecimalFormat("0.00");
				df.setRoundingMode(RoundingMode.DOWN);
				if (!isDirectory) {
					if(length>=1024*1024&&length<1024*1024*1024){
						map.put("size", df.format(length / 1024.0/1024.0)+"MB");
					}else if(length>=1024*1024*1024){
						map.put("size", df.format(length / 1024.0/1024.0/1024.0)+"G");
					}else{
						map.put("size", df.format(length / 1024.0)+"KB");
					}
				}
				map.put("isDirectory", isDirectory);
				map.put("date", dateFormat.format(new Date(f.lastModified())));
				fileList.add(map);
			}
			contentMap.put("totalCount", file.listFiles().length);

			// 文件列表按照上传时间排序
			Collections.sort(fileList, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					return o2.get("date").toString().compareTo(o1.get("date").toString());
				}
			});

			contentMap.put("fileList", fileList);
			return Response.ok(contentMap);
		} catch (Exception e) {
			logger.error("getFileList failed");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}

	/**
	 * @Description 新建文件夹
	 * @param dir
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月29日 上午11:56:52
	 */
	public static Response addDirectory(String dir) {
		try {
			File file = new File(dir);
			// File file = new File(basePath + dir);
			if (file.exists()) {
				return new Response(ResponseMessage.error(FileErrorEnum.FILE_EXIST.getCode(), FileErrorEnum.FILE_EXIST.getMessage()));
			}
			file.mkdirs();
			return Response.ok();
		} catch (Exception e) {
			logger.error("addDirectory failed");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}

	/**
	 * @Description 删除文件夹
	 * @param folderPath
	 * @author suqiliang
	 * @Date 2018年6月29日 下午2:33:20
	 */
	public Response deleteFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			// delAllFile(basePath + folderPath); // 删除完里面所有内容
			File myFilePath = new File(folderPath);
			// File myFilePath = new File(basePath + folderPath);
			myFilePath.delete(); // 删除空文件夹
			//删除记录同步到文件表里去
			deleteFileRecord(myFilePath);
			return Response.ok();
		} catch (Exception e) {
			logger.error("deleteFolder failed");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
    

	/**
	 * @Description 删除文件夹内所有文件
	 * @param path
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月29日 下午2:34:40
	 */
	public void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
				deleteFileRecord(temp);

			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
				deleteFolder(path + File.separator + tempList[i]);// 再删除空文件夹
			}
		}
	}
	
	/**
     * @Description 删除记录同步到文件表里去
     * @param myFilePath
     * @author cuidongdong
	 * @Date 2018年10月35日 10:36:21
     */
	private void deleteFileRecord(File myFilePath) {
		Map<String, Object> map = new HashMap<>();
		String path = myFilePath.toString().replace("\\", "/");
		fileService.deleteFolderFile(path);
	}

	/**
	 * @Description 文件重命名
	 * @param dir
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月29日 上午11:56:52
	 */
	public static Response renameFile(String dir, String newDir) {
		try {
			File file = new File(dir);
			// File file = new File(basePath + dir);
			if (!file.exists()) {
				return new Response(ResponseMessage.error(FileErrorEnum.FILE_NOT_EXIST.getCode(), FileErrorEnum.FILE_NOT_EXIST.getMessage()));
			}
			file.renameTo(new File(newDir));
			// file.renameTo(new File(basePath + newDir));
			return Response.ok();
		} catch (Exception e) {
			logger.error("renameFile failed");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}

	/**
	 * @Description 文件复制
	 * @param dir
	 * @return
	 * @author suqiliang
	 * @Date 2018年7月2日 上午9:44:49
	 */
	public Response copyFile(String dir) {
		try {
			File file = new File(dir);
			if (!file.exists()) {
				return new Response(ResponseMessage.error(FileErrorEnum.FILE_NOT_EXIST.getCode(), FileErrorEnum.FILE_NOT_EXIST.getMessage()));
			}
			// 复制文件
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.contains(".")) {
					// 这里用dir进行前缀截取，因为dir可能是多级目录下的某个文件，所以要把多级目录保留下来
					String prefix = dir.substring(0, dir.lastIndexOf("."));
					String postfix = fileName.substring(fileName.lastIndexOf("."));
					String copyFileName = prefix + "_副本";
					File copyFile = new File(copyFileName + postfix);
					if (!copyFile.exists()) {
						// 某文件不存在复制副本
						copyFiles(file, copyFile);
						saveFileRecord(dir, copyFile.toString().replace("\\", "/"));//复制文件记录同步到表里
						
					} else {
						// 某文件存在复制副本
						int n = 2;
						copyFileName = prefix + "_副本 (" + n + ")";
						copyFile = new File(copyFileName + postfix);
						while (copyFile.exists()) {
							n = n + 1;
							copyFileName = prefix + "_副本 (" + n + ")";
							copyFile = new File(copyFileName + postfix);
						}
						copyFiles(file, copyFile);
						saveFileRecord(dir, copyFile.toString().replace("\\", "/"));//复制文件记录同步到表里
					}
				} else {
					String copyFileName = dir + "_副本";
					File copyFile = new File(copyFileName);
					if (!copyFile.exists()) {
						// 某文件不存在复制副本
						copyFiles(file, copyFile);
						saveFileRecord(dir, copyFile.toString().replace("\\", "/"));//复制文件记录同步到表里
					} else {
						// 某文件存在复制副本
						int n = 2;
						copyFileName = dir + "_副本 (" + n + ")";
						copyFile = new File(copyFileName);
						while (copyFile.exists()) {
							n = n + 1;
							copyFileName = dir + "_副本 (" + n + ")";
							copyFile = new File(copyFileName);
						}
						copyFiles(file, copyFile);
						saveFileRecord(dir, copyFile.toString().replace("\\", "/"));//复制文件记录同步到表里
					}
				}
			} else if (file.isDirectory()) {
				// 复制目录
				// 目标文件夹
				String copyDirName = dir + "_副本";
				File copyDir = new File(copyDirName);
				if (!copyDir.exists()) {
					// 某目录不存在复制副本
					// 创建目标文件夹
					copyDir.setWritable(true, false);// 在linux下新建目录需要先获取权限
					copyDir.mkdirs();
					//添加要复制的最外层目录到数据库
					String copyFile=copyDir.toString().replace("\\", "/");
					saveFileRecord(dir, copyFile);//复制文件记录同步到表里
					saveDirectoryRecord(dir,copyFile);
					// 获取源文件夹当前下的文件或目录
					File[] files = (new File(dir)).listFiles();
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile()) {
							// 复制文件
							copyFiles(files[i], new File(copyDirName + File.separator + files[i].getName()));
						}
						if (files[i].isDirectory()) {
							// 复制目录
							String sourceDir = dir + File.separator + files[i].getName();
							String targetDir = copyDirName + File.separator + files[i].getName();
							copyDirectiory(sourceDir, targetDir);
						}
					}
				} else {
					// 某目录存在复制副本
					int n = 2;
					copyDirName = dir + "_副本 (" + n + ")";
					copyDir = new File(copyDirName);
					while (copyDir.exists()) {
						n = n + 1;
						copyDirName = dir + "_副本 (" + n + ")";
						copyDir = new File(copyDirName);
					}
					// 创建目标文件夹
					copyDir.setWritable(true, false);// 在linux下新建目录需要先获取权限
					copyDir.mkdirs();
					//添加要复制的最外层目录到数据库
					String copyFile=copyDir.toString().replace("\\", "/");
					saveFileRecord(dir, copyFile);//复制文件记录同步到表里
					saveDirectoryRecord(dir,copyFile);
					// 获取源文件夹当前下的文件或目录
					File[] files = (new File(dir)).listFiles();
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile()) {
							// 复制文件
							copyFiles(files[i], new File(copyDirName + File.separator + files[i].getName()));
						}
						if (files[i].isDirectory()) {
							// 复制目录
							String sourceDir = dir + File.separator + files[i].getName();
							String targetDir = copyDirName + File.separator + files[i].getName();
							copyDirectiory(sourceDir, targetDir);
						}
					}
				}
			}

			return Response.ok();
		} catch (Exception e) {
			logger.error("copyFile failed");
			logger.error(e.getMessage(), e);
			return Response.error();
		}
	}
    /**
     * 复制文件到表里
     * @param dir
     * @param copyFile
     */
	private void saveFileRecord(String dir, String copyFile) {
		try{//删除记录同步到文件表里去
			Map<String, Object> map = new HashMap<>();
			map.put("oldFile",dir);
			map.put("path",copyFile);
			fileService.insertCopyFiles(map);
		} catch(Exception e){
			logger.error("复制文件记录同步到表里出错");
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
     * @Description 复制文件夹里的文件记录到表里
     * @param dir
     * @param copyFile
     * @author cuidongdong
     * @Date 2018年10月11日  9:25:00
     */
	private void saveDirectoryRecord(String dir, String copyFile) {
		Map<String, Object> map = new HashMap<>();
		map.put("oldFile",dir);
		map.put("path",copyFile);
		fileService.copySunFiles(map);
	}

	/**
	 * @Description 文件下载
	 * @param filename
	 *            文件名
	 * @return
	 * @author suqiliang
	 * @Date 2018年6月30日 下午3:13:04
	 */
	public static byte[] load(String filename) {
		byte[] buffer = null;
		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(filename));
			// InputStream fis = new BufferedInputStream(new
			// FileInputStream(basePath + filename));
			buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
		} catch (Exception e) {
			logger.error("file load failed");
			logger.error(e.getMessage(), e);
		}
		return buffer;
	}

	// （目录下）文件复制 ==公共方法==
	public static void copyFiles(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);
		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);
		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();
		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	// 复制文件夹 ==公共方法==
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				copyFiles(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}
	
	/**
     * 计算文件大小文件大小
     * @param filePath 文件路径例如：E:\\imgData\\afr\\9211496189393485.jpg
     * @return    文件大小 byte
     */
	public static long getFileSize(String filePath){
        long fileSize=0l;
         FileChannel fc= null;
            try {  
                File f= new File(filePath);
                if (f.exists() && f.isFile()){  
                    FileInputStream fis= new FileInputStream(f);
                    fc= fis.getChannel();  
                    fileSize=fc.size();
                }else{  
                }  
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } finally {
                if (null!=fc){  
                    try{  
                        fc.close();  
                    }catch(IOException e){
                    }  
                }   
            } 
            
            return fileSize;
    }
}
