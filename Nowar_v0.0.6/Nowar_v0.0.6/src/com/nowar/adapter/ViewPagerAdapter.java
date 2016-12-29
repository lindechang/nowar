package com.nowar.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.lindec.app.tools.BluetoothUtil;
import com.nowar.activity.OpenActivity;
import com.nowar.broadcast.BROADCAST_CONSTANT.BLUETOOTH;
import com.nowar.broadcast.BluetoothBroadcast;
import com.nowar.db.BtDevice;
import com.nowar.main.R;
import com.nowar.activity.MyActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ViewPagerAdapter extends PagerAdapter {
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	private SparseArray<View> pageViews = new SparseArray<View>();
	private Context context;
	private Activity getActivity;
	private SwipeRefreshLayout mSwipeLayout1;
	private SwipeRefreshLayout mSwipeLayout2;
	private ListView tab1ListView;
	private ListView tab2ListView;
	// private BluetoothAdapter mBtAdapter;
	private static final int LISTVIEW1_REFRESH = 0X001;
	private static final int LISTVIEW2_REFRESH = 0X002;
	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	// private MyAdapter myAdapter;
	private BluetoothDeviceAdapter mBluetoothDeviceAdapter1;
	private BluetoothDeviceAdapter mBluetoothDeviceAdapter2;

	// private BtDevice btDevice;
	public static List<BluetoothDeviceItem> mList1;
	public static List<BluetoothDeviceItem> mList2;

	public ViewPagerAdapter(Activity activity, Context context) {
		getActivity = activity;
		this.context = context;
		// mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		// BluetoothUtil.getBluetoothUtil().btEnable(mBtAdapter);
		// BluetoothUtil.getBluetoothUtil().btSearch(mBtAdapter);
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
		mReciver = new MessageBackReciver();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BLUETOOTH.SEARCH_NEWDEVICE);
		mIntentFilter.addAction(BLUETOOTH.SEARCH_BONDDEVICE);
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);

		// btDevice = new BtDevice();
		mList1 = new ArrayList<BluetoothDeviceItem>();
		mList2 = new ArrayList<BluetoothDeviceItem>();
		mBluetoothDeviceAdapter1 = new BluetoothDeviceAdapter(context, mList1);
		mBluetoothDeviceAdapter2 = new BluetoothDeviceAdapter(context, mList2);

		// BluetoothBroadcast.setNewDevice.clear();
		// BluetoothBroadcast.setBondDevice.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return obj == view;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String pageText = null;
		switch (position) {
		case 0:
			pageText = "Ìí¼Ó";
			break;
		case 1:
			pageText = "¿ØÖÆ";
			break;

		default:
			break;
		}
		return (CharSequence) pageText;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// Inflate a new layout from our resources
		// System.out.println("position:" + position);
		View view;
		if (position == 0) {
			view = getActivity.getLayoutInflater().inflate(R.layout.tab1_view,
					container, false);
			mSwipeLayout1 = (SwipeRefreshLayout) view
					.findViewById(R.id.id_swipe_ly1);
			mSwipeLayout1.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					BluetoothUtil.getBluetoothUtil().btSearch(
							MyActivity.mBtAdapter);
					mList1.clear();
					mList2.clear();
					BluetoothBroadcast.setNewDevice.clear();
					BluetoothBroadcast.setBondDevice.clear();
					mBluetoothDeviceAdapter1.notifyDataSetChanged();
					mBluetoothDeviceAdapter2.notifyDataSetChanged();
					mHandler.sendEmptyMessageDelayed(LISTVIEW1_REFRESH, 2000);

				}
			});
			mSwipeLayout1.setColorScheme(android.R.color.holo_green_dark,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			tab1ListView = (ListView) view.findViewById(R.id.id_listview1);
			tab1ListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					BluetoothDeviceItem btItem = mList1.get(position);
					MyActivity._socket = BluetoothUtil.btConnect(
							MyActivity.mBtAdapter, btItem.number);
					if (null != MyActivity._socket) {
						BluetoothUtil.getBluetoothUtil().btDisconnect(
								MyActivity._socket);
						MyActivity._socket = null;
						mList1.remove(position);
						mBluetoothDeviceAdapter1.notifyDataSetChanged();
					}
				}
			});
			mList1.clear();
			Iterator<BtDevice> it = BluetoothBroadcast.setNewDevice.iterator();
			while (it.hasNext()) {
				BtDevice mBtDevice = (BtDevice) it.next();
				BluetoothDeviceItem item = new BluetoothDeviceItem();
				item.name = mBtDevice.getSwdeviceName();
				item.number = mBtDevice.getSwdeviceNumber();
				item.time = format.format(new Date());
				mList1.add(item);
			}
			tab1ListView.setAdapter(mBluetoothDeviceAdapter1);
			mBluetoothDeviceAdapter1.notifyDataSetChanged();

		} else {
			view = getActivity.getLayoutInflater().inflate(R.layout.tab2_view,
					container, false);
			mSwipeLayout2 = (SwipeRefreshLayout) view
					.findViewById(R.id.id_swipe_ly2);
			mSwipeLayout2.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					BluetoothUtil.getBluetoothUtil().btSearch(
							MyActivity.mBtAdapter);
					mList1.clear();
					mList2.clear();
					BluetoothBroadcast.setNewDevice.clear();
					BluetoothBroadcast.setBondDevice.clear();
					mBluetoothDeviceAdapter1.notifyDataSetChanged();
					mBluetoothDeviceAdapter2.notifyDataSetChanged();
					mHandler.sendEmptyMessageDelayed(LISTVIEW2_REFRESH, 2000);

				}
			});
			mSwipeLayout2.setColorScheme(android.R.color.holo_green_dark,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			tab2ListView = (ListView) view.findViewById(R.id.id_listview2);
			tab2ListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					BluetoothDeviceItem btItem = mList2.get(position);
					MyActivity._socket = BluetoothUtil.btConnect(
							MyActivity.mBtAdapter, btItem.number);
					if (null != MyActivity._socket) {
						Intent mIntent = new Intent();
						mIntent.setClass(getActivity, OpenActivity.class);
						getActivity.startActivity(mIntent);
					}

				}
			});
			mList2.clear();
			Iterator<BtDevice> it = BluetoothBroadcast.setBondDevice.iterator();
			int i = 0;
			while (it.hasNext()) {
				i++;
				System.out.println("i:" + i);
				BtDevice mBtDevice = (BtDevice) it.next();
				BluetoothDeviceItem item = new BluetoothDeviceItem();
				item.name = mBtDevice.getSwdeviceName();
				item.number = mBtDevice.getSwdeviceNumber();
				item.time = format.format(new Date());
				mList2.add(item);
			}
			tab2ListView.setAdapter(mBluetoothDeviceAdapter2);
			mBluetoothDeviceAdapter2.notifyDataSetChanged();

		}

		container.addView(view);

		pageViews.put(position, view);
		// Return the View
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		pageViews.remove(position);
	}

	@Override
	public void notifyDataSetChanged() {
		int position = 0;
		for (int i = 0; i < pageViews.size(); i++) {
			position = pageViews.keyAt(i);
			View view = pageViews.get(position);
			// Change the content of this view
			TextView txt = (TextView) view.findViewById(R.id.item_subtitle);
			txt.setText("This Page " + (position + 1) + " has been refreshed");
		}
		super.notifyDataSetChanged();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LISTVIEW1_REFRESH:
				// mDatas.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
				// mAdapter.notifyDataSetChanged();
				mSwipeLayout1.setRefreshing(false);
				break;
			case LISTVIEW2_REFRESH:
				// mDatas.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
				// mAdapter.notifyDataSetChanged();
				mSwipeLayout2.setRefreshing(false);
				break;

			}
		};
	};

	private class MessageBackReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			System.out.println("action:" + action);
			if (action.equals(BLUETOOTH.SEARCH_NEWDEVICE)) {

				mList1.clear();
				Iterator<BtDevice> it = BluetoothBroadcast.setNewDevice
						.iterator();
				while (it.hasNext()) {
					BtDevice mBtDevice = (BtDevice) it.next();
					BluetoothDeviceItem item = new BluetoothDeviceItem();
					item.name = mBtDevice.getSwdeviceName();
					item.number = mBtDevice.getSwdeviceNumber();
					item.time = format.format(new Date());
					mList1.add(item);
				}
				tab1ListView.setAdapter(mBluetoothDeviceAdapter1);
				mBluetoothDeviceAdapter1.notifyDataSetChanged();

			} else if (action.equals(BLUETOOTH.SEARCH_BONDDEVICE)) {
				mList2.clear();
				int m = 0;
				Iterator<BtDevice> it = BluetoothBroadcast.setBondDevice
						.iterator();
				while (it.hasNext()) {
					m++;
					System.out.println("m:" + m);
					BtDevice mBtDevice = (BtDevice) it.next();
					BluetoothDeviceItem item = new BluetoothDeviceItem();
					item.name = mBtDevice.getSwdeviceName();
					item.number = mBtDevice.getSwdeviceNumber();
					item.time = format.format(new Date());
					mList2.add(item);
				}
				tab2ListView.setAdapter(mBluetoothDeviceAdapter2);
				mBluetoothDeviceAdapter2.notifyDataSetChanged();
			}
		};
	}

	public void updateListView() {
		if (null != mBluetoothDeviceAdapter1) {
			mBluetoothDeviceAdapter1.notifyDataSetChanged();
		}
		if (null != mBluetoothDeviceAdapter2) {
			mBluetoothDeviceAdapter2.notifyDataSetChanged();
		}

	}
}
