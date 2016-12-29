package com.lindec.app.tools;

public class PrintUtil {

	private static PrintUtil mPrintUtil;

	public static PrintUtil getPrintUtil() {
		if (mPrintUtil == null) {
			mPrintUtil = new PrintUtil();
		}
		return mPrintUtil;
	}

	public void println(String msg) {
		System.out.println(msg);
	}

	public void println(int msg) {
		System.out.println(msg);
	}

	public void print(String msg) {
		System.out.print(msg);
	}

	public void print(int msg) {
		System.out.print(msg);
	}
}
