package com.lenovo.weixin.utils;
/**
 * 
 * @author yuhao5
 *
 */
public class Mygenerate {
	public static String generate(String encrypt, String tagetId) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<ToUserName><![CDATA[%2$s]]></ToUserName>\n" + "</xml>";
		return String.format(format, encrypt, tagetId);

	}
}
