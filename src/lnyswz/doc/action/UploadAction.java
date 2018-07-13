package lnyswz.doc.action;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Img;
import lnyswz.doc.util.Constant;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author 王文阳
 *
 */
@Namespace("/doc")
@Action("uploadAction")
public class UploadAction extends BaseAction{
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	private String dirName;

	public void upload() throws Exception {
		Json j = new Json();

		//定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");

		//最大文件大小
		long maxSize = 50000000;

		//文件保存目录路径
		String savePath = ServletActionContext.getServletContext().getRealPath(Constant.UPLOADFILE_PATH);
        //文件保存目录URL
        //String saveUrl  = ServletActionContext.getRequest().getContextPath() + Constant.UPLOADFILE_PATH;
        String saveUrl  = Constant.UPLOADFILE_PATH;

		//检查目录
		File uploadDir = new File(savePath);
		if(!uploadDir.exists()) {
			uploadDir.mkdir();
		}

		//检查目录写权限
		if(!uploadDir.canWrite()){
			j.setMsg("上传目录没有写权限！");
			writeJson(j);
			return;
		}

		//验证文件大小及格式
		if (maxSize < upload.length()) {
			j.setMsg("上传文件大小超过50M限制！");
			writeJson(j);
			return;
		}


		//检查扩展名
		String fileExt = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1).toLowerCase();
		if(!Arrays.<String>asList(extMap.get("image").split(",")).contains(fileExt)){
			j.setMsg("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get("image") + "格式！");
			writeJson(j);
			return;
		}

		//SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		//String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;

		//上传方式
        //FileUtils.copyFile(upload, new File(savePath + dirName + "\\" + newFileName));
        FileUtils.copyFile(upload, new File(savePath + dirName + "\\" + uploadFileName));
		//saveUrl += dirName + newFileName;
		saveUrl += dirName + uploadFileName;
        //删除临时文件
        upload.delete();

        Img img = new Img();
        img.setFilePath(saveUrl);
		j.setSuccess(true);
		j.setMsg("文件上传成功！");
		j.setObj(img);
		writeJson(j);
	}

	public static boolean deleteFile(String sPath) {
		File file = new File(UploadAction.getRootPath() + sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}

	public static boolean moveFile(String oldFilePath, String newFilePath){
		String rootPath = UploadAction.getRootPath();
		try {
			FileUtils.moveFile(new File(rootPath + "/" + oldFilePath), new File(rootPath + "/" + newFilePath));
		}catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean deleteDir(String path){
		String rootPath = UploadAction.getRootPath();
		try {
			FileUtils.deleteDirectory(new File(rootPath + path));
		}catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static String getRootPath() {
		// 因为类名为"Util"，因此" Util.class"一定能找到
		String result = UploadAction.class.getResource("UploadAction.class").toString();
		int index = result.indexOf("WEB-INF");
		if (index == -1) {
			index = result.indexOf("bin");
		}
		result = result.substring(0, index);
		if (result.startsWith("jar")) {
			// 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径
			result = result.substring(10);
		} else if (result.startsWith("file")) {
			// 当class文件在class文件中时，返回"file:/F:/ ..."样的路径
			result = result.substring(6);
		}
		if (result.endsWith("/"))
			result = result.substring(0, result.length() - 1);// 不包含最后的"/"
		return result;
	}

	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

}
