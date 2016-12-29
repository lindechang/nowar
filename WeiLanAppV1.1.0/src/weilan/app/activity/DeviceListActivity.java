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
 * �豸�б�Activity
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

	// �����������Ի���
	public ProgressDialog m_pDialog;

	// �ϴδ��ڴ�״̬��SlideView
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
		// ���ݹ�����token
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

		addBtn = (Button) findViewById(R.id.slide_titlebar_add); // �豸��Ӱ���
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
				// ���������ǵ�item
				View itemView = mInflater.inflate(R.layout.slide_listview_item,
						null);

				slideView = new SlideView(DeviceListActivity.this);
				// �����item���뵽slideView
				slideView.setContentView(itemView);
				// ��������һЩ���ݻ���
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
		// ���ﴦ��ListItem�ĵ���¼�
		Log.e(TAG, "onItemClick position=" + position);

	}

	@Override
	public void onSlide(View view, int status) {
		// �����ǰ�����Ѿ��򿪵�SlideView����ô����ر�
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}
		// ��¼���δ��ڴ�״̬��view
		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}

	private void mDalay() {
		// ����ProgressDialog����
		m_pDialog = new ProgressDialog(this);

		// ���ý�������񣬷��ΪԲ�Σ���ת��
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// ����ProgressDialog ��ʾ��Ϣ
		m_pDialog.setMessage("���Եȡ�����");

		// ����ProgressDialog �Ľ������Ƿ���ȷ
		m_pDialog.setIndeterminate(false);

		// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
		m_pDialog.setCancelable(false);
		
		

		// ����ProgressDialog

		// m_pDialog.show();

		// ��Ҫ�ر������ʾ��ʱ��

		// m_pDialog.hide();

	}

	/**
	 * AddDevice ����豸
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
				case Dialog.BUTTON_POSITIVE: // ����豸
					EditText mBookName = (EditText) myview
							.findViewById(R.id.editText1);
					EditText mBookAuthor = (EditText) myview
							.findViewById(R.id.editText2);
					String mbookname = mBookName.getText().toString().trim();
					String mbookauthor = mBookAuthor.getText().toString()
							.trim();

					// ���������߶�����Ϊ�գ������˳�
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
					Toast.makeText(DeviceListActivity.this, "��ӳɹ�!",
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
		new AlertDialog.Builder(this).setTitle("���׷��")
				.setIcon(android.R.drawable.ic_dialog_info).setView(myview)
				.setPositiveButton("ȷ��", onclick).setNegativeButton("ȡ��", null)
				.show();

	}

	@Override
	public void onClick(View v) {
		// ���ﴦ��ɾ����ť�ĵ���¼�������ɾ���Ի�
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
			// finish();//Ϊ��SetInfoActivity ����ʱ
			// ִ�е�һ��DeviceListActivity��onCreate()����
		}
		// ɾ���豸
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
				// ��Ҫ�ر������ʾ��ʱ��
				// m_pDialog.hide();
				// mListView.invalidateViews();

				/* ����һ�����̣߳������߳���ִ�к�ʱ�ķ��� */
				new Thread(new Runnable() {
					@Override
					public void run() {
						spandTimeMethod();// ��ʱ�ķ���
						// handler.sendEmptyMessage(0);// ִ�к�ʱ�ķ���֮��������handler
						// m_pDialog.hide();
						// m_pDialog.dismiss();
						m_pDialog.cancel();
						// Toast.makeText(DeviceListActivity.this, "ɾ���ɹ�!",
						// Toast.LENGTH_SHORT).show();
					}

				}).start();
			}
			Log.e(TAG, "onClick v=" + v);
		}
	}

	// public void onLongClick(View v) {
	// //ѧϰ�����������Ҫ����ѧϰ˼�룬�����ñ��˵Ĵ��롣
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
