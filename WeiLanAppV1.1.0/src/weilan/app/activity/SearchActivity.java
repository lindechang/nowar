package weilan.app.activity;

import java.util.HashMap;
import java.util.Map;

import com.baidu.platform.comapi.map.m;

import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.data.StaticVariable;
import weilan.app.main.R;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.mina.ClientHandler;
import weilan.app.tools.mina.SendService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {
	private TextView mTextView;
	private EditText mEditText;
	private Button back;
	private RelativeLayout rlayout;
	public static Activity TAG = null;
	private String type; // 设备类 、好友类
	private String goal;

	private class MessageBackReciver extends BroadcastReceiver {
		// private WeakReference<TextView> textView;

		public MessageBackReciver() {

		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BCH_CONSTANT.ACTION)) {
				String message = intent.getStringExtra("message");
				if (type.equals("friend")) {
					if (message.equals("check_friend_success")) {
						Intent mintent = new Intent();
						mintent.putExtra("friendNumber", goal);
						mintent.putExtra("type", type);
						mintent.setClass(SearchActivity.this,
								BindingAcitivity.class);
						startActivity(mintent);
						// Toast.makeText(SearchActivity.this, "查询用户成功",
						// Toast.LENGTH_SHORT).show();
					} else if (message.equals("check_friend_fail")) {
						Toast.makeText(SearchActivity.this, "未查询到该用户",
								Toast.LENGTH_SHORT).show();
					}
				} else if (type.equals("device")) {
					if (message.equals("check_success")) {
						Intent mintent = new Intent();
						mintent.putExtra("deviceNumber", goal);
						mintent.putExtra("type", type);
						mintent.setClass(SearchActivity.this,
								BindingAcitivity.class);
						startActivity(mintent);
					} else if (message.equals("check_fail")) {
						Toast.makeText(SearchActivity.this, "设备未入网或已绑定主人",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {

			}
		};
	}

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_search);
		TAG = this;

		Intent intent = getIntent();
		type = intent.getStringExtra("type");

		mTextView = (TextView) findViewById(R.id.add_tv);
		mEditText = (EditText) findViewById(R.id.add_ET);
		back = (Button) findViewById(R.id.add_search_back_id);
		rlayout = (RelativeLayout) findViewById(R.id.add_search_tt_id);

		mEditText.addTextChangedListener(mTextWatcher);
		rlayout.setOnClickListener(this);
		back.setOnClickListener(this);

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		mReciver = new MessageBackReciver();
		// mServiceIntent = new Intent(this, BackService.class);
		mReciver.isInitialStickyBroadcast();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BCH_CONSTANT.ACTION);
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = mEditText.getSelectionStart();
			editEnd = mEditText.getSelectionEnd();
			if ((temp.length() > 0) && (temp.length() <= 16)) {
				rlayout.setVisibility(LinearLayout.VISIBLE);
				mTextView.setText(temp);
				goal = mTextView.getText().toString().trim();
			} else if (temp.length() > 16) {
				Toast.makeText(SearchActivity.this, "你输入的字数已经超过了限制！",
						Toast.LENGTH_SHORT).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mEditText.setText(s);
				mEditText.setSelection(tempSelection);
				mTextView.setText(s);
			} else {
				rlayout.setVisibility(LinearLayout.GONE);
			}
			// mTextView.setText("您输入了" + temp.length() + "个字符");
			// if (temp.length() > 10) {

			// }
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
	}

	protected void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	public void onClick(View v) {
		SharedPreferences sharedPreferences = getSharedPreferences("loginInfo",
				this.MODE_PRIVATE);
		String username = sharedPreferences.getString("userName", "");
		if (v.equals(rlayout)) {

			if (type.equals("friend")) {
				MessagePacket packet = new MessagePacket("check_newfriend",
						username, null, goal, null);
				boolean isSend = SendService.sendData(packet);
				// boolean isSend = sendToSevice("check", username, null,
				// deviceNumber, null);
				if (!isSend) {
					Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				}
			} else if (type.equals("device")) {
				MessagePacket packet = new MessagePacket("check", username,
						null, goal, null);
				boolean isSend = SendService.sendData(packet);
				// boolean isSend = sendToSevice("check", username, null,
				// deviceNumber, null);
				if (!isSend) {
					Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				}
			}

		}
		if (v.equals(back)) {
			finish();
		}
	}

	// public synchronized boolean sendToSevice(String type, String username,
	// String password, String devicenumber, String content) {
	// boolean flag = false;
	// if (ClientHandler.session == null) {
	// return false;
	// }
	// try {
	// // PrintWriter writer = new PrintWriter(
	// // TcpService.socket.getOutputStream(), true);
	// PacketService service = new PacketService();
	// String msg = FastJsonTools.createFastJsonString(service.setPacket(
	// type, username, password, devicenumber, content));
	// // out.println(msg);
	// if (!ClientHandler.session.isClosing()
	// && ClientHandler.session.isConnected()) {
	// ClientHandler.session.write(msg);
	// flag = true;
	// } else {
	// flag = false;
	// }
	// } catch (Exception e) {
	// flag = false;
	// System.out.println(e.toString());
	// }
	// return flag;
	// }
}