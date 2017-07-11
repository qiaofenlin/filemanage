package com.aicyber.filemanage.web;

import java.io.File;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aicyber.filemanage.file.LocalFileManage;
import com.aicyber.tools.JsonUtil;
import com.aicyber.tools.log.LogFactory;
import com.aicyber.tools.log.Logger;

@Controller
public class FileUploadController {
	private Logger logger = LogFactory.getLogger(this.getClass());

	@Autowired
	private LocalFileManage localFileManage;

	@Value("${web.system.sourceBaseUrl}")
	private String sourceBaseUrl;

	@ExceptionHandler({ Exception.class })
	@ResponseBody
	public ResponseEntity<String> exception(Exception e) {
		logger.error(e);
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", false);
		if (file.isEmpty()) {
			res.put("errmsg", "file is empty");
			return new ResponseEntity<String>(JsonUtil.toJSONString(res), HttpStatus.BAD_REQUEST);
		}
		File tempFile = null;
		try {
			String type = "";
			if (file.getOriginalFilename().lastIndexOf(".") > -1) {
				type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));// 取文件格式后缀名
			}
			tempFile = localFileManage.copyToTempDir(file.getInputStream());
			String md5 = localFileManage.MD5(tempFile);
			if (!localFileManage.isExists(md5)) {
				localFileManage.bacpup(tempFile, md5);
			}
			res.put("success", true);
			res.put("md5", md5);
			res.put("uri", new URL(sourceBaseUrl + "get/" + md5 + (StringUtils.isBlank(type) ? "" : type)).toString());
			return new ResponseEntity<String>(JsonUtil.toJSONString(res), HttpStatus.OK);
		} catch (Exception e) {
			res.put("errmsg", e.getMessage());
			return new ResponseEntity<String>(JsonUtil.toJSONString(res), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			FileUtils.deleteQuietly(tempFile);
		}
	}

	@RequestMapping(value = "get/{file:.*}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity getFile(@PathVariable String file) {
		try {
			String md5 = StringUtils.substringBeforeLast(file, ".");
			if (!localFileManage.isExists(md5)) {
				Map<String, Object> res = new HashMap<String, Object>();
				res.put("success", false);
				res.put("errmsg", "file is not exists!");
				return new ResponseEntity<String>(JsonUtil.toJSONString(res), HttpStatus.NOT_FOUND);
			}
			File targetFile = localFileManage.get(md5);
			FileNameMap fileNameMap = URLConnection.getFileNameMap();  
			String  contentType= fileNameMap.getContentTypeFor(file);
			HttpHeaders headers = new HttpHeaders();
			if(StringUtils.isNotBlank(contentType)){
				headers.setContentType(MediaType.valueOf(contentType));
			}
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(targetFile),headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("success", false);
			res.put("errmsg", e.getMessage());
			return new ResponseEntity<String>(JsonUtil.toJSONString(res), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
