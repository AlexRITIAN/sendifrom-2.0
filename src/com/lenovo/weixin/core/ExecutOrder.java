package com.lenovo.weixin.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.lenovo.weixin.beans.InfromBean;
import com.lenovo.weixin.dao.InfromDao;
import com.lenovo.weixin.service.Send;
import com.lenovo.weixin.utils.CMDUtil;
import com.lenovo.weixin.utils.ClientUtil;
import com.lenovo.weixin.utils.LoadConfig;
import com.lenovo.weixin.utils.MyBatisUtils;
import com.lenovo.weixin.utils.ParseJSON;
import com.qq.weixin.mp.aes.AesException;

import net.sf.json.JSONObject;

/**
 * 执行命令
 * 
 * @author yuhao5
 *
 */
public class ExecutOrder {
	/** 日志 */
	private Logger logger = Logger.getLogger(ExecutOrder.class);
	/** 读取配置文件 */
	private LoadConfig lc = new LoadConfig();
	/** cmd命令头部,用于拼接cmd命令 */
	private String cmdBaise = lc.getProperty("cmdBaise");
	/** cmd命令尾部,用于拼接cmd命令 */
	private String cmdEnd = lc.getProperty("cmdEnd");

	/**
	 * 
	 * @param orders
	 *            接收到的多个命令
	 * @return 执行命令的结果,key为命令,value为命令执行后的结果
	 * @throws IOException
	 * @throws AesException
	 */
	public Map<String, Object> execut(String[] orders) throws IOException, AesException {
		Map<String, Object> resultMap = new HashMap<>();
		if (orders.length > 1) {
			for (int i = 1; i < orders.length; i++) {
				logger.info("orders[" + i + "] :" + orders[i]);
				if ("查询".equals(orders[i])) {// 执行查询命令
					Send.sendMsg(1, 2, "yuhao5", "当前只支持build命令");
					// resultMap.putAll(executFind(orders[i]));
				} else if ("server_status".equals(orders[i])) {// 执行服务器状态命令
					Send.sendMsg(1, 2, "yuhao5", "当前只支持build命令");
					// resultMap.putAll(executServerStatus(orders[i]));
				} else if (orders[i].indexOf("build@") != -1) {// 执行jenkins build命令
					resultMap.putAll(executJobs(orders[i]));
				} else if (!"null".equals(orders[i]) && !orders[i].isEmpty()) {// 错误命令
					Send.sendMsg(1, 2, "yuhao5", "错误指令:" + orders[i]);
				}
			}
		}
		return resultMap;
	}

	/**
	 * 执行查询命令,从Infrom表中查询,目前已作废
	 * 
	 * @param order
	 *            接收到的命令
	 * @return 执行命令的结果,key为命令,value为命令执行后的结果
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private Map<String, Object> executFind(String order) throws IOException {
		Map<String, Object> result = new HashMap<>();
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		InfromDao mapper = sqlSession.getMapper(InfromDao.class);
		InfromBean selectById = mapper.selectById(1);
		result.put(order, selectById);
		logger.info(order);
		return result;
	}

	/**
	 * 查询服务器状态,从Infrom表查询,目前已作废
	 * 
	 * @param order
	 *            接收到的命令
	 * @return 执行命令的结果,key为命令,value为命令执行后的结果
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private Map<String, Object> executServerStatus(String order) throws IOException {
		Map<String, Object> result = new HashMap<>();
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		InfromDao mapper = sqlSession.getMapper(InfromDao.class);
		LoadConfig lc = new LoadConfig();
		String resEntity = ClientUtil.get(lc.getProperty("sendUrl") + "key=server_status_param");
		logger.info(resEntity);
		JSONObject json = ParseJSON.getJSON(resEntity);
		InfromBean selectById = mapper.selectByName(json.getString("name"));
		result.put(order, selectById.getInfrom().replace("\"", "'"));
		logger.info(order);
		return result;
	}

	/**
	 * 执行Jenkins Build命令
	 * 
	 * @param order
	 *            接收到的命令@jobname格式的String
	 * @return 执行结果,实际上Jenkins执行build命令不会返回任何数据
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private Map<String, Object> executJobs(String order) throws ClientProtocolException, IOException {
		Map<String, Object> result = new HashMap<>();
		String[] split = order.split("@");// 切割字符串,获取命令和jobName
		String cmd = cmdBaise + split[1] + "/" + split[0] + cmdEnd;// 拼接cmd命令
		result.put(order, CMDUtil.callCMD(cmd));// 将命令和执行cmd结果放入map
		return result;
	}

}
