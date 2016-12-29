package com.nowar.activity;

import java.io.IOException;
import java.io.OutputStream;

import com.nowar.broadcast.BROADCAST_CONSTANT.BLUETOOTH;
import com.nowar.broadcast.BROADCAST_CONSTANT.NETWORK;
import com.nowar.main.BaseActivity;
import com.nowar.main.R;
//import com.nowar.bluetooth.BtClientListener;

import com.nowar.mina.ClientHandler;
import com.lindec.app.tools.BluetoothUtil;
import com.lindec.app.tools.DateUitl;
import com.lindec.app.tools.PrintUtil;
import com.nowar.service.ConnectorService;
import com.nowar.sharedprefs.SharedPrefsUtil;
import com.nowar.activity.MyActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.style.BulletSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class OpenActivity extends BaseActivity implements OnClickListener {

	private ImageView mImaga;
	public static final String SEND = "SEND";
	private OutputStream out;// 输出流，
	private MessageBackReciver mReciver;
	private IntentFilter mIntentFilter;
	private LocalBroadcastManager mLocalBroadcastManager;
	private boolean isOpen = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crl);
		PrintUtil.getPrintUtil().println(DateUitl.getMSMDate());
		mImaga = (ImageView) findViewById(R.id.open_btn_id);
		mImaga.setOnClickListener(this);
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BLUETOOTH.MSG);
		readDoor();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BluetoothUtil.getBluetoothUtil().btDisconnect(MyActivity._socket);
		MyActivity._socket = null;
	}

	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {

			if (openDoor()) {
				PlayMusic(R.raw.dingdong);
			} else {
				Toast.makeText(OpenActivity.this, "门锁未连接！", Toast.LENGTH_LONG)
						.show();
			}

		}
	};

	private boolean openDoor() {
		if (!sendData("open\n"))
			return false;
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// if (!sendData("open\n"))
		// return false;

		return true;

	}

	private boolean closeDoor() {
		if (!sendData("close\n"))
			return false;
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // if (!sendData("close\n"))
		// // return false;

		return true;

	}

	private boolean readDoor() {
		if (!sendData("read\n"))
			return false;
		return true;

	}

	private boolean sendData(String msg) {
		// TODO Auto-generated method stub
		if (MyActivity._socket.isConnected()) {
			try {
				out = MyActivity._socket.getOutputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			try {
				out.write(msg.getBytes());
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 播放音乐，参数是资源id
	 * 
	 * @param MusicId
	 */
	private void PlayMusic(int MusicId) {
		// System.out.println("------声音-------");
		MediaPlayer music = MediaPlayer.create(this, MusicId);
		music.start();
	}

	private class MessageBackReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BLUETOOTH.MSG)) {
				String msg = intent.getStringExtra("msg");
				msg = msg.replace("\n", "");
				PrintUtil.getPrintUtil().println(msg);
				if (msg.equals("readopen")) {
					isOpen = true;
					mImaga.setImageResource(R.drawable.lightbulb_on);
				} else if (msg.equals("readclose")) {
					isOpen = false;
					mImaga.setImageResource(R.drawable.lightbulb_off);
				} else if (msg.equals("openok")) {
					isOpen = true;
					mImaga.setImageResource(R.drawable.lightbulb_on);
				} else if (msg.equals("closeok")) {
					isOpen = false;
					mImaga.setImageResource(R.drawable.lightbulb_off);
				}
			} else {

			}
		};
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mImaga)) {
			if (isOpen) {
				closeDoor();
			} else {
				openDoor();
			}
			// Message msg = Message.obtain();
			// handle.sendMessage(msg);
		}

	}
}
