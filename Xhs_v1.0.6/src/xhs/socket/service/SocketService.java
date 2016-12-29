package xhs.socket.service;

import java.util.List;
import java.util.Map;

public interface SocketService {

	
	public boolean login(List<Object> params);
	/**
	 * 完成数据库更新修改
	 * @param sql
	 * @param params
	 * @return
	 */
	public  boolean addOrUpdate(String sql,List<Object> params);
	
	/**
	 * 查询数据库设备
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String, Object> checkDB(String sql,List<Object> params);
   // public boolean updataUser(List<Object> params);
	

	public <T> T getSingleParam(String sql, List<Object> params, String key);
}
