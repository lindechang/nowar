package com.lindec.app.tools;

import java.util.Calendar;

public class DateUitl {

	/**
	 * 系统时间
	 * @return
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));//年
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);//月
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));//日
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//小时
		String mins = String.valueOf(c.get(Calendar.MINUTE));//分钟
		String sec = String.valueOf(c.get(Calendar.SECOND));//秒
		String ms = String.valueOf(c.get(Calendar.MILLISECOND));//毫秒

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "\\" + month + "\\" + day + " " + hour + ":"
				+ mins+" "+sec+"\'"+ms+"\"");
		return sbBuffer.toString();
	}
	
	public static String getMSMDate() {
		Calendar c = Calendar.getInstance();
		String mins = String.valueOf(c.get(Calendar.MINUTE));
		String ss = String.valueOf(c.get(Calendar.SECOND));
		String ms = String.valueOf(c.get(Calendar.MILLISECOND));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(mins + ":" + ss + ":" + ms );

		return sbBuffer.toString();
	}
}
