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
	 * HttpClient��POST��ʽ�Ĵ���
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByHttpClientPost(String userName, String userPass) {
		// 1.���� HttpClient ��ʵ��
		HttpClient client = new DefaultHttpClient();
		// 2. ����ĳ�����ӷ�����ʵ������������HttpPost���� HttpPost �Ĺ��캯���д�������ӵĵ�ַ
		String uri = "http://172.16.237.200:8080/video/login.do";
		HttpPost httpPost = new HttpPost(uri);
		try {
			// ��װ���ݲ����ļ���
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// ����������������Ҫ���ݵĲ���
			parameters.add(new BasicNameValuePair("username", userName));
			parameters.add(new BasicNameValuePair("userpass", userPass));
			// �������ݲ�����װ ʵ�����
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
					"UTF-8");// ���ô��ݲ����ı���
			// ��ʵ�������뵽httpPost������
			httpPost.setEntity(entity);
			// 3. ���õ�һ���д����õ�ʵ���� execute ������ִ�еڶ����д����õ� method ʵ��
			HttpResponse response = client.execute(httpPost); // HttpUriRequest�ĺ������
																// //�����������һ�»س�
			// 4. �� response
			if (response.getStatusLine().getStatusCode() == 200) {// �ж�״̬��
				InputStream is = response.getEntity().getContent();// ��ȡ����
				final String result = StreamTools.streamToStr(is); // ͨ��������ת���ı�
				// LoginActivity.this.runOnUiThread(new Runnable() {
				// //ͨ��runOnUiThread��������
				// @Override
				// public void run() {
				// //���ÿؼ�������(�������Ǵӷ������˻�ȡ��)
				// // tv_result.setText(result);
				// }
				// });
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 6. �ͷ����ӡ�����ִ�з����Ƿ�ɹ����������ͷ�����
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * ͨ��httpClient�е�GET��ʽ�����
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByHttpClientGet(String userName, String userPass) {
		// HttpClient ������ GET��ʽ����
		// 1.���� HttpClient ��ʵ�� ��һ�������
		HttpClient client = new DefaultHttpClient(); // DefaultHttpClient
														// extends
														// AbstractHttpClient
		try {

			// 2. ����ĳ�����ӷ�����ʵ������������HttpGet���� HttpGet
			// �Ĺ��캯���д�������ӵĵ�ַ
			String uri = "http://172.16.237.200:8080/video/login.do?username="
					+ userName + "&userpass=" + userPass;
			// ǿ�� ��ַ���ܹ����� localhost:����
			HttpGet httpGet = new HttpGet(uri);
			// 3. ���õ�һ���д����õ�ʵ���� execute ������ִ�еڶ����д����õ� method ʵ��
			HttpResponse response = client.execute(httpGet); // �������������һ�»س�
			// 4. �� response
			int statusCode = response.getStatusLine().getStatusCode();// ��ȡ״̬���е�״̬��
			if (statusCode == 200) { // �������200 һ��ok
				HttpEntity entity = response.getEntity();// ����ʵ�����
				InputStream is = entity.getContent(); // ��ȡʵ��������
				final String result = StreamTools.streamToStr(is); // ͨ��������ת���ı�
				// LoginActivity.this.runOnUiThread(new Runnable() {
				// //ͨ��runOnUiThread��������
				// @Override
				// public void run() {
				// //���ÿؼ�������(�������Ǵӷ������˻�ȡ��)
				// tv_result.setText(result);
				// }
				// });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5.�ͷ����ӡ�����ִ�з����Ƿ�ɹ����������ͷ�����
			client.getConnectionManager().shutdown();// �ͷ�����
		}
	}

}
