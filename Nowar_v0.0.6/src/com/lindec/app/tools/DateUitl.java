package com.lindec.app.tools;

import java.util.Calendar;

public class DateUitl {

	/**
	 * ϵͳʱ��
	 * @return
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));//��
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);//��
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));//��
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//Сʱ
		String mins = String.valueOf(c.get(Calendar.MINUTE));//����
		String sec = String.valueOf(c.get(Calendar.SECOND));//��
		String ms = String.valueOf(c.get(Calendar.MILLISECOND));//����

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
