package com.lindec.app.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author lindec
 * @Create 2015/4/2
 * @last 2015/4/2
 * @version 1.0
 * @annotation ��ȡ����汾 ������
 */
public class VersionUtils {

	private static VersionUtils mVersionUtils;

	public VersionUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * ��һʵ��
	 */
	public static VersionUtils getVersionUtils() {
		if (mVersionUtils == null) {
			mVersionUtils = new VersionUtils();
		}
		return mVersionUtils;
	}

	public String getVersion(Context context)// ��ȡ�汾��
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "version_unknown";
		}
	}

	public int getVersionCode(Context context)// ��ȡ�汾��(�ڲ�ʶ���)
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

}
