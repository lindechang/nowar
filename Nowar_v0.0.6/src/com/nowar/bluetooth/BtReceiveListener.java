package com.nowar.bluetooth;

import java.io.IOException;
import java.io.InputStream;

import com.lindec.app.tools.PrintUtil;
import com.nowar.activity.LoginActivity;
//import com.nowar.activity.MainActivity;
import com.nowar.broadcast.BROADCAST_CONSTANT.BLUETOOTH;
import com.nowar.broadcast.BROADCAST_CONSTANT.NETWORK;
import com.nowar.mina.ClientHandler;
import com.nowar.sharedprefs.SharedPrefsUtil;
import com.nowar.activity.MyActivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BtReceiveListener extends Thread {

	private MessageBackReciver mReciver;
	private IntentFilter mIntentFilter;
	private LocalBroadcastManager mLocalBroadcastManager;
	private InputStream is = null; // 输入流，用来接收蓝牙数据

	private String smsg = ""; // 显示用数据缓存
	private String fmsg = ""; // 保存用数据缓存
	boolean bRun = true;

	public BtReceiveListener(Context context) {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
		mReciver = new MessageBackReciver();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BLUETOOTH.CONNECTED);
		mIntentFilter.addAction(BLUETOOTH.DISCONNECTED);
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		// mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	private void sendLocalBroadcast(Intent intent) {
		mLocalBroadcastManager.sendBroadcast(intent);
	}

	@Override
	public void run() {

		int num = 0;
		byte[] buffer = new byte[1024];
		byte[] buffer_new = new byte[1024];
		int i = 0;
		int n = 0;
		bRun = true;
		// 接收线程
		while (true) {
			try {
				while (true) {
					if (null != MyActivity._socket) {
						if (MyActivity._socket.isConnected()) {
							is = MyActivity._socket.getInputStream();
							if (is.available() != 0)
								break;
						}
					}
				}
				Thread.sleep(100);
				while (true) {
					num = is.read(buffer); // 读入数据
					n = 0;

					String s0 = new String(buffer, 0, num);
					fmsg += s0; // 保存收到数据
					for (i = 0; i < num; i++) {
						if ((buffer[i] == 0x0d) && (buffer[i + 1] == 0x0a)) {
							buffer_new[n] = 0x0a;
							i++;
						} else {
							buffer_new[n] = buffer[i];
						}
						n++;
					}
					String s = new String(buffer_new, 0, n);
					smsg += s; // 写入接收缓存
					if (is.available() == 0)
						break; // 短时间没有数据才跳出进行显示

				}
				handler.sendMessage(handler.obtainMessage());
			} catch (Exception e) {
				e.printStackTrace();
				//PrintUtil.getPrintUtil().println("Run出错");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		}
	}

	// 消息处理队列
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent(BLUETOOTH.MSG);
			intent.putExtra("msg", smsg);
			// PrintUtil.getPrintUtil().println(smsg.length());
			// String tmep = smsg.replace("\n", "");
			// PrintUtil.getPrintUtil().println(tmep.length());
			
			sendLocalBroadcast(intent);
			smsg = "";
		}
	};

	private class MessageBackReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BLUETOOTH.CONNECTED)) {
				// 打开接收线程
				try {
					is = MyActivity._socket.getInputStream(); // 得到蓝牙数据输入流
					bRun = true;
				} catch (Exception e) {
					try {
						is.close();
						is = null;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			} else if (action.equals(BLUETOOTH.DISCONNECTED)) {
				try {
					is.close();
					is = null;
					bRun = false;
				} catch (Exception e) {
				}
			}
		};
	}

}
