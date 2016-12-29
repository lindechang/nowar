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
	// private BluetoothDevice _device = null; // �����豸
	private Context context;


	public static Set<BtDevice> setNewDevice = new HashSet<BtDevice>();
	public static Set<BtDevice> setBondDevice = new HashSet<BtDevice>();
	// ʹ��LocalBroadcastManager�����ºô���
	// ���͵Ĺ㲥ֻ�����Լ�App�ڴ���������й¶������App��ȷ����˽���ݲ���й¶
	// ����AppҲ�޷������App���͸ù㲥�����õ�������App�������ƻ�
	// ��ϵͳȫ�ֹ㲥���Ӹ�Ч
	// ͨ��mLocalBroadcastManagerע����� �޷�����ϵͳ���Ĺ㲥
	private static LocalBroadcastManager mLocalBroadcastManager;

	public BluetoothBroadcast(Context context) {
		this.context = context;
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
		// // ע����ղ��ҵ��豸action������
		// IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// context.registerReceiver(mReceiver, filter);
		IntentFilter filter = new IntentFilter();
		// ע����ղ��ҵ��豸action������
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		// ע������action������
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		// ע�����ӶϿ�action������
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		// UUID
		filter.addAction(BluetoothDevice.ACTION_UUID);
		// ע����ҽ���action������
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		// ע�᱾����������״̬action������
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		context.registerReceiver(mReceiver, filter);
		// ��ע���޷�����ϵͳ���Ĺ㲥
		// mLocalBroadcastManager.registerReceiver(mReceiver, filter);
	}

	private void sendLocalBroadcast(Intent intent) {
		mLocalBroadcastManager.sendBroadcast(intent);
	}

	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			 System.out.println("�鵽�豸");
			 
			 
			 
			// if (!dbFun.CheckSwDevice(db, device.getAddress())) {
			// dbFun.InsertDevice(db, device.getName(), device.getAddress(),
			// 0, 0);
			// Intent mintent = new Intent(BLUETOOTH.SEARCH);
			// sendLocalBroadcast(mintent);
			// }

		}
	};

	/**
	 * �����豸������
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// ���ҵ��豸action
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Intent mIntent = new
				// Intent(BROADCAST_CONSTANT.BLUETOOTH.ENABLE);
				// mIntent.putExtra("broadcastKey", "abaa");
				// sendLocalBroadcast(mIntent);
				// �õ������豸
				System.out.println("����");
				Message msg = Message.obtain();
				handle.sendMessage(msg);
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// ���������Ե����Թ����ѵõ���ʾ�����������ӵ��б��н�����ʾ
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// System.out.println("δ����豸");
					// System.out.println("name:" + device.getName());
					// System.out.println("address:" + device.getAddress());
					BtDevice newDevice = new BtDevice();
					newDevice.setSwdeviceName(device.getName());
					newDevice.setSwdeviceNumber(device.getAddress());
					setNewDevice.add(newDevice);
					Intent mintent = new Intent(BLUETOOTH.SEARCH_NEWDEVICE);
					sendLocalBroadcast(mintent);
				} else { // ��ӵ�������豸�б�
					// System.out.println("������豸");
					// System.out.println("name:" + device.getName());
					// System.out.println("address:" + device.getAddress());
					BtDevice newDevice = new BtDevice();
					newDevice.setSwdeviceName(device.getName());
					newDevice.setSwdeviceNumber(device.getAddress());
					setBondDevice.add(newDevice);
					Intent mintent = new Intent(BLUETOOTH.SEARCH_BONDDEVICE);
					sendLocalBroadcast(mintent);
				}
				// �������action
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				 System.out.println("�������!!!");
			} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				// Intent mintent = new Intent(BLUETOOTH.CONNECTED);
				// sendLocalBroadcast(mintent);
				// Toast.makeText(context, "���ӳɹ���", Toast.LENGTH_LONG).show();
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				// try {
				// Intent mintent = new Intent(BLUETOOTH.DISCONNECTED);
				// sendLocalBroadcast(mintent);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// Toast.makeText(context, "�Ͽ����ӣ�", Toast.LENGTH_LONG).show();
			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				// System.out.println("������������״̬�ı䣡");
				BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();;
				switch (mBtAdapter.getState()) {
				case BluetoothAdapter.STATE_ON:
					//ÿ�������������Զ�ɨ��һ��
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
