package com.lenovo.weixin.service;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.lenovo.weixin.utils.ClientUtil;
import com.lenovo.weixin.utils.JenkinsUtil;
import com.lenovo.weixin.utils.LoadConfig;
import com.qq.weixin.mp.aes.AesException;

import net.sf.json.JSONObject;

/**
 * 发送报警信息
 * 
 * @author yuhao5
 *
 */
public class AlarmService implements Runnable {
	/** 用于生成日志 */
	private Logger logger = Logger.getLogger(AlarmService.class);

	/**
	 * 定时任务执行器会自动调用run
	 */
	@Override
	public void run() {
		System.out.println("AlarmService run!" + new Date().getTime());
		String redNames = null;//状态为红色的项目name
		LoadConfig lc = new LoadConfig();//读取配置文件
		try {
			redNames = ClientUtil.get(lc.getProperty("sendUrl") + "key=red_names");//从公众号后台获取red_names
			//判断red_names是否为空
			if (!"".equals(redNames) && redNames != null && !redNames.isEmpty() && !"null".equals(redNames)) {
				String[] redNamesArray = redNames.split(",");//切割red_names,获取redName数组
				StringBuffer descriptionBuffer = new StringBuffer();//用于拼接发送给用户的消息
				StringBuffer date = new StringBuffer();//用于拼接存放在缓存中的数据
				date.append("[");
				boolean flag = false;//标记是否发送报警信息
				for (String redName : redNamesArray) {
					String url = "http://10.99.201.86/deploy/job/" + redName + "/";
					JSONObject job = JenkinsUtil.getJob(url);//获取状态为red的项目的详细信息
					if (!"red".equals(job.getString("color"))) {
						continue;
					}
					String lastBuidTime = null;//从缓存中获取项目最后build的时间
					try {
						lastBuidTime = ClientUtil
								.get(lc.getProperty("sendUrl") + "key=" + job.getString("name") + "_lastbuildtime");
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
					long timestamp = JenkinsUtil.getJob(job.getJSONObject("lastBuild").getString("url"))
							.getLong("timestamp");
					//判断缓存中是否存在项目最后build时间,有则与从Jenkins获取的项目最后build时间对比,相同则略过,不同则报警
					//不存在,报警
					if (!"".equals(lastBuidTime) && lastBuidTime != null && !lastBuidTime.isEmpty()
							&& !"null".equals(lastBuidTime)) {
						if (Long.valueOf(lastBuidTime) == timestamp) {
							continue;
						}
						date.append("{'name':'" + job.getString("name") + "_lastbuildtime" + "','date':'"
								+ String.valueOf(timestamp) + "'},");
						descriptionBuffer.append(" | " + job.getString("name"));
						flag = true;
						continue;
					}
					date.append("{'name':'" + job.getString("name") + "_lastbuildtime" + "','date':'"
							+ String.valueOf(timestamp) + "'},");
					descriptionBuffer.append(" | " + job.getString("name"));
					flag = true;
				}
				date.append("]");
				date.deleteCharAt(date.length() - 2);
				if (flag) {
					try {
						Send.sendNews(1, 2, "yuhao5","Warning:Build Failed Project",
								descriptionBuffer.toString(), "");//发送报警
						Send.sendAlarm(date.toString());//讲报警的项目信息存入缓存
					} catch (IOException | AesException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
	}

}
