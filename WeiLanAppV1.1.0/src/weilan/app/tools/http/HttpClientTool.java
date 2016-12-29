package weilan.app.tools.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientTool {
	/**
	 * HttpClient中POST方式的处理
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByHttpClientPost(String userName, String userPass) {
		// 1.创建 HttpClient 的实例
		HttpClient client = new DefaultHttpClient();
		// 2. 创建某种连接方法的实例，在这里是HttpPost。在 HttpPost 的构造函数中传入待连接的地址
		String uri = "http://172.16.237.200:8080/video/login.do";
		HttpPost httpPost = new HttpPost(uri);
		try {
			// 封装传递参数的集合
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// 往这个集合中添加你要传递的参数
			parameters.add(new BasicNameValuePair("username", userName));
			parameters.add(new BasicNameValuePair("userpass", userPass));
			// 创建传递参数封装 实体对象
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
					"UTF-8");// 设置传递参数的编码
			// 把实体对象存入到httpPost对象中
			httpPost.setEntity(entity);
			// 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例
			HttpResponse response = client.execute(httpPost); // HttpUriRequest的后代对象
																// //在浏览器中敲一下回车
			// 4. 读 response
			if (response.getStatusLine().getStatusCode() == 200) {// 判断状态码
				InputStream is = response.getEntity().getContent();// 获取内容
				final String result = StreamTools.streamToStr(is); // 通过工具类转换文本
				// LoginActivity.this.runOnUiThread(new Runnable() {
				// //通过runOnUiThread方法处理
				// @Override
				// public void run() {
				// //设置控件的内容(此内容是从服务器端获取的)
				// // tv_result.setText(result);
				// }
				// });
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 6. 释放连接。无论执行方法是否成功，都必须释放连接
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * 通过httpClient中的GET方式处理的
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByHttpClientGet(String userName, String userPass) {
		// HttpClient 发请求 GET方式处理
		// 1.创建 HttpClient 的实例 打开一个浏览器
		HttpClient client = new DefaultHttpClient(); // DefaultHttpClient
														// extends
														// AbstractHttpClient
		try {

			// 2. 创建某种连接方法的实例，在这里是HttpGet。在 HttpGet
			// 的构造函数中传入待连接的地址
			String uri = "http://172.16.237.200:8080/video/login.do?username="
					+ userName + "&userpass=" + userPass;
			// 强调 地址不能够出现 localhost:操作
			HttpGet httpGet = new HttpGet(uri);
			// 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例
			HttpResponse response = client.execute(httpGet); // 在浏览器中敲了一下回车
			// 4. 读 response
			int statusCode = response.getStatusLine().getStatusCode();// 读取状态行中的状态码
			if (statusCode == 200) { // 如果等于200 一切ok
				HttpEntity entity = response.getEntity();// 返回实体对象
				InputStream is = entity.getContent(); // 读取实体中内容
				final String result = StreamTools.streamToStr(is); // 通过工具类转换文本
				// LoginActivity.this.runOnUiThread(new Runnable() {
				// //通过runOnUiThread方法处理
				// @Override
				// public void run() {
				// //设置控件的内容(此内容是从服务器端获取的)
				// tv_result.setText(result);
				// }
				// });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5.释放连接。无论执行方法是否成功，都必须释放连接
			client.getConnectionManager().shutdown();// 释放链接
		}
	}

}
