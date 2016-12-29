package com.nowar.activity;

import com.nowar.main.R;
import com.nowar.service.ConnectorService;
import com.nowar.sharedprefs.SharedPrefsUtil;
import com.nowar.activity.MyActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		ConnectorService.intent = new Intent(getApplicationContext(),
				ConnectorService.class);
		startService(ConnectorService.intent);
		if (SharedPrefsUtil.getSharedPrefsUtil().getValue(this, "isAutoLogin",
				false)) {
			new Handler().postDelayed(new Runnable() {
				// @Override
				public void run() {
					Intent intent = new Intent();
					Bundle tokenBun = new Bundle();
					tokenBun.putString("token", "SSSSSSSSS");// ÐèÒªÐÞ¸Ä
					intent.putExtras(tokenBun);
					intent.setClass(StartActivity.this, MyActivity.class);
					startActivity(intent);
					finish();
				}
			}, 2000);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(StartActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}, 2000);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
