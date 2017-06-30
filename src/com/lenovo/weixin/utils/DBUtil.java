package com.lenovo.weixin.utils;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.lenovo.weixin.beans.InfromBean;
import com.lenovo.weixin.dao.InfromDao;
/**
 * 数据库操作工具类
 * @author yuhao5
 *
 */
public class DBUtil {
	/**
	 * 向表Infrom插入单条数据
	 * @param infrom
	 * @return
	 * @throws IOException
	 */
	public static int insert(InfromBean infrom) throws IOException{
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		InfromDao mapper = sqlSession.getMapper(InfromDao.class);
		return mapper.insert(infrom);
	}
	
	/**
	 * 插入多条数据
	 * @param infoList
	 * @return
	 * @throws IOException
	 */
	public static int insertJobs(List<InfromBean> infoList) throws IOException{
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		InfromDao mapper = sqlSession.getMapper(InfromDao.class);
		return mapper.insertByList(infoList);
	}
}
