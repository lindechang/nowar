package weilan.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weilan.app.db.DeviceDB;
import weilan.app.db.CreateDB;
import weilan.app.main.R;
import weilan.app.tools.http.NetTool;
import weilan.app.ui.ListViewCompat;
import weilan.app.ui.SlideView;
import weilan.app.ui.SlideView.OnSlideListener;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设备列表Activity
 * 
 * @author lindec
 * 
 */
public class DeviceListActivity extends Activity implements
		OnItemClickListener, OnClickListener, OnSlideListener {

	private static final String TAG = "DeviceListActivity";

	private ListViewCompat mListView;

	private List<MessageItem> mMessageItems = new ArrayList<DeviceListActivity.MessageItem>();

	private SlideAdapter mSlideAdapter;

	// 声明进度条对话框
	public ProgressDialog m_pDialog;

	// 上次处于打开状态的SlideView
	private SlideView mLastSlideViewWithStatusOn;

	// SQL
	private DeviceDB mDeviceDB;
	private Cursor mCursor;

	private Button addBtn;

	private View myview;

	private String I_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_listview);
		// 传递过来的token
		Intent intent = getIntent();
		Bundle mbundle = intent.getExtras();
		I_token = mbundle.getString("token");
		mDalay();
		initView();

	}

	private void initView() {
		mDeviceDB = new DeviceDB(this);
		// mTrackerDB.Open();

		mCursor = mDeviceDB.SelectDevice();

		addBtn = (Button) findViewById(R.id.slide_titlebar_add); // 设备添加按键
		addBtn.setOnClickListener(this);

		for (int i = 0; i < mCursor.getCount(); i++) {
			// System.out.println("mCursor.getCount()" + mCursor.getCount());
			MessageItem item = new MessageItem();

			mCursor.moveToPosition(i);
			item.iconRes = R.drawable.map;
			item.title = mCursor.getString(1);
			item.msg = mCursor.getString(2);
			item.time = "06:18";
			mMessageItems.add(item);
		}

		mSlideAdapter = new SlideAdapter();
		mListView = (ListViewCompat) findViewById(R.id.list);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mSlideAdapter);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				int position = mListView.getPositionForView(arg1);
				// Intent intent = new Intent();
				// intent.setClass(DeviceListActivity.this, MapActivity.class);
				// Bundle device = new Bundle();
				// device.putInt("position", position);
				// intent.putExtras(device);
				// DeviceListActivity.this.startActivity(intent);

				return false;
			}
		});

	}

	private class SlideAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		SlideAdapter() {
			super();
			mInflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return mMessageItems.size();
			// return mCursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return mMessageItems.get(position);
			// return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			SlideView slideView = (SlideView) convertView;
			if (slideView == null) {
				// 这里是我们的item
				View itemView = mInflater.inflate(R.layout.slide_listview_item,
						null);

				slideView = new SlideView(DeviceListActivity.this);
				// 这里把item加入到slideView
				slideView.setContentView(itemView);
				// 下面是做一些数据缓存
				holder = new ViewHolder(slideView);
				slideView.setOnSlideListener(DeviceListActivity.this);
				slideView.setTag(holder);
			} else {
				holder = (ViewHolder) slideView.getTag();
			}
			MessageItem item = mMessageItems.get(position);
			item.slideView = slideView;
			item.slideView.shrink();

			mCursor.moveToPosition(position);

			// holder.icon.setImageResource(item.iconRes);
			// holder.title.setText(item.title);
			// holder.msg.setText(item.msg);
			// holder.time.setText(item.time);
			holder.icon.setImageResource(R.drawable.tp_device_item);
			holder.title.setText(mCursor.getString(1));
			holder.msg.setText(mCursor.getString(2));
			holder.time.setText("18:20");
			holder.setHolder.setOnClickListener(DeviceListActivity.this);
			holder.deleteHolder.setOnClickListener(DeviceListActivity.this);

			return slideView;
		}

	}

	public class MessageItem {
		public int iconRes;
		public String title;
		public String msg;
		public String time;
		public SlideView slideView;
	}

	private static class ViewHolder {
		public ImageView icon;
		public TextView title;
		public TextView msg;
		public TextView time;
		public ViewGroup setHolder;
		public ViewGroup deleteHolder;

		ViewHolder(View view) {
			icon = (ImageView) view.findViewById(R.id.icon);
			title = (TextView) view.findViewById(R.id.title);
			msg = (TextView) view.findViewById(R.id.msg);
			time = (TextView) view.findViewById(R.id.time);
			setHolder = (ViewGroup) view.findViewById(R.id.holder);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder2);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 这里处理ListItem的点击事件
		Log.e(TAG, "onItemClick position=" + position);

	}

	@Override
	public void onSlide(View view, int status) {
		// 如果当前存在已经打开的SlideView，那么将其关闭
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}
		// 记录本次处于打开状态的view
		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}

	private void mDalay() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(this);

		// 设置进度条风格，风格为圆形，旋转的
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// 设置ProgressDialog 提示信息
		m_pDialog.setMessage("请稍等。。。");

		// 设置ProgressDialog 的进度条是否不明确
		m_pDialog.setIndeterminate(false);

		// 设置ProgressDialog 是否可以按退回按键取消
		m_pDialog.setCancelable(false);
		
		

		// 设置ProgressDialog

		// m_pDialog.show();

		// 需要关闭这个提示的时候：

		// m_pDialog.hide();

	}

	/**
	 * AddDevice 添加设备
	 */
	public void AddDevice() {
		DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {
				// case Dialog.BUTTON_NEGATIVE:
				// Toast.makeText(MainActivity.this, "No..",
				// Toast.LENGTH_LONG).show();
				// break;
				// case Dialog.BUTTON_NEUTRAL:
				// Toast.makeText(MainActivity.this, "I don't know.",
				// Toast.LENGTH_LONG).show();
				// break;
				case Dialog.BUTTON_POSITIVE: // 添加设备
					EditText mBookName = (EditText) myview
							.findViewById(R.id.editText1);
					EditText mBookAuthor = (EditText) myview
							.findViewById(R.id.editText2);
					String mbookname = mBookName.getText().toString().trim();
					String mbookauthor = mBookAuthor.getText().toString()
							.trim();

					// 书名和作者都不能为空，或者退出
					if (mbookname.equals("") || mbookauthor.equals("")) {
						return;
					}
					MessageItem item = new MessageItem();
					item.iconRes = R.drawable.tp_device_item;
					item.title = mbookname;
					item.msg = mbookauthor;
					item.time = "18:18";
					mMessageItems.add(item);
					// mBooksDB.insert(mbookname,
					// mbookauthor,mstatus,mpassword,mtiming);
					// mTrackerDB.insert(mbookname, mbookauthor, 0, 0);
                    mDeviceDB.InsertDevice(mbookname, mbookauthor, 0, 0);
					mCursor.requery();
					mListView.invalidateViews();
					Toast.makeText(DeviceListActivity.this, "添加成功!",
							Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}

			}
		};

		LayoutInflater minflater = (LayoutInflater) DeviceListActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// LayoutInflater minflater = LayoutInflater.from(MainActivity.this);
		myview = minflater.inflate(R.layout.add_device_dialog, null);
		new AlertDialog.Builder(this).setTitle("添加追踪")
				.setIcon(android.R.drawable.ic_dialog_info).setView(myview)
				.setPositiveButton("确定", onclick).setNegativeButton("取消", null)
				.show();

	}

	@Override
	public void onClick(View v) {
		// 这里处理删除按钮的点击事件，可以删除对话
		if (v.equals(addBtn)) {
			AddDevice();
		}
		if (v.getId() == R.id.holder) {
			String retStr = "";
			String bindUrl = "http://23.245.26.254/index.php/home/device/bind?token="
					+ I_token;
			Map<String, String> map = new HashMap<String, String>();
			System.out.println("I_token-----:" + I_token);
			// map.put("token", I_token);
			map.put("deviceid", "123");

			try {
				retStr = NetTool.sendPostRequest(bindUrl, map, "utf-8");
				System.out.println("retStr-----:" + retStr);

			} catch (Exception e) {
				e.printStackTrace();
			}
			// int position = mListView.getPositionForView(v);
			// Intent mintent = new Intent();
			// mintent.setClass(DeviceListActivity.this,SetInfoActivity.class);
			// Bundle device = new Bundle();
			// device.putInt("pos", position);
			// mintent.putExtras(device);
			// DeviceListActivity.this.startActivity(mintent);
			// finish();//为了SetInfoActivity 返回时
			// 执行第一个DeviceListActivity的onCreate()方法
		}
		// 删除设备
		if (v.getId() == R.id.holder2) {
			int position = mListView.getPositionForView(v);
			if (position != ListView.INVALID_POSITION) {

				mCursor.moveToPosition(position);
				if (mCursor.getInt(0) != 0) {
					//mTrackerDB.delete(mCursor.getInt(0));
					mDeviceDB.DeleteDeviceByID(mCursor.getInt(0));
					mCursor.requery();
				}
				mMessageItems.remove(position);
				m_pDialog.show();
				mSlideAdapter.notifyDataSetChanged();
				// 需要关闭这个提示的时候：
				// m_pDialog.hide();
				// mListView.invalidateViews();

				/* 开启一个新线程，在新线程里执行耗时的方法 */
				new Thread(new Runnable() {
					@Override
					public void run() {
						spandTimeMethod();// 耗时的方法
						// handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
						// m_pDialog.hide();
						// m_pDialog.dismiss();
						m_pDialog.cancel();
						// Toast.makeText(DeviceListActivity.this, "删除成功!",
						// Toast.LENGTH_SHORT).show();
					}

				}).start();
			}
			Log.e(TAG, "onClick v=" + v);
		}
	}

	// public void onLongClick(View v) {
	// //学习软件开发最主要的是学习思想，不是拿别人的代码。
	// int position = mListView.getPositionForView(v);
	// Intent intent = new Intent();
	// intent.setClass(DeviceListActivity.this, MainActivity.class);
	// Bundle device = new Bundle();
	// device.putInt("position", position);
	// intent.putExtras(device);
	// DeviceListActivity.this.startActivity(intent);
	//
	// //return false;
	// }

	private void spandTimeMethod() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onRestart() {
		super.onRestart();
		mCursor.requery();
		mListView.invalidateViews();
	}

}
