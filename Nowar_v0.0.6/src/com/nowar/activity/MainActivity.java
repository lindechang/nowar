package com.nowar.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.nowar.main.AppManager;
import com.nowar.main.BaseActivity;
import com.nowar.main.R;
import com.lindec.app.tools.BitmapUtil;
import com.lindec.app.tools.BluetoothUtil;
import com.lindec.app.tools.DateUitl;
import com.lindec.app.tools.PrintUtil;
import com.lindec.app.tools.VersionUtils;
import com.nowar.adapter.BluetoothDeviceAdapter;
import com.nowar.adapter.BluetoothDeviceItem;
import com.nowar.broadcast.BROADCAST_CONSTANT.BLUETOOTH;
import com.nowar.broadcast.BROADCAST_CONSTANT.NETWORK;
import com.nowar.db.BtDB;
import com.nowar.db.BtDevice;
import com.nowar.db.BtdbFun;
import com.nowar.menu.SlidingMenu;
import com.nowar.mina.SendService;
import com.nowar.packet.MessagePacket;
import com.nowar.service.ConnectorService;
import com.nowar.sharedprefs.SharedPrefsUtil;
import com.nowar.tools.CircleTransformation;
import com.nowar.version.UpdateVersions;
import com.squareup.picasso.Picasso;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnItemClickListener,
		OnClickListener {
	public static BluetoothSocket _socket = null;

	private static final int REFRESH_COMPLETE = 0X110;
	private Toast mToast;
	private ListView mListView;
	// private List list;
	// public ArrayAdapter<String> DevicesArrayAdapter;
	private ArrayList<BtDevice> allTicketsList = new ArrayList<BtDevice>();

	// private MyAdapter myAdapter;
	private BluetoothDeviceAdapter mBluetoothDeviceAdapter;
	private BtDB db;
	private BtdbFun dbFun;
	private BtDevice btDevice;
	private List<BluetoothDeviceItem> mList;
	
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	private ImageView mRefreshImag;

	private SlidingMenu mMenu;

	private BluetoothAdapter mBtAdapter;

	private RelativeLayout set;
	private RelativeLayout feedback;
	private RelativeLayout about;
	private RelativeLayout updataVersion;
	private RelativeLayout exit;
	private ImageView mUserImag;
	private TextView mUserText;
	private Bitmap mBitmap1;
	private Bitmap mBitmap2;
	private String userNumber;
	private String head_path;// 头像路径


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	private void initView() {
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothUtil.getBluetoothUtil().btEnable(mBtAdapter);
		// BluetoothUtil.getBluetoothUtil().btSearch(mBtAdapter);
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		mUserImag = (ImageView) findViewById(R.id.iv_user_head);
		set = (RelativeLayout) findViewById(R.id.menu_relayout_set);
		feedback = (RelativeLayout) findViewById(R.id.menu_relayout_feedback);
		about = (RelativeLayout) findViewById(R.id.menu_relayout_about);
		updataVersion = (RelativeLayout) findViewById(R.id.menu_relayout_updata_version);
		exit = (RelativeLayout) findViewById(R.id.menu_relayout_exit);
		mRefreshImag = (ImageView) findViewById(R.id.menu_del_id);
		mListView = (ListView) findViewById(R.id.menu_list_id);
		mUserText = (TextView) findViewById(R.id.tv_user_name);

		// Picasso.with(this).load(R.drawable.ic_user)
		// .transform(new CircleTransformation()).into(mUserImag);

		mUserImag.setOnClickListener(this);
		set.setOnClickListener(this);
		feedback.setOnClickListener(this);
		about.setOnClickListener(this);
		updataVersion.setOnClickListener(this);
		exit.setOnClickListener(this);
		mRefreshImag.setOnClickListener(this);
		mListView.setOnItemClickListener(this);

		userNumber = SharedPrefsUtil.getSharedPrefsUtil().getValue(this,
				"userNumber", "");

	}

	private void initData() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BLUETOOTH.SEARCH);
		mIntentFilter.addAction(NETWORK.ACTION);

		btDevice = new BtDevice();
		mList = new ArrayList<BluetoothDeviceItem>();
		mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(this, mList);
		mListView.setAdapter(mBluetoothDeviceAdapter);
		db = new BtDB(this);
		dbFun = new BtdbFun();

		// myAdapter = new MyAdapter(this);
		// allTicketsList = dbFun.FetchAll(db);
		// MyAdapter.list.clear();
		// for (int i = 0; i < allTicketsList.size(); i++) {
		// BtDevice bt = new BtDevice();
		// bt = allTicketsList.get(i);
		// MyItem item = new MyItem();
		// item.name = bt.getSwdeviceName();
		// item.number = bt.getSwdeviceNumber();
		// item.time = format.format(new Date());
		// MyAdapter.list.add(item);
		//
		// }
		// // mListView.setAdapter(DevicesArrayAdapter);
		// mListView.setAdapter(myAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mBluetoothDeviceAdapter.notifyDataSetChanged();
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		BluetoothUtil.getBluetoothUtil().btEnable(mBtAdapter);
		// System.out.println("开始扫描");
		// BluetoothUtil.getBluetoothUtil().btSearch(mBtAdapter);

		String userName = SharedPrefsUtil.getSharedPrefsUtil().getValue(this,
				userNumber, "");
		if (userName != null && !userName.equals("")) {
			mUserText.setText(userName);
		}
		head_path = "/sdcard/myHead/" + userNumber + "/";// sd路径
		mBitmap1 = BitmapFactory.decodeFile(head_path + "head.jpg");// 从Sd中找头像，转换成Bitmap
		if (mBitmap1 != null) {
			// Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
			// mUserImag.setImageDrawable(drawable);
			mBitmap2 = BitmapUtil.getRoundedCornerBitmap(mBitmap1,
					(float) (300.0));
			mUserImag.setImageBitmap(mBitmap2);
		} else {
			// 先设默认模式
			mBitmap1 = BitmapUtil.drawableToBitmap(getResources().getDrawable(
					R.drawable.ic_user));
			mBitmap2 = BitmapUtil.getRoundedCornerBitmap(mBitmap1,
					(float) (300.0));
			mUserImag.setImageBitmap(mBitmap2);
			/**
			 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
			 * 
			 */
		}

	}

	protected void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BluetoothUtil.getBluetoothUtil().btDisable(mBtAdapter);
	}

	@Override
	public void finish() {
		// super.finish();
		dbFun.DelAllDevice(db);
		mList.clear();
		BluetoothUtil.getBluetoothUtil().btDisable(mBtAdapter);
		moveTaskToBack(true);// 设置该activity永不过期
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		// intent.putExtra("position", position);
		// System.out.println("position:" + position);
		PrintUtil.getPrintUtil().println(DateUitl.getMSMDate());
		btDevice = allTicketsList.get(position);
		// System.out.println("name:" + bt.getSwdeviceName());
		// System.out.println("number:" + bt.getSwdeviceNumber());
		_socket = BluetoothUtil.btConnect(mBtAdapter,
				btDevice.getSwdeviceNumber());
		if (null != _socket) {
			Intent intent = new Intent(MainActivity.this, OpenActivity.class);
			// intent.setClass(MainActivity.this, OpenActivity.class);
			startActivity(intent);
		} else {
		}

		// Message msg = Message.obtain();
		// handle.sendMessage(msg);
	}

	private class MessageBackReciver extends BroadcastReceiver {
		public MessageBackReciver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BLUETOOTH.SEARCH)) {

				allTicketsList = dbFun.FetchAll(db);
				mList.clear();
				for (int i = 0; i < allTicketsList.size(); i++) {
					BtDevice bt = new BtDevice();
					bt = allTicketsList.get(i);
					BluetoothDeviceItem item = new BluetoothDeviceItem();
					item.name = bt.getSwdeviceName();
					item.number = bt.getSwdeviceNumber();
					item.time = format.format(new Date());
					mList.add(item);
				}
				mListView.setAdapter(mBluetoothDeviceAdapter);
				mBluetoothDeviceAdapter.notifyDataSetChanged();

			} else if (action.equals(NETWORK.ACTION)) {
				String message = intent.getStringExtra("message");
				if (message.equals("old_version")) {
					Toast.makeText(MainActivity.this, "已是最新版本",
							Toast.LENGTH_LONG).show();
				} else if (message.equals("new_version")) {
					UpdateVersions mUpdate = new UpdateVersions(
							MainActivity.this);
					mUpdate.checkUpdateInfo();
				}
			}
		};
	}

	@Override
	public void onClick(View v) {

		if (v.equals(mRefreshImag)) {
			// BluetoothUtil.getBluetoothUtil().outAllPairedDevice(mBtAdapter);
			dbFun.DelAllDevice(db);
			mList.clear();
			mBluetoothDeviceAdapter.notifyDataSetChanged();
			BluetoothUtil.getBluetoothUtil().btRequetEnable(this, mBtAdapter);
			BluetoothUtil.getBluetoothUtil().btSearch(mBtAdapter);
		} else if (v.equals(mUserImag)) {
			Intent intent = new Intent();
			intent.setClass(this, SetHeadActivity.class);
			startActivity(intent);
		} else if (v.equals(about)) {
			Intent intent = new Intent();
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
		} else if (v.equals(set)) {
			Intent intent = new Intent();
			intent.putExtra("userName", mUserText.getText().toString().trim());
			intent.setClass(this, SetUsernameActivity.class);
			startActivity(intent);
		} else if (v.equals(feedback)) {
			Intent intent = new Intent();
			intent.setClass(this, FeedbackActivity.class);
			startActivity(intent);
		} else if (v.equals(updataVersion)) {
			MessagePacket packet = new MessagePacket("check_version", null,
					null, null, VersionUtils.getVersionUtils().getVersion(this));
			boolean isSend = SendService.sendData(packet);
		} else if (v.equals(exit)) {
			this.getApplicationContext().stopService(ConnectorService.intent);
			SharedPrefsUtil.getSharedPrefsUtil().putValue(this, "isAutoLogin",
					false);
			finish();
			System.exit(0);
			// AppManager.getAppManager().AppExit(this);
		}

	}

}
