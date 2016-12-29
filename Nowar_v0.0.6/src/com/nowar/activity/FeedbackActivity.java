package com.nowar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nowar.main.BaseActivity;
import com.nowar.main.R;
import com.nowar.mina.SendService;
import com.nowar.packet.MessagePacket;
import com.nowar.sharedprefs.SharedPrefsUtil;
import com.nowar.ui.ChatMsgEntity;
import com.nowar.ui.ChatMsgViewAdapter;

public class FeedbackActivity extends BaseActivity implements OnClickListener {

	private Button sendBut;
	private Button back;
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		sendBut = (Button) findViewById(R.id.btn_send_feedback);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		mListView = (ListView) findViewById(R.id.listview);
		back = (Button) findViewById(R.id.feedback_back);
		sendBut.setOnClickListener(this);
		back.setOnClickListener(this);
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);

		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

	@Override
	public void onClick(View v) {
		if (v.equals(sendBut)) {
			String contString = mEditTextContent.getText().toString();
			if (contString.length() > 0) {
				String userNumber = SharedPrefsUtil.getSharedPrefsUtil()
						.getValue(this, "userNumber", "");
				String userName = SharedPrefsUtil.getSharedPrefsUtil()
						.getValue(this, userNumber, "");
				if (userName != null && !userName.equals("")) {
				} else {
					userName = "新用户";
				}
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(getDate());
				entity.setName(userName);
				entity.setMsgType(false);
				entity.setText(contString);

				// MessagePacket packet = new MessagePacket("chat", "han", null,
				// null, contString);
				// boolean isSend = SendService.sendData(packet);
				// if (!isSend) {
				// Toast.makeText(this, "未连接网络,请检查...", 2000).show();
				// } else {
				//
				// }

				mDataArrays.add(entity);
				mAdapter.notifyDataSetChanged();

				mEditTextContent.setText("");

				mListView.setSelection(mListView.getCount() - 1);

			}
		} else if (v.equals(back)) {
			finish();
		}

	}

}
