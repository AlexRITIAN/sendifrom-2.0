package com.lenovo.weixin.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.lenovo.weixin.beans.InfromBean;
import com.qq.weixin.mp.aes.AesException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 从Jenkins获取数据工具类
 * @author yuhao5
 *
 */
public class JenkinsUtil {
	private static String cmdStart = "curl -X POST ";
	private static String cmdEnd = "api/json --user admin:admin86";
	/**
	 * 获取所有项目的状态和name
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	public static InfromBean updataDB() throws IOException, AesException {
		InfromBean infrom = new InfromBean();
		String cmd = "curl -X POST http://10.99.201.86/deploy/api/json?pretty=true --user admin:admin86";
		Timestamp creat_time = new Timestamp(new Date().getTime());
		Timestamp edit_time = creat_time;
		
		infrom.setName("all");
		infrom.setInfrom(CMDUtil.callCMD(cmd));
		infrom.setCreat_time(creat_time);
		infrom.setEdit_time(edit_time);
		
		return infrom;
	}
	
	public static Map<String,String> get(JSONObject json,String name){
		Map<String,String> map = new HashMap<>();
		JSONArray jobs = json.getJSONArray(name);
		JSONArray sendJobs = new JSONArray();
		List<String> names = new ArrayList<>();
		for (Object job : jobs) {
			JSONObject jobj = (JSONObject)job;
			if("All".equals(jobj.getString("name"))){
				continue;
			}
			String jobCMD = cmdStart + jobj.getString("url") + cmdEnd;
			names.add(jobj.getString("name"));
			sendJobs.add(CMDUtil.callCMD(jobCMD));
		}
		map.put("names", names.toString());
		map.put(name, sendJobs.toString().replace("\"", "'"));
		return map;
	}
	
	public static List<InfromBean> updata(JSONObject json,String name,Timestamp creat_time, Timestamp edit_time){
		List<InfromBean> list = new ArrayList<>();
		JSONArray jobs = json.getJSONArray(name);
//		JSONArray sendJobs = new JSONArray();
//		List<String> names = new ArrayList<>();
		for (Object job : jobs) {
			JSONObject jobj = (JSONObject)job;
			InfromBean jobBean = new InfromBean();
			String jobCMD = cmdStart + jobj.getString("url") + cmdEnd;
			jobBean.setName(jobj.getString("name"));
			jobBean.setInfrom(CMDUtil.callCMD(jobCMD));
			jobBean.setCreat_time(creat_time);
			jobBean.setEdit_time(edit_time);
			list.add(jobBean);
//			names.add(jobj.getString("name"));
//			sendJobs.add(jobBean.getInfrom());
		}
		return list;
	}
	
	public static JSONObject getJob(String url){
		String cmd = cmdStart + url + cmdEnd;
		return ParseJSON.getJSON(CMDUtil.callCMD(cmd));
		
	}
}
