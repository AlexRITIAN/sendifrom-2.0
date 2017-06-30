package com.lenovo.weixin.utils;

import net.sf.json.JSONObject;
/**
 * Json转换工具类
 * @author yuhao5
 *
 */
public class ParseJSON {
	public static JSONObject getJSON(String JSONStr){
		JSONObject jsonObj = JSONObject.fromObject(JSONStr);
		return jsonObj;
	}
}
