package com.nowar.broadcast;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class NetworkBroadcast {
	private Context context;
	 private static LocalBroadcastManager mLocalBroadcastManager;
	public NetworkBroadcast(Context context) {
		this.context = context;
		mLocalBroadcastManager  = LocalBroadcastManager.getInstance(context);
	}
	
	public static void sendLocalBroadcast(Intent intent) {
		mLocalBroadcastManager.sendBroadcast(intent);
	}
}
