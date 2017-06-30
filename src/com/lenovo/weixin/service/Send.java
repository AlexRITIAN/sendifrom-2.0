package com.lenovo.weixin.service;

import java.io.IOException;
import java.util.Date;
import com.lenovo.weixin.utils.ClientUtil;
import com.lenovo.weixin.utils.LoadConfig;
import com.lenovo.weixin.utils.Mygenerate;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * 向公众号发送消息
 * 
 * @author yuhao5
 *
 */
public class Send {
	private static String sendBase(String replyMsg) throws IOException, AesException {
		LoadConfig lc = new LoadConfig();
		WXBizMsgCrypt wx = new WXBizMsgCrypt(lc.getProperty("token"), lc.getProperty("encodingAesKey"),
				lc.getProperty("corpId"));
		String timeStamp = String.valueOf(new Date().getTime());
		String nonce = wx.getRandomStr();
		String encrypt = wx.encrypt(nonce, replyMsg);
		String postStr = Mygenerate.generate(encrypt, "yuhao5");
		String postURL = lc.getProperty("sendUrl") + "msg_signature="
				+ SHA1.getSHA1(lc.getProperty("token"), timeStamp, nonce, encrypt) + "&timestamp=" + timeStamp
				+ "&nonce=" + nonce;

		return ClientUtil.post(postURL, postStr);
	}

	/**
	 * 发送text消息
	 * 
	 * @param tagetType
	 *            目标分类:1为用户,2为部门,3为标签
	 * @param taget
	 *            目标数目:1为全部,2为自选
	 * @param tagetID
	 *            目标ID:格式为id1|id2|id3,如果taget为1,可以忽略该参数
	 * @param msg
	 *            发送给公众号的消息内容
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	public static String sendMsg(int tagetType, int taget, String tagetID, String msg)
			throws IOException, AesException {
		String replyMsg = "{\"tagetType\":\"" + tagetType + "\",\"taget\":\"" + taget + "\",\"tagetID\":\"" + tagetID
				+ "\",\"type\":\"text\",\"msg\":\"" + msg + "\",\"status\":\"Csendfunction\"}";
		return sendBase(replyMsg);
	}

	/**
	 * 发送news消息
	 * 
	 * @param tagetType
	 *            目标分类:1为用户,2为部门,3为标签
	 * @param taget
	 *            目标数目:1为全部,2为自选
	 * @param tagetID
	 *            目标ID:格式为id1|id2|id3,如果taget为1,可以忽略该参数
	 * @param msg
	 *            发送给公众号的消息内容
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	public static String sendNews(int tagetType, int taget, String tagetID, String title, String description,
			String picurl) throws IOException, AesException {
		String replyMsg = "{\"tagetType\":\"" + tagetType + "\",\"taget\":\"" + taget + "\",\"tagetID\":\"" + tagetID
				+ "\",\"type\":\"news\",\"title\":\"" + title + "\",\"description\":\"" + description
				+ "\",\"picurl\":\"" + picurl + "\",\"status\":\"Csendfunction\"}";
		return sendBase(replyMsg);
	}
	/**
	 * 
	 * @param msg
	 * @param status
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	private static String sendEcacheBase(String msg, String status) throws IOException, AesException {
		String replyMsg = "{\"tagetType\":\"1\",\"taget\":\"2\",\"tagetID\":\"yuhao5\",\"type\":\"text\",\"msg\":\""
				+ msg + "\",\"status\":\"" + status + "\"}";
		return sendBase(replyMsg);
	}
	/**
	 * 把报警信息写入缓存
	 * @param msg
	 * 			报警信息
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	public static String sendAlarm(String msg) throws IOException, AesException {
		return sendEcacheBase(msg, "AlarmLastTimeFunction");
	}
	/**
	 * 把Jenkins项目信息写入缓存
	 * @param msg
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	public static String sendJenkins(String msg) throws IOException, AesException {
		return sendEcacheBase(msg, "JenkinsFunction");
	}
}
