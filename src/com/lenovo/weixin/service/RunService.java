package com.lenovo.weixin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.lenovo.weixin.beans.InfromBean;
import com.lenovo.weixin.utils.DBUtil;
import com.lenovo.weixin.utils.JenkinsUtil;
import com.lenovo.weixin.utils.ParseJSON;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 想公众号发送Jenkins信息,获取命令,发送结果集
 * 
 * @author yuhao5
 *
 */
public class RunService implements Runnable {
	/** 用于获取,执行命令*/
	private Order order = new Order();
	/** 用于声称日志*/
	private Logger logger = Logger.getLogger(RunService.class);

	@Override
	public void run() {
		try {
			System.out.println("RunService run!" + new Date().getTime());
			//获取所有项目的状态和name
			InfromBean infrom = JenkinsUtil.updataDB();
			JSONObject json = ParseJSON.getJSON(infrom.getInfrom());
			order.getOrder();//获取命令,并且执行命令,将结果发送到公众号
			//将项目状态信息写入数据库
			DBUtil.insert(infrom);
			DBUtil.insertJobs(JenkinsUtil.updata(json, "jobs", infrom.getCreat_time(), infrom.getEdit_time()));
			//拼接要写入缓存的数据
			String sendJson = "{\'names\':\'" + JenkinsUtil.get(json, "jobs").get("names") + "\',\'jobs\':"
					+ JenkinsUtil.get(json, "jobs").get("jobs") + ",\'views_names\':\'"
					+ JenkinsUtil.get(json, "views").get("names") + "\',\'views\':"
					+ JenkinsUtil.get(json, "views").get("views") + "}";
			System.out.println(
					(JenkinsUtil.get(json, "views").get("views") != null) ? "get date success !" : "get date fail !");
			//将数据写入缓存
			Send.sendJenkins(sendJson);

			JSONArray jobs = json.getJSONArray("jobs");
			List<String> redNames = new ArrayList<>();//状态为fail的项目的name
			//遍历项目,将状态为fail的name写入list
			for (Object job : jobs) {
				JSONObject jobJson = (JSONObject) job;
				if ("red".equals(jobJson.getString("color"))) {
					redNames.add(jobJson.getString("name"));
				}
			}
			
			//将状态为fail的项目信息写入缓存
			String msg = "{'name':'red_names','date':'"
					+ redNames.toString().replace("[", "").replace("]", "").replace(" ", "") + "'}";
			Send.sendAlarm(msg);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
