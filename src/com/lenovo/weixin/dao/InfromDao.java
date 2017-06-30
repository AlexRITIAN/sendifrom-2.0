package com.lenovo.weixin.dao;

import java.util.List;

import com.lenovo.weixin.beans.InfromBean;
/**
 * Mybatis Infrom表接口
 * @author yuhao5
 *
 */
public interface InfromDao {
	/**
	 * 插入数据,若字段存在则update
	 * @param infrom
	 * 			Infrom表对应的bean
	 * @return
	 */
	int insert(InfromBean infrom);
	
	/**
	 * 批量插入数据,若字段存在则update
	 * @param list
	 * @return
	 */
	int insertByList(List<InfromBean> list);
	
	/**
	 * 按Id查询
	 * @param id
	 * @return
	 */
	InfromBean selectById(int id);
	/**
	 * 按Name查询
	 * @param name
	 * @return
	 */
	InfromBean selectByName(String name);
}
