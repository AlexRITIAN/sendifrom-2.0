package com.lenovo.weixin.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
/**
 * 执行CMD命令工具类
 * @author yuhao5
 *
 */
public class CMDUtil {
	private static Logger logger = Logger.getLogger(CMDUtil.class);
	/**
	 * 执行linux命令
	 * @param cmd
	 * 			--要执行的命令
	 * @return
	 * 		--执行命令的结果
	 */
	public static String callCMD(String cmd) {
		StringBuffer buffer = new StringBuffer();
//		String[] orderspilt = order.split("@");
		try {
			// curl -H "Content-Type:application/json" -X POST --data
			// (json.data) URL
//			String cmd = "curl -X POST http://10.99.201.86/deploy/job/" + orderspilt[1] + "/build --user admin:admin86";
			// String[] cmd = {"tar", "-cf", tarName, fileName};
			Process process = Runtime.getRuntime().exec(cmd);//执行cmd命令
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));//获取控制台输入流
			String line = "";
			while ((line = input.readLine()) != null) {
				buffer.append(line);
			}
			input.close();
			int waitFor = process.waitFor();//等待命令执行完成,不为0说明执行出现问题
			if (waitFor != 0) {
				logger.info("waitFor : " + waitFor);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return buffer.toString();
	}
}
