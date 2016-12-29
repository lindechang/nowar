package weilan.app.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 获取软件版本 工具类
 * @author lindec
 *
 */
public class VersionUtils {

	public VersionUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String getVersion(Context context)// 获取版本号
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

	public static int getVersionCode(Context context)// 获取版本号(内部识别号)
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
