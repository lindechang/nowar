package com.nowar.broadcast;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class AllBroadcast {

	private Context context;

	public AllBroadcast(Context context) {
		this.context = context;
		new NetworkBroadcast(context);
		new BluetoothBroadcast(context);
	}

}
