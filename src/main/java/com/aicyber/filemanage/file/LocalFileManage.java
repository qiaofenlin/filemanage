package com.aicyber.filemanage.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aicyber.tools.MD5Util;
import com.aicyber.tools.StringUtil;

@Component
public class LocalFileManage {

	@Value("${file.system.tempPath}")
	private String tempPath;
	@Value("${file.system.backupPath}")
	private String backupPath;

	public File copyToTempDir(InputStream in) throws IOException {
		String gainUUID = StringUtil.gainUUID();
		File targerFile = new File(tempPath, gainUUID);
		FileUtils.copyInputStreamToFile(in, targerFile);
		return targerFile;
	}

	public String MD5(File file) throws FileNotFoundException {
		return MD5Util.getMd5ByFile(file);
	}

	public File bacpup(File source, String filename) throws IOException {
		File backupDir = this.getBackupDir(filename);
		if (backupDir.exists()) {
			backupDir.mkdirs();
		}
		File targetFile = new File(backupDir, filename);
		FileUtils.copyFile(source, targetFile);
		return targetFile;
	}

	public File get(String filename) {
		return new File(this.getBackupDir(filename), filename);
	}

	public boolean isExists(String filename) {
		File targetFile = new File(this.getBackupDir(filename), filename);
		return targetFile.exists();
	}

	private File getBackupDir(String filename) {
		String fir = filename.substring(0, 2);
		String sec = filename.substring(2, 4);
		File file = new File(backupPath, fir + File.separator + sec);

		return file;
	}

}
