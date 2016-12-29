package weilan.app.ui;

import java.util.ArrayList;
import java.util.List;

import weilan.app.activity.AddDeviceActivity;
import weilan.app.activity.ChatActivity;
import weilan.app.activity.SetDeviceInfoActivity;
import weilan.app.activity.SetInfoActivity;
import weilan.app.activity.SwOperateActivity;
import weilan.app.db.Friend;
import weilan.app.db.FriendTable;
import weilan.app.db.FriendTableHandle;
import weilan.app.db.SwDB;
import weilan.app.db.SwDevice;
import weilan.app.db.SwdbFun;
import weilan.app.main.R;
import weilan.app.main.RoutePlanActivity;
import weilan.app.popupwindow.MorePopWindow;
import weilan.app.tools.http.NetTool;
import weilan.app.ui.SlideView.OnSlideListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * Created with IntelliJ IDEA. Author: wangjie email:tiantian.china.2@gmail.com
 * Date: 13-6-14 Time: 下午2:39
 */
public class Tab_FW extends Fragment implements OnItemClickListener,
		OnClickListener, OnSlideListener {

	private ListViewCompat mListView;

	private List<MessageItem> mMessageList = new ArrayList<MessageItem>();
	private ArrayList<Friend> allTicketsList = new ArrayList<Friend>();
	private SlideAdapter mSlideAdapter;

	// 声明进度条对话框
	public ProgressDialog m_pDialog;

	// 上次处于打开状�?的SlideView
	private SlideView mLastSlideViewWithStatusOn;

	// SQL
	//private SwDB mSwdb;
	private FriendTable mFriendDB;
	// private Cursor mCursor;
	private FriendTableHandle mFrienddbFun;
	private Button addBtn;
	// private LinearLayout addSs;
	// private LinearLayout addSm;

	private View myview;

	// private String I_token;

	private void initView() {

		// TextView tiltename = (TextView)
		// getView().findViewById(R.id.friends_list_title_text_id);
		// tiltename.setTextColor(this.getResources().getColor(R.color.white));
		// 传�?过来的token
		// Intent intent = getActivity().getIntent();
		// Bundle mbundle = intent.getExtras();
		// I_token = mbundle.getString("token");
		mFriendDB = new FriendTable(this.getActivity());
		mFrienddbFun = new FriendTableHandle();

		// mSwdb.Open();
		// mCursor = mSwdb.SelectSW();

		addBtn = (Button) this.getView().findViewById(R.id.friends_list_title_add_id); // 好友添加按键
		addBtn.setOnClickListener(Tab_FW.this);

		// for (int i = 0; i < mCursor.getCount(); i++) {
		// // System.out.println("mCursor.getCount()" + mCursor.getCount());
		// MessageItem item = new MessageItem();
		//
		// mCursor.moveToPosition(i);
		// if (mCursor.getInt(3) == 1) {
		// item.iconRes = R.drawable.sw_button_open;
		// } else {
		// item.iconRes = R.drawable.sw_button_close;
		// }
		//
		// item.title = mCursor.getString(1);
		// item.msg = mCursor.getString(2);
		// item.time = "06:18";
		// mMessageList.add(item);
		// }

		allTicketsList = mFrienddbFun.FetchAll(mFriendDB);
		mMessageList.clear();
		for (int i = 0; i < allTicketsList.size(); i++) {
			MessageItem item = new MessageItem();
			Friend book = new Friend();
			book = allTicketsList.get(i);
			item.iconRes = R.drawable.defalt_head;
			// if (sw.getSwdeviceSelect() == 1) {
			// item.iconRes = R.drawable.sw_button_open;
			// } else {
			// item.iconRes = R.drawable.sw_button_close;
			// }
			item.title = book.getFriendName();
			item.msg = book.getFriendNumber();
			item.time = "06:18";
			mMessageList.add(item);
		}

		mSlideAdapter = new SlideAdapter();
		mListView = (ListViewCompat) this.getView().findViewById(R.id.friends_list_id);
		mListView.setOnItemClickListener(Tab_FW.this);
		mListView.setAdapter(mSlideAdapter);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				System.out.println("AAAAAAAAAA____onItemLongClick");
				int position = mListView.getPositionForView(arg1);
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.setClass(getActivity(), ChatActivity.class);
				// Bundle device = new Bundle();
				// device.putInt("position", position);
				// intent.putExtras(device);
				getActivity().startActivity(intent);
				return false;
			}
		});

	}

	private void uplistview() {
		allTicketsList = mFrienddbFun.FetchAll(mFriendDB);
		mMessageList.clear();
		for (int i = 0; i < allTicketsList.size(); i++) {
			MessageItem item = new MessageItem();
			Friend book = new Friend();
			book = allTicketsList.get(i);
			item.iconRes = R.drawable.defalt_head;
			// if (sw.getSwdeviceSelect() == 1) {
			// item.iconRes = R.drawable.sw_button_open;
			// } else {
			// item.iconRes = R.drawable.sw_button_close;
			// }
			item.title = book.getFriendName();
			item.msg = book.getFriendNumber();
			item.time = "06:18";
			mMessageList.add(item);
		}
		mSlideAdapter.notifyDataSetChanged();
	}

	private void mDalay() {
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(getActivity());

		//

		// 设置进度条风格，风格为圆形，旋转�?
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// 设置ProgressDialog 提示信息
		m_pDialog.setMessage("请稍等�?。�?");

		// 设置ProgressDialog 的进度条是否不明�?
		m_pDialog.setIndeterminate(false);

		// 设置ProgressDialog 是否可以按�?回按键取�?
		m_pDialog.setCancelable(false);

		// 设置ProgressDialog

		// m_pDialog.show();

		// �?��关闭这个提示的时候：

		// m_pDialog.hide();

	}

	/**
	 * list的一个Item参数 赋初�?
	 * 
	 * @author lindec
	 * 
	 */
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

	/**
	 * 自定义�?配器
	 * 
	 * @author lindec
	 * 
	 */
	private class SlideAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		SlideAdapter() {
			super();
			mInflater = getActivity().getLayoutInflater();
		}

		@Override
		public int getCount() {
			return mMessageList.size();
			// return mCursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return mMessageList.get(position);
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

				slideView = new SlideView(getActivity());
				// 这里把item加入到slideView
				slideView.setContentView(itemView);
				// 下面是做�?��数据缓存
				holder = new ViewHolder(slideView);
				slideView.setOnSlideListener(Tab_FW.this);
				slideView.setTag(holder);
			} else {
				holder = (ViewHolder) slideView.getTag();
			}
			MessageItem item = mMessageList.get(position);
			item.slideView = slideView;
			item.slideView.shrink();

			holder.icon.setImageResource(item.iconRes);
			holder.title.setText(item.title);
			holder.msg.setText(item.msg);
			holder.time.setText("18:20");

			holder.setHolder.setOnClickListener(Tab_FW.this);
			holder.deleteHolder.setOnClickListener(Tab_FW.this);

			return slideView;
		}

	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.friends_list_view, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mDalay();
		initView();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 更新listview
		uplistview();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private void spandTimeMethod() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// 这里处理删除按钮的点击事件，可以删除对话
		if (v.equals(addBtn)) {
			// AddDevice();
			//Intent intent = new Intent();
			//intent.setClass(getActivity(), AddDeviceActivity.class);
			//intent.setClass(getActivity(), ChatActivity.class);
			//startActivity(intent);
		}

		// if (v.equals(addBtn)) {
		// // AddDevice();
		// System.out.println("有反馈："
		// +swFun.CheckSwDevice(mSwdb,"FFFFFFFFFFFF"));
		// }

		// 设置信息
		if (v.getId() == R.id.holder) {
			int position = mListView.getPositionForView(v);
			Intent intent = new Intent();
			intent.setClass(getActivity(), SetDeviceInfoActivity.class);
			intent.putExtra("position", position);
			startActivity(intent);

			// finish();// 为了SetInfoActivity 返回�?
			// 执行第一个DeviceListActivity的onCreate()方法
		}
		// 删除设备
		if (v.getId() == R.id.holder2) {
			int position = mListView.getPositionForView(v);
			if (position != ListView.INVALID_POSITION) {
				mFrienddbFun.DelSingleFriend(mFriendDB, position);
				mMessageList.remove(position);
				m_pDialog.show();
				mSlideAdapter.notifyDataSetChanged();
				// �?��关闭这个提示的时候：
				// m_pDialog.hide();
				// mListView.invalidateViews();

				/* �?���?��新线程，在新线程里执行�?时的方法 */
				new Thread(new Runnable() {
					@Override
					public void run() {
						spandTimeMethod();// 耗时的方�?
						// handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
						// m_pDialog.hide();
						// m_pDialog.dismiss();
						m_pDialog.cancel();
						// Toast.makeText(DeviceListActivity.this, "删除成功!",
						// Toast.LENGTH_SHORT).show();
					}

				}).start();
			}
			// Log.e(TAG, "onClick v=" + v);
		}
	}

	@Override
	public void onSlide(View view, int status) {
		// 如果当前存在已经打开的SlideView，那么将其关�?
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}
		// 记录本次处于打开状�?的view
		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

}
