package weilan.app.activity;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.db.DeviceDB;
import weilan.app.db.SwDB;
import weilan.app.db.SwdbFun;
import weilan.app.main.R;
import weilan.app.popupwindow.MorePopWindow;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.http.NetTool;
import weilan.app.tools.mina.ClientHandler;
import weilan.app.tools.mina.SendService;
//import yundian.tracker.tcp.TcpService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddDeviceActivity extends Activity implements OnClickListener {

	// private String url =
	// "http://mikimao.vicp.cc:8080/XhsServers/servlet/DeviceAction";
	// public static String deviceNumber;
	public static Activity TAG = null;
	public static String deviceNumber;
	private String retStr = "";
	private Map<String, String> chaMap;

	// private EditText editText;
	// private Button btn;

	private Button addset;
	private Button back;
	private LinearLayout layout;
	private TextView checkText;

	// private DeviceDB mDeviceDB;
	private SwDB mSwdb;
	private SwdbFun swFun;
	// private Cursor mCursor;
	private MorePopWindow morePopWindow;

	// private String deviceNumber = null;

	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			layout.setVisibility(LinearLayout.VISIBLE);
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
			} else {
				String message = intent.getStringExtra("message");
				System.out.println("AddDeviceActivity-接收到广播来的message-->>"
						+ message);
				if (message.equals("check_success")) {
					layout.setVisibility(LinearLayout.VISIBLE);
				} else if (message.equals("binding_success")) {
					// /mSwdb.Open();
					// System.out.println("查看设备是否已存在："
					// + mSwdb.checkDevice(deviceNumber));
					if (!swFun.CheckSwDevice(mSwdb, deviceNumber)) {
						swFun.InsertDevice(mSwdb, "新设备", deviceNumber, 0, 0);
					}
					// mSwdb.Close();
					// Intent mintent = new Intent();
					// mintent.setClass(AddDeviceActivity.this,
					// MainActivity.class);
					// startActivity(mintent);
					finish();
				} else if (message.equals("check_fail")) {
					checkText.setVisibility(LinearLayout.VISIBLE);
				}
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
		setContentView(R.layout.add_device_view);
		TAG = this;
		// editText = (EditText) findViewById(R.id.cha_editText1_id);
		// btn = (Button) findViewById(R.id.cha_hostBtn_id);
		addset = (Button) findViewById(R.id.add_device_xiala_id);
		back = (Button) findViewById(R.id.add_device_back_id);
		layout = (LinearLayout) findViewById(R.id.cha_Linear_id2);
		checkText = (TextView) findViewById(R.id.check_text_id);
		addset.setOnClickListener(this);
		back.setOnClickListener(this);
		layout.setOnClickListener(this);
		// mDeviceDB = new DeviceDB(this);
		// mDeviceDB.Open();
		mSwdb = new SwDB(this);
		swFun = new SwdbFun();
		// mSwdb.Open();

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		mReciver = new MessageBackReciver();
		// mServiceIntent = new Intent(this, BackService.class);
		mReciver.isInitialStickyBroadcast();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BCH_CONSTANT.ACTION);

		morePopWindow = new MorePopWindow(AddDeviceActivity.this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// System.out.println("我是onResume");
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		morePopWindow.showDismiss();
		// layout.setVisibility(LinearLayout.GONE);
		checkText.setVisibility(LinearLayout.GONE);
	}

	protected void onPause() {
		super.onPause();
		// System.out.println("我是onPause");
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getSharedPreferences("loginInfo",
				this.MODE_PRIVATE);
		String username = sharedPreferences.getString("userName", "");
		if (v.equals(layout)) {
			MessagePacket packet = new MessagePacket("binding", username, null,
					deviceNumber, null);
			boolean isSend = SendService.sendData(packet);
			// boolean isSend = sendToSevice("binding", username, null,
			// deviceNumber, null);
			if (!isSend) {
				Toast.makeText(this, "未连接网络,请检查...", 2000).show();
			}
		}

		if (v.equals(addset)) {
			morePopWindow.showPopupWindow(addset);
		}
		if (v.equals(back)) {
			finish();
		}

	}

//	public synchronized boolean sendToSevice(String type, String username,
//			String password, String devicenumber, String content) {
//		boolean flag = false;
//		if (ClientHandler.session == null) {
//			return false;
//		}
//		try {
//			// PrintWriter writer = new PrintWriter(
//			// TcpService.socket.getOutputStream(), true);
//			PacketService service = new PacketService();
//			String msg = FastJsonTools.createFastJsonString(service.setPacket(
//					type, username, password, devicenumber, content));
//			// out.println(msg);
//			if (!ClientHandler.session.isClosing()
//					&& ClientHandler.session.isConnected()) {
//				ClientHandler.session.write(msg);
//				flag = true;
//			} else {
//				flag = false;
//			}
//		} catch (Exception e) {
//			flag = false;
//			System.out.println(e.toString());
//		}
//		return flag;
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// mDeviceDB.Close();
		// mSwdb.Close();

	}

}
