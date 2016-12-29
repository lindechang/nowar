package com.nowar.activity;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.nowar.broadcast.BROADCAST_CONSTANT.NETWORK;
import com.nowar.main.BaseActivity;
import com.nowar.main.R;
import com.nowar.mina.SendService;
import com.nowar.packet.MessagePacket;
import com.nowar.version.CustomDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;
	private Button registerBtn;
	private Button backBtn;

	private EditText regNumber;
	private EditText regPassword;
	// private EditText regEmail;
	private CustomDialog customDialog;
	private Handler dialoghandler = new Handler();
	private Runnable dialogRunnable = new Runnable() {
		public void run() {
			if (customDialog.isShowing()) {
				customDialog.dismiss();
			}
			dialoghandler.removeCallbacks(dialogRunnable);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		InitView();
	}

	private void InitView() {
		customDialog = new CustomDialog(this, R.layout.dialog_layout,
				R.style.DialogTheme);
		registerBtn = (Button) findViewById(R.id.btn_ra_register);
		backBtn = (Button)findViewById(R.id.reg_back_but_id);
		registerBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);

		regNumber = (EditText) findViewById(R.id.et_ra_account);
		regPassword = (EditText) findViewById(R.id.et_ra_password);

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		// mServiceIntent = new Intent(this, BackService.class);
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(NETWORK.ACTION);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(registerBtn)) {
			String Number = regNumber.getText().toString();
			String Pwd = regPassword.getText().toString();

			MessagePacket packet = new MessagePacket("register", Number, Pwd,
					null, null);
			boolean isSend = SendService.sendData(packet);
			// boolean isSend =sendToSevice("register",name,pass,email,null);
			if (!isSend) {
				Toast.makeText(this, "未连接网络,请检查...", 2000).show();
			} else {
				customDialog.show();
				dialoghandler.postDelayed(dialogRunnable, 10 * 1000);
			}

		}else if(v.equals(backBtn)){
			this.finish();
		}

	}
	
	private class MessageBackReciver extends BroadcastReceiver {
		// private WeakReference<TextView> textView;

		public MessageBackReciver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(NETWORK.ACTION)) {
				customDialog.dismiss();
				String message = intent.getStringExtra("message");
				System.out.println("-接收到广播来的message-->>" + message);
				if (message.equals("register_success")) {
					System.out.println("注册成功");
					Intent mIntent = new Intent();
					mIntent.setClass(RegisterActivity.this, LoginActivity.class);
					RegisterActivity.this.startActivity(mIntent);
					finish();
				} else {
					System.out.println("用户存在");
				}
			} else {

			}
		};
	}

}
