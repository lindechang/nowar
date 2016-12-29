package weilan.app.activity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import weilan.app.db.SwDB;
import weilan.app.db.SwDevice;
import weilan.app.db.SwdbFun;
import weilan.app.main.R;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.mina.ClientHandler;
import weilan.app.tools.mina.SendService;
import weilan.app.ui.CustomDialog;
//import yundian.tracker.tcp.TcpService;
//import yundian.tracker.tcp.TcpThread;
//import yundian.tracker.tcp.ThreadParam;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DialerFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SwOperateActivity extends Activity implements OnClickListener {
	private int test = 0;
	private CustomDialog Dialog;
	private String command = null;
	private SwDB mSwDB;
	private SwdbFun swFun;
	private SwDevice swDe;
	// private Cursor mCursor;
	private ImageView iMage;

	private ExecutorService es;
	private Future<String> future;

	// private int ID;
	// private String NAME;
	// private String deviceNumber;
	// private int SW;
	// private int BANGDING;

	private int position;
	private Map<String, String> commandMap;

	// private ProgressDialog dialog;

	private OutputStream out;

	private Handler dialoghandler = new Handler();
	private Runnable dialogRunnable = new Runnable() {
		public void run() {
			if (Dialog.isShowing()) {
				Dialog.dismiss();
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
				Dialog.dismiss();
				String message = intent.getStringExtra("message");
				System.out.println("-接收到广播来的message-->>" + message);
				// mSwDB.Open();
				if (message.equals("open_success")) {
					iMage.setImageResource(R.drawable.sw_button_open_big);
					try {
						// mSwDB.Update(ID, NAME, deviceNumber, 1, BANGDING);
						swFun.Update(mSwDB, swDe.getSwdeviceNumber(),
								SwDB.SWDEVICE_SELECT, 1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (message.equals("close_success")) {
					iMage.setImageResource(R.drawable.sw_button_close_big);
					try {
						swFun.Update(mSwDB, swDe.getSwdeviceNumber(),
								SwDB.SWDEVICE_SELECT, 0);
						// mSwDB.Update(ID, NAME, deviceNumber, 0, BANGDING);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (message.equals("device_disconnection")) {
					Toast.makeText(SwOperateActivity.this, "设备掉线，请稍后操作...",
							2000).show();
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
		setContentView(R.layout.sw_operate_view);
		initView();
	}

	private void initView() {

		Dialog = new CustomDialog(this, R.layout.dialog_layout,
				R.style.DialogTheme);
		// dialog = new ProgressDialog(this);
		// dialog.setTitle("提示信息");
		// dialog.setMessage("控制中，请稍后....");
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		// mServiceIntent = new Intent(this, BackService.class);
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BCH_CONSTANT.ACTION);
		mSwDB = new SwDB(this);
		swFun = new SwdbFun();
		swDe = new SwDevice();
		// mSwDB.Open();
		// mCursor = mSwDB.SelectSW();

		iMage = (ImageView) findViewById(R.id.sw_btn_id);
		iMage.setOnClickListener(this);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = bundle.getInt("position");
		swDe = swFun.getSingleSW(mSwDB, position);
		// mCursor.moveToPosition(position);
		// ID = mCursor.getInt(0);
		// NAME = mCursor.getString(1);
		// deviceNumber = mCursor.getString(2);
		// SW = mCursor.getInt(3);
		// BANGDING = mCursor.getInt(4);
		// commandMap = new HashMap<String, String>();

		if (swDe.getSwdeviceSelect() == 1) {
			iMage.setImageResource(R.drawable.sw_button_open_big);

		} else if (swDe.getSwdeviceSelect() == 0) {
			iMage.setImageResource(R.drawable.sw_button_close_big);
		}
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

	public void onClick(View v) {
		boolean isSend = false;
		if (v.getId() == R.id.sw_btn_id) {
			swDe = swFun.getSingleSW(mSwDB, position);
			if (swDe.getSwdeviceSelect() == 1) {
				MessagePacket packet = new MessagePacket("sw", null, null,
						swDe.getSwdeviceNumber(), "0");
				isSend = SendService.sendData(packet);
				// isSend = sendToSevice("sw", null, null,
				// swDe.getSwdeviceNumber(), "0");
				if (isSend) {
					// dialog.show();
					Dialog.show();
				} else {
					Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				}

			} else if (swDe.getSwdeviceSelect() == 0) {
				MessagePacket packet = new MessagePacket("sw", null, null,
						swDe.getSwdeviceNumber(), "1");
				isSend = SendService.sendData(packet);
				// isSend = sendToSevice("sw", null, null,
				// swDe.getSwdeviceNumber(), "1");
				if (isSend) {
					Dialog.show();
				} else {
					Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				}
			}
			if (Dialog.isShowing()) {
				dialoghandler.postDelayed(dialogRunnable, 10 * 1000);
			}
		}
		// if (v.getId() == R.id.sw_btn_id) {
		// if (test % 2 == 0) {
		// iMage.setImageResource(R.drawable.sw_button_open_big);
		// } else {
		// iMage.setImageResource(R.drawable.sw_button_close_big);
		// }
		// test++;
		// }

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
