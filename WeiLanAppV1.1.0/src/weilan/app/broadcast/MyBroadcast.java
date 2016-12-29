package weilan.app.broadcast;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class MyBroadcast {

	private Context context;
	private LocalBroadcastManager mLocalBroadcastManager;

	public MyBroadcast(Context context) {
		this.context = context;
	}

	public void sendBroadcast(Intent intent) {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
		mLocalBroadcastManager.sendBroadcast(intent);
	}

}
