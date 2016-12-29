package weilan.app.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.db.Friend;
import weilan.app.db.FriendTable;
import weilan.app.db.FriendTableHandle;
import weilan.app.db.MsgEx;
import weilan.app.db.MsgExTableHandle;
import weilan.app.main.R;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.mina.SendService;
import weilan.app.ui.ChatMsgEntity;
import weilan.app.ui.ChatMsgViewAdapter;
import weilan.app.ui.SoundMeter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private Button mBtnSend;
	private Button mBtnBack;
	private ImageButton mImageButton;
	private TextView mBtnRcd;
	
	
	private EditText mEditTextContent;
	private RelativeLayout mBottom;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private boolean isShosrt = false;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
			voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private View rcChat_popup;
	private LinearLayout del_re;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = false;
	private int flag = 1;
	private Handler mHandler = new Handler();
	private String voiceName;
	private long startVoiceT, endVoiceT;
	private Date date;
	private FriendTable mFriendDB;
	private FriendTableHandle mFrienddbFun;
	private MsgExTableHandle msgExHandle;

	private String contString;// ��������

	private int position;
	private String friendnub;

	ArrayList<MsgEx> allMsgExList = new ArrayList<MsgEx>();

	private MessageBackReciver mReciver;
	private IntentFilter mIntentFilter;
	private LocalBroadcastManager mLocalBroadcastManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		mFriendDB = new FriendTable(this);
		mFrienddbFun = new FriendTableHandle();
		Friend friend = mFrienddbFun.getSingleFriend(mFriendDB, position);
		friendnub = friend.getFriendNumber();

		msgExHandle = new MsgExTableHandle(this);

		mReciver = new MessageBackReciver();
		mReciver.isInitialStickyBroadcast();
		mIntentFilter = new IntentFilter();
		// mIntentFilter.addAction(ConnectorService.HEART_BEAT_ACTION);
		mIntentFilter.addAction(BCH_CONSTANT.TYPE.CHAT);
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		// ����activityʱ���Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();

		setData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
	}

	protected void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
		// mSwdb.Close();
	}

	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnRcd = (TextView) findViewById(R.id.btn_rcd);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		
		mImageButton = (ImageButton) findViewById(R.id.chat_right_btn_id);
		
		mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);
		chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);
		volume = (ImageView) this.findViewById(R.id.volume);
		
		mBtnSend.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mImageButton.setOnClickListener(this);
		
		rcChat_popup = this.findViewById(R.id.rcChat_popup);
		img1 = (ImageView) this.findViewById(R.id.img1);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		del_re = (LinearLayout) this.findViewById(R.id.del_re);
		voice_rcd_hint_rcding = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_tooshort);
		mSensor = new SoundMeter();
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

		// ���������л���ť
		chatting_mode_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (btn_vocie) {
					mBtnRcd.setVisibility(View.GONE);
					mBottom.setVisibility(View.VISIBLE);
					btn_vocie = false;
					chatting_mode_btn
							.setImageResource(R.drawable.chatting_setmode_msg_btn);

				} else {
					mBtnRcd.setVisibility(View.VISIBLE);
					mBottom.setVisibility(View.GONE);
					chatting_mode_btn
							.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_vocie = true;
				}
			}
		});
		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// ��������¼�ư�ťʱ����falseִ�и���OnTouch
				return false;
			}
		});
	}

	private String[] msgArray = new String[] { "���˾��ж�Թ", "�ж�Թ���н���", "�˾��ǽ���",
			"����ô�˳��� ", "�����г������ɺ�", "����ƽ����Ҳ�����ཻ��һ�졣" };

	private String[] dataArray = new String[] { "2012-10-31 18:00",
			"2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20",
			"2012-10-31 18:30", "2012-10-31 18:35" };
	private final static int COUNT = 6;

	// public void initData() {
	// for (int i = 0; i < COUNT; i++) {
	// ChatMsgEntity entity = new ChatMsgEntity();
	// entity.setDate(dataArray[i]);
	// if (i % 2 == 0) {
	// entity.setName("�׸���");
	// entity.setMsgType(true);
	// } else {
	// entity.setName("�߸�˧");
	// entity.setMsgType(false);
	// }
	//
	// entity.setText(msgArray[i]);
	// mDataArrays.add(entity);
	// }
	//
	// mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
	// mListView.setAdapter(mAdapter);
	//
	// // date = new Date();
	//
	// }

	public void setData() {
		allMsgExList.clear();
		mDataArrays.clear();
		allMsgExList = msgExHandle.FetchAll();
		for (int i = 0; i < allMsgExList.size(); i++) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(allMsgExList.get(i).getCreateTime());
			if (allMsgExList.get(i).getIsSend() == 1) {
				entity.setName("�߸�˧");
				entity.setMsgType(false);
			} else {
				entity.setName("�׸���");
				entity.setMsgType(true);
			}
			entity.setText(allMsgExList.get(i).getContent());
			mDataArrays.add(entity);
		}

		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		//mListView.setSelection(mListView.getCount() - 1);
		//mListView.deferNotifyDataSetChanged();
		// date = new Date();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.chat_right_btn_id:
			delchat();
			break;
		}
	}

	private void delchat(){
		msgExHandle.DelAllMsgEx();	
		mDataArrays.clear();
		mAdapter.notifyDataSetChanged();
		//mListView.setSelection(mListView.getCount() - 1);
		//setData();
	}
	private void send() {
		contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("�߸�˧");
			entity.setMsgType(false);
			entity.setText(contString);

			// Friend friend = mFrienddbFun.getSingleFriend(mFriendDB,
			// position);
			MessagePacket packet = new MessagePacket("chat", "han", null,
					friendnub, contString);
			boolean isSend = SendService.sendData(packet);
			// boolean isSend = sendToSevice("check", username, null,
			// deviceNumber, null);
			if (!isSend) {
				Toast.makeText(this, "δ��������,����...", 2000).show();
			} else {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int mm = 1;
						long flag = msgExHandle.InsertMsg(mm, mm, mm, "123",
								"2015-10-13-14:53", friendnub, contString,
								"path", "nothing");

					}
				}).start();
			}

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();

			mEditTextContent.setText("");

			mListView.setSelection(mListView.getCount() - 1);

		}
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	// ��������¼�ư�ťʱ
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
			return false;
		}

		if (btn_vocie) {
			System.out.println("1");
			int[] location = new int[2];
			mBtnRcd.getLocationInWindow(location); // ��ȡ�ڵ�ǰ�����ڵľ�������
			int btn_rc_Y = location[1];
			int btn_rc_X = location[0];
			int[] del_location = new int[2];
			del_re.getLocationInWindow(del_location);
			int del_Y = del_location[1];
			int del_x = del_location[0];
			if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				System.out.println("2");
				if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {// �ж����ư��µ�λ���Ƿ�������¼�ư�ť�ķ�Χ��
					System.out.println("3");
					mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					rcChat_popup.setVisibility(View.VISIBLE);
					voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					voice_rcd_hint_tooshort.setVisibility(View.GONE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								voice_rcd_hint_loading.setVisibility(View.GONE);
								voice_rcd_hint_rcding
										.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					date = new Date();
					startVoiceT = date.getTime();
					// startVoiceT = SystemClock.currentThreadTimeMillis();
					voiceName = startVoiceT + ".amr";
					start(voiceName);
					flag = 2;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// �ɿ�����ʱִ��¼�����
				System.out.println("4");
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					rcChat_popup.setVisibility(View.GONE);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					stop();
					flag = 1;
					File file = new File(
							android.os.Environment
									.getExternalStorageDirectory()
									+ "/"
									+ voiceName);
					if (file.exists()) {
						file.delete();
					}
				} else {

					voice_rcd_hint_rcding.setVisibility(View.GONE);
					stop();
					date = new Date();
					endVoiceT = date.getTime();
					// endVoiceT = SystemClock.currentThreadTimeMillis();
					flag = 1;
					int time = (int) ((endVoiceT - startVoiceT) / 1000);
					// System.out.println("startVoiceT:"+startVoiceT);
					// System.out.println("endVoiceT:"+endVoiceT);
					// System.out.println("time:"+time);
					if (time < 1) {
						isShosrt = true;
						voice_rcd_hint_loading.setVisibility(View.GONE);
						voice_rcd_hint_rcding.setVisibility(View.GONE);
						voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
						mHandler.postDelayed(new Runnable() {
							public void run() {
								voice_rcd_hint_tooshort
										.setVisibility(View.GONE);
								rcChat_popup.setVisibility(View.GONE);
								isShosrt = false;
							}
						}, 500);
						return false;
					}
					ChatMsgEntity entity = new ChatMsgEntity();
					entity.setDate(getDate());
					entity.setName("�߸�˧");
					entity.setMsgType(false);
					entity.setTime(time + "\"");
					entity.setText(voiceName);
					mDataArrays.add(entity);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mListView.getCount() - 1);
					rcChat_popup.setVisibility(View.GONE);

				}
			}
			if (event.getY() < btn_rc_Y) {// ���ư��µ�λ�ò�������¼�ư�ť�ķ�Χ��
				System.out.println("5");
				Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc);
				Animation mBigAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc2);
				img1.setVisibility(View.GONE);
				del_re.setVisibility(View.VISIBLE);
				del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
					sc_img1.startAnimation(mLitteAnimation);
					sc_img1.startAnimation(mBigAnimation);
				}
			} else {
				img1.setVisibility(View.VISIBLE);
				del_re.setVisibility(View.GONE);
				del_re.setBackgroundResource(0);
			}
		}
		return super.onTouchEvent(event);
	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	public void head_xiaohei(View v) { // ������ ���ذ�ť

	}

	private class MessageBackReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BCH_CONSTANT.TYPE.CHAT)) {
				String message = intent.getStringExtra("message");
				// Toast.makeText(ChatActivity.this, message, Toast.LENGTH_LONG)
				// .show();
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(getDate());
				entity.setName("�׸���");
				entity.setMsgType(true);
				entity.setText(message);
				mDataArrays.add(entity);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mListView.getCount() - 1);
			} else {

			}
		};
	}

}