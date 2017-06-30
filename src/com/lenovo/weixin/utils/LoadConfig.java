package com.lenovo.weixin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * 配置文件工具类
 * @author yuhao5
 *
 */
public class LoadConfig {
	private FileInputStream in;
	private Logger logger = Logger.getLogger(LoadConfig.class);
	Properties pro;

	public LoadConfig() {
		try {
			in = new FileInputStream(new File("/home/appadmin/weixin/conf/conf.properties"));
			pro = new Properties();
			pro.load(in);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getProperty(String key){
		return pro.getProperty(key);
	}
}
