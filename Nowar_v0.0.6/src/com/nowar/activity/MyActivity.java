package com.nowar.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lindec.app.tools.BitmapUtil;
import com.lindec.app.tools.BluetoothUtil;
import com.lindec.app.tools.DateUitl;
import com.lindec.app.tools.PrintUtil;
import com.lindec.app.tools.VersionUtils;
import com.nowar.activity.AboutActivity;
import com.nowar.activity.FeedbackActivity;
import com.nowar.activity.MainActivity;
import com.nowar.activity.OpenActivity;
import com.nowar.activity.SetHeadActivity;
import com.nowar.activity.SetUsernameActivity;
import com.nowar.adapter.BluetoothDeviceAdapter;
import com.nowar.adapter.BluetoothDeviceItem;
import com.nowar.adapter.ViewPagerAdapter;
import com.nowar.broadcast.BluetoothBroadcast;
import com.nowar.broadcast.BROADCAST_CONSTANT.BLUETOOTH;
import com.nowar.broadcast.BROADCAST_CONSTANT.NETWORK;
import com.nowar.db.BtDB;
import com.nowar.db.BtDevice;
import com.nowar.db.BtdbFun;
import com.nowar.main.R;
import com.nowar.menu.SlidingMenu;
import com.nowar.mina.SendService;
import com.nowar.packet.MessagePacket;
import com.nowar.service.ConnectorService;
import com.nowar.sharedprefs.SharedPrefsUtil;
import com.nowar.version.UpdateVersions;
import com.nowar.view.SlidingTabLayout;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends ActionBarActivity implements OnClickListener,
		OnItemClickListener {

	public static BluetoothSocket _socket = null;
	ActionBarDrawerToggle mDrawerToggle;
	DrawerLayout mDrawerLayout;

	SlidingTabLayout mSlidingTabLayout;
	ViewPager mViewPager;
	ViewPagerAdapter mViewPagerAdapter;

	SwipeRefreshLayout mSwipeLayout;
	LinearLayout navButton1;
	LinearLayout navButton2;
	LinearLayout navButton3;
	LinearLayout navButton4;
	LinearLayout navButton5;
	Toolbar mToolbar;

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	private ImageView mRefreshImag;

	private ImageView mUserImag;
	private TextView mUserText;
	private Bitmap mBitmap1;
	private Bitmap mBitmap2;
	private String userNumber;
	private String head_path;// 头像路径
	private BtDevice btDevice;
	private List<BluetoothDeviceItem> mList;

	public static BluetoothAdapter mBtAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_m);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothUtil.getBluetoothUtil().btEnable(mBtAdapter);
		//BluetoothUtil.getBluetoothUtil().btSearch(mBtAdapter);
		initview();
		initdata();
		initlistener();
	}

	private void initview() {
		mUserImag = (ImageView) findViewById(R.id.menu_user_head);
		mUserText = (TextView) findViewById(R.id.menu_user_name);
		navButton1 = (LinearLayout) findViewById(R.id.txtNavButton1);
		navButton2 = (LinearLayout) findViewById(R.id.txtNavButton2);
		navButton3 = (LinearLayout) findViewById(R.id.txtNavButton3);
		navButton4 = (LinearLayout) findViewById(R.id.txtNavButton4);
		navButton5 = (LinearLayout) findViewById(R.id.txtNavButton5);
		mDrawerLayout = (DrawerLayout) findViewById(R.id._drawer_layout);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		mViewPager = (ViewPager) findViewById(R.id._view_pager);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id._sliding_tabs);
		// tab1ListView = (ListView) findViewById(R.id.id_listview1);
		// tab2ListView = (ListView) findViewById(R.id.id_listview2);

	}

	private void initdata() {
		// mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		// BluetoothUtil.getBluetoothUtil().btEnable(mBtAdapter);

		mToolbar.setTitle("");
		setSupportActionBar(mToolbar);
		mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(
				R.color.gainsboro));
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				mToolbar, R.string.app_name, R.string.app_name);
		mViewPager.setOffscreenPageLimit(2);
		mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator,
				android.R.id.text1);
		// 滑动条颜色
		mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(
				R.color.gainsboro));
		mSlidingTabLayout.setDistributeEvenly(true);
		mViewPagerAdapter = new ViewPagerAdapter(MyActivity.this, this);
		mViewPager.setAdapter(mViewPagerAdapter);
		mSlidingTabLayout.setViewPager(mViewPager);

		userNumber = SharedPrefsUtil.getSharedPrefsUtil().getValue(this,
				"userNumber", "");
		// String userName = SharedPrefsUtil.getSharedPrefsUtil().getValue(this,
		// userNumber, "");
		// if (userName != null && !userName.equals("")) {
		// mUserText.setText(userName);
		// }
		// head_path = "/sdcard/myHead/" + userNumber + "/";// sd路径
		// mBitmap1 = BitmapFactory.decodeFile(head_path + "head.jpg");//
		// 从Sd中找头像，转换成Bitmap
		// if (mBitmap1 != null) {
		// // Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
		// // mUserImag.setImageDrawable(drawable);
		// mBitmap2 = BitmapUtil.getRoundedCornerBitmap(mBitmap1,
		// (float) (300.0));
		// mUserImag.setImageBitmap(mBitmap2);
		// } else {
		// // 先设默认模式
		// mBitmap1 = BitmapUtil.drawableToBitmap(getResources().getDrawable(
		// R.drawable.ic_user));
		// mBitmap2 = BitmapUtil.getRoundedCornerBitmap(mBitmap1,
		// (float) (300.0));
		// mUserImag.setImageBitmap(mBitmap2);
		// /**
		// * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
		// *
		// */
		// }

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mReciver = new MessageBackReciver();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(NETWORK.ACTION);

	}

	private void initlistener() {
		mUserImag.setOnClickListener(this);
		navButton1.setOnClickListener(this);
		navButton2.setOnClickListener(this);
		navButton3.setOnClickListener(this);
		navButton4.setOnClickListener(this);
		navButton5.setOnClickListener(this);
		// tab1ListView.setOnItemClickListener(this);
		// tab2ListView.setOnItemClickListener(this);
		if (mSlidingTabLayout != null) {
			mSlidingTabLayout
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageScrolled(int position,
								float positionOffset, int positionOffsetPixels) {
							// System.out.println("onPageScrolled-----"+position);

						}

						@Override
						public void onPageSelected(int position) {
							System.out
									.println("onPageSelected-----" + position);
						}

						@Override
						public void onPageScrollStateChanged(int state) {
							// System.out.println("onPageScrollStateChanged-----"+state);
						}
					});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
			mDrawerLayout.closeDrawers();
			return;
		}
		super.onBackPressed();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mViewPagerAdapter.updateListView();
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
		mBitmap1 = BitmapFactory.decodeFile(head_path + "head.jpg");//
		// 从Sd中找头像，转换成Bitmap
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
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	@Override
	public void finish() {
		ViewPagerAdapter.mList1.clear();
		ViewPagerAdapter.mList2.clear();
		BluetoothBroadcast.setNewDevice.clear();
		BluetoothBroadcast.setBondDevice.clear();
		BluetoothUtil.getBluetoothUtil().btDisable(mBtAdapter);
		moveTaskToBack(true);// 设置该activity永不过期
	}

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View view, int position,
	// long arg3) {
	// // intent.putExtra("position", position);
	// // System.out.println("position:" + position);
	// PrintUtil.getPrintUtil().println(DateUitl.getMSMDate());
	// btDevice = allTicketsList.get(position);
	// // System.out.println("name:" + bt.getSwdeviceName());
	// // System.out.println("number:" + bt.getSwdeviceNumber());
	// _socket = BluetoothUtil.btConnect(mBtAdapter,
	// btDevice.getSwdeviceNumber());
	// if (null != _socket) {
	// Intent intent = new Intent(MyActivity.this, OpenActivity.class);
	// // intent.setClass(MainActivity.this, OpenActivity.class);
	// startActivity(intent);
	// } else {
	// }
	//
	// // Message msg = Message.obtain();
	// // handle.sendMessage(msg);
	// }

	private class MessageBackReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(NETWORK.ACTION)) {
				String message = intent.getStringExtra("message");
				if (message.equals("old_version")) {
					Toast.makeText(MyActivity.this, "已是最新版本", Toast.LENGTH_LONG)
							.show();
				} else if (message.equals("new_version")) {
					UpdateVersions mUpdate = new UpdateVersions(MyActivity.this);
					mUpdate.checkUpdateInfo();
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		if (v.equals(navButton1)) {
			Intent intent = new Intent();
			intent.putExtra("userName", mUserText.getText().toString().trim());
			intent.setClass(this, SetUsernameActivity.class);
			startActivity(intent);
		} else if (v.equals(navButton2)) {
			Intent intent = new Intent();
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
		} else if (v.equals(navButton3)) {
			Intent intent = new Intent();
			intent.setClass(this, FeedbackActivity.class);
			startActivity(intent);

		} else if (v.equals(navButton4)) {
			MessagePacket packet = new MessagePacket("check_version", null,
					null, null, VersionUtils.getVersionUtils().getVersion(this));
			boolean isSend = SendService.sendData(packet);

		} else if (v.equals(navButton5)) {
			this.getApplicationContext().stopService(ConnectorService.intent);
			SharedPrefsUtil.getSharedPrefsUtil().putValue(this, "isAutoLogin",
					false);
			finish();
			System.exit(0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public void onClick(View v) {
	//
	// if (v.equals(mRefreshImag)) {
	// // BluetoothUtil.getBluetoothUtil().outAllPairedDevice(mBtAdapter);
	// dbFun.DelAllDevice(db);
	// mList.clear();
	// mBluetoothDeviceAdapter.notifyDataSetChanged();
	// BluetoothUtil.getBluetoothUtil().btRequetEnable(this, mBtAdapter);
	// BluetoothUtil.getBluetoothUtil().btSearch(mBtAdapter);
	// } else if (v.equals(mUserImag)) {
	// Intent intent = new Intent();
	// intent.setClass(this, SetHeadActivity.class);
	// startActivity(intent);
	// }
	//
	// }
}
