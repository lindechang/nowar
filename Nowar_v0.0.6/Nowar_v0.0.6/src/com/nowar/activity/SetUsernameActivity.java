package com.nowar.activity;

import com.nowar.main.BaseActivity;
import com.nowar.main.R;
import com.nowar.sharedprefs.SharedPrefsUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetUsernameActivity extends BaseActivity implements OnClickListener {
	private String mUserName;
	private Button save;
	private Button back;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_username);
		Intent intent = getIntent();
		mUserName = intent.getStringExtra("userName");

		save = (Button) findViewById(R.id.set_user_save_but_id);
		back = (Button) findViewById(R.id.set_user_name_back_id);
		editText = (EditText) findViewById(R.id.set_user_name_editText_id);
		editText.setText(mUserName);
		editText.setSelection(mUserName.length());
		save.setOnClickListener(this);
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.equals(back)) {
			finish();
		} else if (v.equals(save)) {
			// Editor editor = ClientListener.sharedPreferences.edit();
			// editor.putString("user", editText.getText().toString().trim());
			// editor.commit();
			String userNumber = SharedPrefsUtil.getSharedPrefsUtil().getValue(
					this, "userNumber", "");
			SharedPrefsUtil.getSharedPrefsUtil().putValue(this, userNumber,
					editText.getText().toString().trim());
			finish();
		}
	}
}
