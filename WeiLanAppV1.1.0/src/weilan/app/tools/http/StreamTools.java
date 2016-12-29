package weilan.app.tools.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {

	/**
	 * ��������ת�����ַ�������
	 * 
	 * @param is
	 * @return
	 */
	public static String streamToStr(InputStream is) {
		try {
			// �����ֽ��������������
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			// �����ȡ�ĳ���
			int len = 0;
			// �����ȡ�Ļ�����
			byte buffer[] = new byte[1024];
			// ���ն���Ļ���������ѭ����ȡ��ֱ����ȡ���Ϊֹ
			while ((len = is.read(buffer)) != -1) {
				// ���ݶ�ȡ�ĳ���д�뵽�ֽ����������������
				os.write(buffer, 0, len);
			}
			// �ر���
			is.close();
			os.close();
			// �Ѷ�ȡ���ֽ��������������ת�����ֽ�����
			byte data[] = os.toByteArray();
			// ����ָ���ı������ת�����ַ���(�˱���Ҫ�����˵ı���һ�¾Ͳ���������������ˣ�androidĬ�ϵı���ΪUTF-8)
			return new String(data, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
