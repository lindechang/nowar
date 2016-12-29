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
 * @annotation ����������
 */
public class BluetoothUtil {

	// SPP����UUID��
	// Android �� ��Ƭ�� �豸���� UUID
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private Context context;
	private static BluetoothUtil mBluetoothUtil;

	// public BluetoothUtil(Context context) {
	// this.context = context;
	// // �õ������������
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
	 * ���������
	 * 
	 * @param mBtAdapter
	 */
	public void btRequetEnable(Activity activity, BluetoothAdapter mBtAdapter) {
		if (null != mBtAdapter) {
			if (!mBtAdapter.isEnabled()) {
				// �����Ի�����ʾ�û��Ǻ��
				Intent intent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(intent, 0);
			}
		}
	}

	/**
	 * ��������
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
			// ����򿪱��������豸���ɹ�����ʾ��Ϣ����������;
			isEnalble = false;
		}
		return isEnalble;
	}

	/**
	 * �ر�����
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
	 * ���������豸
	 * 
	 * @param mBtAdapter
	 *            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	 * @return
	 */
	public void btSearch(BluetoothAdapter mBtAdapter) {

		// �ر��ٽ��еķ������
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// �����¿�ʼ
		mBtAdapter.startDiscovery();
	}

	/**
	 * ֹͣ���������豸
	 * 
	 * @param mBtAdapter
	 *            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	 * @return
	 */
	public void btStopSearch(BluetoothAdapter mBtAdapter) {
		// �ر��ٽ��еķ������
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
	}

	/**
	 * ��ȡ��������Ե������豸, һ��ȷ���������Ѿ������ģ������޷���
	 * 
	 * @param mBtAdapter
	 */
	public Set<BluetoothDevice> getAllPairedDevice(BluetoothAdapter mBtAdapter) {

		Set<BluetoothDevice> allPairedDevice = new HashSet<BluetoothDevice>();
		// �ر��ٽ��еķ������
		if (null != mBtAdapter) {
			if (mBtAdapter.isEnabled()) {
				allPairedDevice = mBtAdapter.getBondedDevices();
			}
		}
		return allPairedDevice;
	}

	/**
	 * ����󶨵������豸
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
				// ParcelUuid[] ������Ϊ�˰�ȫ��û�����
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
	 * ���ݵ�ַ���������豸 ,������Socket
	 * 
	 * @param mBtAdapter
	 * @param address
	 *            Զ��������ַ
	 * @return
	 */
	public static BluetoothSocket btConnect(BluetoothAdapter mBtAdapter,
			String address) {
		BluetoothSocket _socket = null;
		BluetoothDevice _device = mBtAdapter.getRemoteDevice(address);
		//����ǰ�ر�����
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// �÷���ŵõ�socket ���
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
	 * �Ͽ������ӵ������豸
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
