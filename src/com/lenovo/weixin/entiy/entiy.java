package com.lenovo.weixin.entiy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.lenovo.weixin.service.AlarmService;
import com.lenovo.weixin.service.RunService;
import com.lenovo.weixin.service.Send;
/**
 * 程序入口
 * @author yuhao5
 *
 */
public class entiy {
	public static void main(String[] args) {
//		EhcacheUtil ehcache = EhcacheUtil.getEhcacheUtil();
		/** 用于生成日志*/
		Logger logger = Logger.getLogger(entiy.class);
		/** 用于执行定时任务*/
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(10);
		RunService runService = new RunService();
		AlarmService alarmService = new AlarmService();
		try {
			Send.sendMsg(1, 2, "yuhao5", "server started !服务器启动!");//程序启动时向公众号发送启动消息
			schedule.scheduleAtFixedRate(runService, 0, 5, TimeUnit.SECONDS);//执行向公众号后台发送数据,获取命令任务,程序启动开始执行,周期5秒
			schedule.scheduleAtFixedRate(alarmService, 0, 6, TimeUnit.SECONDS);//执行报警任务,程序启动后开始执行,周期6秒
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
}
