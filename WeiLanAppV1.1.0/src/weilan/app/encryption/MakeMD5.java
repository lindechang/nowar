package weilan.app.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MakeMD5 {
	public String makeMD5(String password) {

		MessageDigest md;

		try {

			// ����һ��MD5���ܼ���ժҪ

			md = MessageDigest.getInstance("MD5");

			// ����md5����

			md.update(password.getBytes());

			// digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�

			// BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ

			String pwd = new BigInteger(1, md.digest()).toString(16);

			System.err.println(pwd);

			return pwd;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return password;

	}
	
	public static void main(String[] args) {
		MakeMD5 m = new MakeMD5();
		String password = "123456";
		System.out.println(password+"���ܺ�"+m.makeMD5("123456"));
	}

}
