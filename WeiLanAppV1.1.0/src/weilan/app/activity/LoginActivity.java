package weilan.app.activity;

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

import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.data.StaticVariable;
import weilan.app.main.*;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.http.NetTool;
import weilan.app.tools.mina.ClientHandler;
import weilan.app.tools.mina.ClientListener;
import weilan.app.tools.mina.SendService;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import weilan.app.ui.CustomDialog;
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
 * ��½����Activity
 * 
 * @author lindec
 * 
 */
public class LoginActivity extends Activity implements OnClickListener {

	// ��Ҫ�������IP��Ϊ��������IP
	// private String url = "http://157.7.48.137/index.php/home/user/login";
	private String url = "http://mikimao.vicp.cc:8080/XhsServers/servlet/LoginAction";

	// ����������������߳�
	// private HeartbeatThread heartbeatThread = null;

	// socket ���������
	BufferedReader reader = null;
	PrintWriter writer = null;

	private Button loginBtn;
	// private Button regBtn;

	private TextView regText;

	private EditText userName;
	private EditText passWord;

	// private CustomDialog customDialog;

	// private CheckBox remberPassword;
	// private CheckBox autoLogin;

	// private SharedPreferences sharedPreferences;

	private String retStr = "";
	private Map<String, String> loginMap;
	private String name;
	private String pass;
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

	private class MessageBackReciver extends BroadcastReceiver {
		// private WeakReference<TextView> textView;

		public MessageBackReciver() {

		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BCH_CONSTANT.ACTION)) {
				customDialog.dismiss();
				String message = intent.getStringExtra("message");
				System.out
						.println("LoginActivity-���յ��㲥����message-->>" + message);

				if (message.equals("login_success")) {
					// Editor editor = ClientListener.sharedPreferences.edit();
					// editor.putBoolean("isAutoLogin", true);
					// editor.putBoolean("isRember", true);
					// editor.putString("userName", name);
					// editor.putString("passWord", pass);
					// editor.commit();

					SharedPrefsUtil.putValue(LoginActivity.this, "isAutoLogin",
							true);
					SharedPrefsUtil.putValue(LoginActivity.this, "userName",
							name);
					SharedPrefsUtil.putValue(LoginActivity.this, "passWord",
							pass);

					ClientHandler.session.setAttribute("userName", name);

					Intent in = new Intent();
					Bundle tokenBun = new Bundle();
					tokenBun.putString("token", retStr);
					in.putExtras(tokenBun);
					in.setClass(LoginActivity.this, MainActivity.class);
					LoginActivity.this.startActivity(in);
					finish();
				} else if (message.equals("login_fail")) {
					Toast.makeText(LoginActivity.this, "��½�û���������������µ�½��",
							Toast.LENGTH_LONG).show();
				}
			} else {

			}
		};
	}

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	// ThreadParam param;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		loginInitView();
	}

	private void loginInitView() {

		// TODO Auto-generated method stub
		customDialog = new CustomDialog(this, R.layout.dialog_layout,
				R.style.DialogTheme);
		userName = (EditText) findViewById(R.id.login_nameEdt_id);
		passWord = (EditText) findViewById(R.id.login_passwordEdt_id);
		regText = (TextView) findViewById(R.id.register_text_id);
		loginBtn = (Button) findViewById(R.id.login_btn_id);
		// remberPassword = (CheckBox)
		// findViewById(R.id.login_remberPassWordBox_id);
		// autoLogin = (CheckBox) findViewById(R.id.login_autoBox_id);

		// ClientListener.sharedPreferences = getApplicationContext()
		// .getSharedPreferences("MySharedPrefs", MODE_PRIVATE);

		loginBtn.setOnClickListener(this);
		regText.setOnClickListener(this);
		// regBtn.setOnClickListener(this);
		// �ж��Ƿ�ѡ���Զ���¼�İ�ť
		// if (autoLogin.isChecked()) {
		// sharedPreferences.edit().putBoolean("isAutoLogin", true).commit();
		// } else {
		// sharedPreferences.edit().putBoolean("isAutoLogin", false).commit();
		// }
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		// mServiceIntent = new Intent(this, BackService.class);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BCH_CONSTANT.ACTION);

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(loginBtn)) {
			name = userName.getText().toString();
			pass = passWord.getText().toString();
			// boolean isSuccess = sendSocketSevice(name, pass);
			// PacketService service = new PacketService();
			// String msg = FastJsonTools.createFastJsonString(service
			// .setPacket("login", name, pass, null, null));

			MessagePacket packet = new MessagePacket("login", name, pass, null,
					null);
			boolean isSend = SendService.sendData(packet);
			// boolean isSuccess = sendToSevice("login", name, pass, null,
			// null);
			// System.out.println("isSuccess:"+isSuccess);
			if (!isSend) {
				Toast.makeText(this, "δ��������,����...", 2000).show();
			} else {
				customDialog.show();
				dialoghandler.postDelayed(dialogRunnable, 10 * 1000);
			}
		}
		if (v.equals(regText)) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			LoginActivity.this.startActivity(intent);

		}
	}

	// public synchronized boolean sendToSevice(String type, String username,
	// String password, String devicenumber, String content) {
	// boolean flag = false;
	// if (ClientHandler.session == null) {
	// return false;
	// }
	// try {
	// // PrintWriter writer = new PrintWriter(
	// // TcpService.socket.getOutputStream(), true);
	// PacketService service = new PacketService();
	// String msg = FastJsonTools.createFastJsonString(service.setPacket(
	// type, username, password, devicenumber, content));
	// // out.println(msg);
	// if (!ClientHandler.session.isClosing()
	// && ClientHandler.session.isConnected()) {
	// ClientHandler.session.write(msg);
	// flag = true;
	// } else {
	// flag = false;
	// }
	// } catch (Exception e) {
	// flag = false;
	// System.out.println(e.toString());
	// }
	// return flag;
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}
}
