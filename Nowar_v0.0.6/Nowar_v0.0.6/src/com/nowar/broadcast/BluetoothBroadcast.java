package com.nowar.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.lindec.app.tools.PrintUtil;
import com.nowar.broadcast.BROADCAST_CONSTANT.BLUETOOTH;
import com.nowar.db.BtDB;
import com.nowar.db.BtDevice;
import com.nowar.db.BtdbFun;
import com.nowar.service.ConnectorService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class BluetoothBroadcast {

	private BluetoothDevice device;
	// private BluetoothAdapter mBtAdapter;
	// private BluetoothDevice _device = null; // 蓝牙设备
	private Context context;


	public static Set<BtDevice> setNewDevice = new HashSet<BtDevice>();
	public static Set<BtDevice> setBondDevice = new HashSet<BtDevice>();
	// 使用LocalBroadcastManager有如下好处：
	// 发送的广播只会在自己App内传播，不会泄露给其他App，确保隐私数据不会泄露
	// 其他App也无法向你的App发送该广播，不用担心其他App会来搞破坏
	// 比系统全局广播更加高效
	// 通过mLocalBroadcastManager注册接收 无法接收系统来的广播
	private static LocalBroadcastManager mLocalBroadcastManager;

	public BluetoothBroadcast(Context context) {
		this.context = context;
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
		// // 注册接收查找到设备action接收器
		// IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// context.registerReceiver(mReceiver, filter);
		IntentFilter filter = new IntentFilter();
		// 注册接收查找到设备action接收器
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		// 注册链接action接收器
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		// 注册链接断开action接收器
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		// UUID
		filter.addAction(BluetoothDevice.ACTION_UUID);
		// 注册查找结束action接收器
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		// 注册本地蓝牙开关状态action接收器
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		context.registerReceiver(mReceiver, filter);
		// 此注册无法接收系统来的广播
		// mLocalBroadcastManager.registerReceiver(mReceiver, filter);
	}

	private void sendLocalBroadcast(Intent intent) {
		mLocalBroadcastManager.sendBroadcast(intent);
	}

	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			 System.out.println("查到设备");
			 
			 
			 
			// if (!dbFun.CheckSwDevice(db, device.getAddress())) {
			// dbFun.InsertDevice(db, device.getName(), device.getAddress(),
			// 0, 0);
			// Intent mintent = new Intent(BLUETOOTH.SEARCH);
			// sendLocalBroadcast(mintent);
			// }

		}
	};

	/**
	 * 蓝牙设备监听器
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 查找到设备action
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Intent mIntent = new
				// Intent(BROADCAST_CONSTANT.BLUETOOTH.ENABLE);
				// mIntent.putExtra("broadcastKey", "abaa");
				// sendLocalBroadcast(mIntent);
				// 得到蓝牙设备
				System.out.println("进啦");
				Message msg = Message.obtain();
				handle.sendMessage(msg);
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 如果是已配对的则略过，已得到显示，其余的在添加到列表中进行显示
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// System.out.println("未配对设备");
					// System.out.println("name:" + device.getName());
					// System.out.println("address:" + device.getAddress());
					BtDevice newDevice = new BtDevice();
					newDevice.setSwdeviceName(device.getName());
					newDevice.setSwdeviceNumber(device.getAddress());
					setNewDevice.add(newDevice);
					Intent mintent = new Intent(BLUETOOTH.SEARCH_NEWDEVICE);
					sendLocalBroadcast(mintent);
				} else { // 添加到已配对设备列表
					// System.out.println("已配对设备");
					// System.out.println("name:" + device.getName());
					// System.out.println("address:" + device.getAddress());
					BtDevice newDevice = new BtDevice();
					newDevice.setSwdeviceName(device.getName());
					newDevice.setSwdeviceNumber(device.getAddress());
					setBondDevice.add(newDevice);
					Intent mintent = new Intent(BLUETOOTH.SEARCH_BONDDEVICE);
					sendLocalBroadcast(mintent);
				}
				// 搜索完成action
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				 System.out.println("搜索完成!!!");
			} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				// Intent mintent = new Intent(BLUETOOTH.CONNECTED);
				// sendLocalBroadcast(mintent);
				// Toast.makeText(context, "链接成功！", Toast.LENGTH_LONG).show();
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				// try {
				// Intent mintent = new Intent(BLUETOOTH.DISCONNECTED);
				// sendLocalBroadcast(mintent);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// Toast.makeText(context, "断开链接！", Toast.LENGTH_LONG).show();
			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				// System.out.println("本地蓝牙开关状态改变！");
				BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();;
				switch (mBtAdapter.getState()) {
				case BluetoothAdapter.STATE_ON:
					//每次蓝牙开启后，自动扫描一次
					mBtAdapter.startDiscovery();
					break;
				case BluetoothAdapter.STATE_OFF:
					break;
				default:
					break;
				}
			} else if (BluetoothDevice.ACTION_UUID.equals(action)) {
				//ParcelUuid uuid = new ParcelUuid();
				//System.out.println("uuid");
			}
		}
	};

}
