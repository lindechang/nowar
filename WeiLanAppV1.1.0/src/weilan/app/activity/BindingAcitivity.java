package weilan.app.activity;

import com.zbar.lib.CaptureActivity;

import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.db.SwDB;
import weilan.app.db.SwdbFun;
import weilan.app.main.R;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.mina.ClientHandler;
import weilan.app.tools.mina.SendService;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BindingAcitivity extends Activity implements OnClickListener {

	private Button sendBin;
	private TextView text;
	private TextView name;
	private ImageView img;
	private String type;
	private String goal;
	private SwDB mSwdb;
	private SwdbFun swFun;
	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	private class MessageBackReciver extends BroadcastReceiver {
		// private WeakReference<TextView> textView;

		public MessageBackReciver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BCH_CONSTANT.ACTION)) {
				String message = intent.getStringExtra("message");
				System.out.println("message:" + message);
				if (type.equals("friend")) {
					if (message.equals("request_success")) {
						Toast.makeText(BindingAcitivity.this, "请求成功",
								Toast.LENGTH_SHORT).show();
						// if (!swFun.CheckSwDevice(mSwdb, goal)) {
						// swFun.InsertDevice(mSwdb, "新设备", goal, 0, 0);
						// } else {
						// Toast.makeText(BindingAcitivity.this, "设备已绑定",
						// Toast.LENGTH_SHORT).show();
						// }
						activityFinish();

					} else if (message.equals("request_fail")) {
						Toast.makeText(BindingAcitivity.this, "请求失败",
								Toast.LENGTH_SHORT).show();
					}

				} else if (type.equals("device")) {
					if (message.equals("binding_success")) {
						if (!swFun.CheckSwDevice(mSwdb, goal)) {
							swFun.InsertDevice(mSwdb, "新设备", goal, 0, 0);
						} else {
							Toast.makeText(BindingAcitivity.this, "设备已绑定",
									Toast.LENGTH_SHORT).show();
						}
						activityFinish();

					} else if (message.equals("binding_fail")) {
						Toast.makeText(BindingAcitivity.this, "绑定失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {

			}
		};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.binding_view);
		mSwdb = new SwDB(this);
		swFun = new SwdbFun();
		Intent intert = getIntent();
		type = intert.getStringExtra("type");

		img = (ImageView) findViewById(R.id.bingding_view_imageView1_id);
		sendBin = (Button) findViewById(R.id.binding_sendBtn_id);
		name = (TextView) findViewById(R.id.bing_name_tt_id);
		text = (TextView) findViewById(R.id.bing_number_tt_id);

		if (type.equals("friend")) {
			goal = intert.getStringExtra("friendNumber");
			img.setImageResource(R.drawable.abaose);
			name.setText("新好友");
		} else {
			goal = intert.getStringExtra("deviceNumber");
			img.setImageResource(R.drawable.sw_button_open);
			name.setText("新设备");
		}

		text.setText(goal);
		sendBin.setOnClickListener(this);

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		mReciver.isInitialStickyBroadcast();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BCH_CONSTANT.ACTION);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String username = SharedPrefsUtil.getValue(this, "userName", "");
		if (v.equals(sendBin)) {
			if (type.equals("friend")) {
				MessagePacket packet = new MessagePacket("add_newfriend",
						username, null, goal, null);
				boolean isSend = SendService.sendData(packet);
				// boolean isSend = sendToSevice("binding", username, null,
				// number,
				// null);
				if (!isSend) {
					Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				}
			} else {
				MessagePacket packet = new MessagePacket("binding", username,
						null, goal, null);
				boolean isSend = SendService.sendData(packet);
				// boolean isSend = sendToSevice("binding", username, null,
				// number,
				// null);
				if (!isSend) {
					Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				}
			}

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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
	}

	protected void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
		// mSwdb.Close();
	}

	private void activityFinish() {
		if (CaptureActivity.TAG != null) {
			if (!CaptureActivity.TAG.isFinishing()) {
				CaptureActivity.TAG.finish();
			}
		}
		// if (AddDeviceActivity.TAG != null) {
		// if (!AddDeviceActivity.TAG.isFinishing()) {
		// AddDeviceActivity.TAG.finish();
		// }
		// }
		if (SearchActivity.TAG != null) {
			if (!SearchActivity.TAG.isFinishing()) {
				SearchActivity.TAG.finish();
			}
		}
		finish();
	}

}
