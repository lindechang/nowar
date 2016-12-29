package com.nowar.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BaseActivity extends Activity {

	public Context context;
	public boolean stopActivity = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ���Activity����ջ
		context = this;
		AppManager.getAppManager().addActivity(this);
		stopActivity = false;

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopActivity = true;
		// ����Activity���Ӷ�ջ���Ƴ�
		AppManager.getAppManager().finishActivity(this);
	}
}
