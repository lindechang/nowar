package weilan.app.activity;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.main.R;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.http.NetTool;
import weilan.app.tools.mina.ClientHandler;
import weilan.app.tools.mina.SendService;
import weilan.app.ui.CustomDialog;
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

public class RegisterActivity extends Activity implements OnClickListener {

	// private String url = "http://157.7.48.137/index.php/home/user/reg";
	// private String url
	// ="http://mikimao.vicp.cc:8080/XhsServers/servlet/RegisterAction";
	private Button registerBtn;

	private EditText regName;
	private EditText regPassword;
	private EditText regEmail;
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

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_view);
		InitView();
	}

	private void InitView() {
		customDialog = new CustomDialog(this, R.layout.dialog_layout,
				R.style.DialogTheme);
		registerBtn = (Button) findViewById(R.id.reg_but_id);
		registerBtn.setOnClickListener(this);

		regName = (EditText) findViewById(R.id.reg_nameEdt_id);
		regPassword = (EditText) findViewById(R.id.reg_passwordEdt_id);
		regEmail = (EditText) findViewById(R.id.reg_emailEdt_id);

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

	@Override
	protected void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(registerBtn)) {
			String name = regName.getText().toString();
			String pass = regPassword.getText().toString();
			String email = regEmail.getText().toString();
			System.out.println("name-----:" + name);
			System.out.println("pass-----:" + pass);
			System.out.println("email-----:" + email);
			MessagePacket packet = new MessagePacket("register", name, pass,
					email, null);
			boolean isSend = SendService.sendData(packet);
			// boolean isSend =sendToSevice("register",name,pass,email,null);
			if (!isSend) {
				Toast.makeText(this, "未连接网络,请检查...", 2000).show();
			} else {
				customDialog.show();
				dialoghandler.postDelayed(dialogRunnable, 10 * 1000);
			}
			// try {
			// retStr = NetTool.sendPostRequest(url, retMap, "utf-8");
			// boolean flag = JsonTools.getResult("result",retStr);
			// if (flag) {
			// System.out.println("注册成功");
			// // System.out.println("retStr-----:" + retStr);
			// // Intent intent = new Intent();
			// // intent.setClass(LoginActivity.this,
			// // DeviceListActivity.class);
			// // LoginActivity.this.startActivity(intent);
			// } else {
			// System.out.println("用户名或密码不正确，请重新输入！");
			// }
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

		}

	}

	/**
	 * @param type
	 * @param username
	 * @param password
	 * @param devicenumber
	 * @param content
	 * @return
	 */
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
}
