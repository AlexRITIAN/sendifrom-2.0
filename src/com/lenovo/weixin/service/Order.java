package com.lenovo.weixin.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lenovo.weixin.core.ExecutOrder;
import com.lenovo.weixin.utils.ClientUtil;
import com.lenovo.weixin.utils.LoadConfig;
import com.qq.weixin.mp.aes.AesException;

/**
 * 处理接收到的命令,并发送结果到公众号
 * 
 * @author yuhao5
 *
 */
public class Order {
	/** 用于执行命令 */
	private ExecutOrder exOrder = new ExecutOrder();
	/** 用于生成日志 */
	private Logger logger = Logger.getLogger(Order.class);

	/**
	 * 获取命令,执行,向公众号发送结果
	 * 
	 * @throws IOException
	 * @throws AesException
	 * @throws InterruptedException
	 */
	public synchronized void getOrder() throws IOException, AesException, InterruptedException {
		LoadConfig lc = new LoadConfig();// 读取配置文件
		String resEntity = ClientUtil.get(lc.getProperty("sendUrl"));// 获取命令,格式:order1@-@order2@-@order3
		String[] orders = resEntity.split("@-@");
		Map<String, Object> resultMap = exOrder.execut(orders);// 执行命令,获取结果集
		Set<String> keySet = resultMap.keySet();
		for (String key : keySet) {// 遍历结果集,发送结果给公众号
			logger.info(key + " : " + resultMap.get(key).toString());
			Send.sendNews(1, 2, "yuhao5", key, resultMap.get(key).toString(),
					"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488346231130&di=0816c162b16f749334c84011da697c53&imgtype=0&src=http%3A%2F%2Fvpic.video.qq.com%2F64228100%2Fr0356ho7uul_ori_3.jpg");

		}
	}
}
