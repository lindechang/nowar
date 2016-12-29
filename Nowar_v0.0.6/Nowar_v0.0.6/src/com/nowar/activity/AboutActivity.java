package com.nowar.activity;

import com.lindec.app.tools.VersionUtils;
import com.nowar.main.BaseActivity;
import com.nowar.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity implements OnClickListener {

	Button back;
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_company);
		text = (TextView) findViewById(R.id.about_wl_version_text_id);
		text.setText("微蓝" + VersionUtils.getVersionUtils().getVersion(this)
				+ "版本");
		back = (Button) findViewById(R.id.about_wl_back_id);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(back)) {
			finish();
		}
	}

}
