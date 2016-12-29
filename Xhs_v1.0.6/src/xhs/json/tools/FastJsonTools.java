package xhs.json.tools;

import com.alibaba.fastjson.JSON;

public class FastJsonTools {

	public FastJsonTools() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param object
	 *            是对解释的集合的类型
	 * @return
	 */
	public static String createFastJsonString(Object value) {
		String alibabaJson = JSON.toJSONString(value);
        return alibabaJson;
	}

}
