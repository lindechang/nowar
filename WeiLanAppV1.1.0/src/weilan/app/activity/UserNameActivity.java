package weilan.app.activity;

import weilan.app.data.StaticVariable;
import weilan.app.db.SwDB;
import weilan.app.db.SwDevice;
import weilan.app.db.SwdbFun;
import weilan.app.main.R;
import weilan.app.tools.mina.ClientListener;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
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

public class UserNameActivity extends Activity implements OnClickListener {
	private String mUser;
	private Button save;
	private Button back;
	private EditText editText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.username_view);
		Intent intent = getIntent();
		mUser = intent.getStringExtra("user");
		
		save = (Button) findViewById(R.id.set_user_save_but_id);	
		back = (Button) findViewById(R.id.set_user_name_back_id);
		editText = (EditText) findViewById(R.id.set_user_name_editText_id);
		editText.setText(mUser);
		editText.setSelection(mUser.length());
		save.setOnClickListener(this);
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if(v.equals(back)){
			finish();
		}else if(v.equals(save)){
			// Editor editor = ClientListener.sharedPreferences.edit();
			// editor.putString("user", editText.getText().toString().trim());
			// editor.commit();
			String userName = SharedPrefsUtil.getValue(this, "userName", "");
			SharedPrefsUtil.putValue(this, userName, editText.getText().toString().trim());
			finish();
		}
	}
}
