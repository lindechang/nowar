package weilan.app.activity;

import weilan.app.db.SwDB;
import weilan.app.db.SwDevice;
import weilan.app.db.SwdbFun;
import weilan.app.main.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserInfoActivity extends Activity implements OnClickListener {
	private int pos;
	private Button save;
	private Button back;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_view);
		Intent intent = getIntent();
		pos = intent.getIntExtra("position", 0);	
		back = (Button) findViewById(R.id.set_user_info_back_id);	
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if(v.equals(back)){
			finish();
		}
	}
}
