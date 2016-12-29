package com.nowar.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.nowar.broadcast.BROADCAST_CONSTANT.NETWORK;
import com.nowar.main.BaseActivity;
import com.nowar.main.R;
import com.nowar.mina.ClientHandler;
import com.nowar.mina.SendService;
import com.nowar.packet.MessagePacket;
import com.nowar.sharedprefs.SharedPrefsUtil;
import com.nowar.activity.MyActivity;
import com.nowar.version.CustomDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登陆界面Activity
 * 
 * @author lindec
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private MessageBackReciver mReciver;
	private IntentFilter mIntentFilter;
	private LocalBroadcastManager mLocalBroadcastManager;
	private Button loginBtn;
	private Button regBtn;
	private EditText userNumber;
	private EditText passWord;
	private String retStr = "";
	private Map<String, String> loginMap;
	private String number;
	private String pass;
	private CustomDialog customDialog;
	private Handler dialoghandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginInitView();
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

	protected void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	private void loginInitView() {

		userNumber = (EditText) findViewById(R.id.login_nameEdt_id);
		passWord = (EditText) findViewById(R.id.login_passwordEdt_id);
		loginBtn = (Button) findViewById(R.id.btn_al_login);
		regBtn = (Button) findViewById(R.id.btn_al_register);

		loginBtn.setOnClickListener(this);
		regBtn.setOnClickListener(this);
		

		customDialog = new CustomDialog(this, R.layout.dialog_layout,
				R.style.DialogTheme);
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(NETWORK.ACTION);

	}
	/**
	 * dialog等待
	 */
	private Runnable dialogRunnable = new Runnable() {
		public void run() {
			if (customDialog.isShowing()) {
				customDialog.dismiss();
			}
			dialoghandler.removeCallbacks(dialogRunnable);
		}
	};

	

	@Override
	public void onClick(View v) {
		if (v.equals(loginBtn)) {
			number = userNumber.getText().toString();
			pass = passWord.getText().toString();
			if (number == null || number.trim().equals("")) {
				Toast.makeText(this, "账号不能为空", 2000).show();
			} else if (pass == null || pass.trim().equals("")) {
				Toast.makeText(this, "密码不能为空", 2000).show();
			} else {
				MessagePacket packet = new MessagePacket("login", number, pass,
						null, null);
				boolean isSend = SendService.sendData(packet);
				// boolean isSuccess = sendToSevice("login", name, pass, null,
				// null);
				// System.out.println("isSuccess:"+isSuccess);
				if (!isSend) {
					Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				} else {
					customDialog.show();
					dialoghandler.postDelayed(dialogRunnable, 10 * 1000);
				}
			}

		}
		if (v.equals(regBtn)) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			LoginActivity.this.startActivity(intent);

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
				if (message.equals("login_success")) {

					SharedPrefsUtil.getSharedPrefsUtil().putValue(
							LoginActivity.this, "isAutoLogin", true);
					SharedPrefsUtil.getSharedPrefsUtil().putValue(
							LoginActivity.this, "userNumber", number);
					SharedPrefsUtil.getSharedPrefsUtil().putValue(
							LoginActivity.this, "passWord", pass);

					ClientHandler.session.setAttribute("userNumber", number);
					Intent in = new Intent();
					Bundle tokenBun = new Bundle();
					tokenBun.putString("token", retStr);
					in.putExtras(tokenBun);
					in.setClass(LoginActivity.this, MyActivity.class);
					startActivity(in);
					finish();
				} else if (message.equals("login_fail")) {
					Toast.makeText(LoginActivity.this, "登陆用户或密码错误，请重新登陆！",
							Toast.LENGTH_LONG).show();
				}
			} else {

			}
		};
	}
}
