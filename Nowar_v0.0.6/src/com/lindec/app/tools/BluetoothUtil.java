package com.lindec.app.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;

/**
 * @author lindec
 * @Create 2015/10/28
 * @last 2015/10/28
 * @version 1.0
 * @annotation 蓝牙工具类
 */
public class BluetoothUtil {

	// SPP服务UUID号
	// Android 与 单片机 设备连接 UUID
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private Context context;
	private static BluetoothUtil mBluetoothUtil;

	// public BluetoothUtil(Context context) {
	// this.context = context;
	// // 得到本地蓝牙句柄
	// BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	// }

	public static BluetoothUtil getBluetoothUtil() {
		if (mBluetoothUtil == null) {
			mBluetoothUtil = new BluetoothUtil();
		}
		return mBluetoothUtil;
	}

	// public void ge(BluetoothAdapter mBtAdapter) {
	// mBtAdapter.get
	// }

	/**
	 * 请求打开蓝牙
	 * 
	 * @param mBtAdapter
	 */
	public void btRequetEnable(Activity activity, BluetoothAdapter mBtAdapter) {
		if (null != mBtAdapter) {
			if (!mBtAdapter.isEnabled()) {
				// 弹出对话框提示用户是后打开
				Intent intent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(intent, 0);
			}
		}
	}

	/**
	 * 开启蓝牙
	 * 
	 * @param mBtAdapter
	 *            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	 * @return
	 */
	public boolean btEnable(BluetoothAdapter mBtAdapter) {
		boolean isEnalble = false;
		if (null != mBtAdapter) {
			if (!mBtAdapter.isEnabled()) {
				mBtAdapter.enable();
			}
			isEnalble = true;
		} else {
			// 如果打开本地蓝牙设备不成功，提示信息，结束程序;
			isEnalble = false;
		}
		return isEnalble;
	}

	/**
	 * 关闭蓝牙
	 * 
	 * @param mBtAdapter
	 *            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	 * @return
	 */
	public boolean btDisable(BluetoothAdapter mBtAdapter) {
		boolean isDisalble = false;
		if (null != mBtAdapter) {
			if (mBtAdapter.isEnabled()) {
				mBtAdapter.disable();
			}
			isDisalble = true;
		} else {
			isDisalble = false;
		}
		return isDisalble;
	}

	/**
	 * 搜索蓝牙设备
	 * 
	 * @param mBtAdapter
	 *            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	 * @return
	 */
	public void btSearch(BluetoothAdapter mBtAdapter) {

		// 关闭再进行的服务查找
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// 并重新开始
		mBtAdapter.startDiscovery();
	}

	/**
	 * 停止搜索蓝牙设备
	 * 
	 * @param mBtAdapter
	 *            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	 * @return
	 */
	public void btStopSearch(BluetoothAdapter mBtAdapter) {
		// 关闭再进行的服务查找
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
	}

	/**
	 * 获取所有已配对的蓝牙设备, 一定确保蓝牙是已经开启的，否则无返回
	 * 
	 * @param mBtAdapter
	 */
	public Set<BluetoothDevice> getAllPairedDevice(BluetoothAdapter mBtAdapter) {

		Set<BluetoothDevice> allPairedDevice = new HashSet<BluetoothDevice>();
		// 关闭再进行的服务查找
		if (null != mBtAdapter) {
			if (mBtAdapter.isEnabled()) {
				allPairedDevice = mBtAdapter.getBondedDevices();
			}
		}
		return allPairedDevice;
	}

	/**
	 * 输出绑定的蓝牙设备
	 * 
	 * @param mBtAdapter
	 */
	public void outAllPairedDevice(BluetoothAdapter mBtAdapter) {
		Set<BluetoothDevice> all = new HashSet<BluetoothDevice>();
		try {
			all = getAllPairedDevice(mBtAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (all != null) {
			Iterator<BluetoothDevice> it = all.iterator();

			while (it.hasNext()) {
				BluetoothDevice device = (BluetoothDevice) it.next();
				ParcelUuid[] uuid = device.getUuids();
				// ParcelUuid[] 好像是为了安全，没有输出
				PrintUtil.getPrintUtil().println(
						"address:" + device.getAddress() + "--uuid:"
								+ uuid.toString());
				// PrintUtil.getPrintUtil().println(
				// "BluetoothDevice:" + device.getAddress());
			}
		} else {
			PrintUtil.getPrintUtil().println("BluetoothDevice:" + null);
		}

	}

	/**
	 * 根据地址链接蓝牙设备 ,并返回Socket
	 * 
	 * @param mBtAdapter
	 * @param address
	 *            远程蓝牙地址
	 * @return
	 */
	public static BluetoothSocket btConnect(BluetoothAdapter mBtAdapter,
			String address) {
		BluetoothSocket _socket = null;
		BluetoothDevice _device = mBtAdapter.getRemoteDevice(address);
		//链接前关闭搜索
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// 用服务号得到socket 配对
		try {
			_socket = _device.createRfcommSocketToServiceRecord(UUID
					.fromString(MY_UUID));
		} catch (IOException e) {
			_socket = null;
			return _socket;
		}
		try {
			_socket.connect();
		} catch (IOException e) {
			_socket = null;
		}
		return _socket;
	}

	/**
	 * 断开已连接的蓝牙设备
	 * 
	 * @param socket
	 * @return
	 */
	public boolean btDisconnect(BluetoothSocket socket) {
		boolean isDis = false;
		if (socket != null) {
			if (socket.isConnected()) {
				try {
					socket.close();
					isDis = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isDis;
	}

}
