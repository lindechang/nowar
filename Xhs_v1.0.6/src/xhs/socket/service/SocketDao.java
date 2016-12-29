package xhs.socket.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import xhs.socket.service.SocketService;
import xhs.tools.jdbc.JdbcUtils;

public class SocketDao implements SocketService {

	JdbcUtils jdbcUtils = null;

	public SocketDao() {
		jdbcUtils = new JdbcUtils();
	}
	
	public boolean login(List<Object> params) {
		// TODO Auto-generated method stub
		boolean flag = false;
		String sql = "select * from userinfo where username=? and pswd=?";
		try {
			jdbcUtils.getConnection();
			Map<String, Object> map = jdbcUtils.findSimpleResult(sql, params);
			flag = !map.isEmpty() ? true : false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			jdbcUtils.releaseConn();
		}
		return flag;
	}

	@Override
	public boolean addOrUpdate(String sql, List<Object> params) {

		boolean flag = false;
		try {

			jdbcUtils.getConnection();
			flag = jdbcUtils.updateByPreparedStatement(sql, params);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			jdbcUtils.releaseConn();
		}
		return flag;
	}

	
	public Map<String, Object> checkDB(String sql, List<Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			jdbcUtils.getConnection();
			map = jdbcUtils.findSimpleResult(sql, params);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			jdbcUtils.releaseConn();
		}
		return map;
	}

	/**
	 * 获取数据库某条记录中某个参数的值
	 * @param sql
	 * @param params
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSingleParam(String sql, List<Object> params, String key) {
		T t = null;
		Map<String, Object> map = new HashMap<String, Object>();
		// String sql = "select * from userinfo where username=? and pswd=?";
		try {
			jdbcUtils.getConnection();
			map = jdbcUtils.findSimpleResult(sql, params);
			// int i = (Integer)map.get("socket");
			if (!map.isEmpty() ? true : false) {
				t = (T) map.get(key);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			jdbcUtils.releaseConn();
		}

		return t;
	}

}
