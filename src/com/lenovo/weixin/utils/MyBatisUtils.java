package com.lenovo.weixin.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
/**
 * mybatis工具类
 * @author yuhao5
 *
 */
public class MyBatisUtils {
	
	//重量级，线程安全
	private static SqlSessionFactory factory = null;
	public static SqlSession getSqlSession() throws IOException {
		
		SqlSession session = null;
		String config = "mybatis.xml";
			
			if( factory == null) {
				 //1.读取主配置文件
				InputStream inputStream = Resources.getResourceAsStream(config);
				 //2.创建SqlSessionFactory
				factory = new SqlSessionFactoryBuilder().build(inputStream);
			}
			//3.获取SqlSession
			session = factory.openSession();
		return session;
	}

}
