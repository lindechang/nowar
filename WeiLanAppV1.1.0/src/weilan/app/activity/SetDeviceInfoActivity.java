package weilan.app.activity;

import weilan.app.db.SwDB;
import weilan.app.db.SwDevice;
import weilan.app.db.SwdbFun;
import weilan.app.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetDeviceInfoActivity extends Activity implements OnClickListener {

	private Button back;
	private LinearLayout layout;
	private int pos;
	private TextView name;
	private TextView number;
	private TextView type;
	private TextView cs;

	private SwDevice device;
	private SwDB mSwDB;
	private SwdbFun fun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setdeviceinfo_view);
		Intent intent = getIntent();
		pos = intent.getIntExtra("position", 0);
		// System.out.println("pos:" + pos);

	    back = (Button) findViewById(R.id.set_device_info_back_id);
		layout = (LinearLayout) findViewById(R.id.set_device_info_layout1);
		back.setOnClickListener(this);
		layout.setOnClickListener(this);
		initview();
	}

	private void initview() {
		
		name = (TextView) findViewById(R.id.set_device_info_name_id);
		number = (TextView) findViewById(R.id.set_device_info_number_id);
		type = (TextView) findViewById(R.id.set_device_info_type_id);
		cs = (TextView) findViewById(R.id.set_device_info_cs_id);

		device = new SwDevice();
		mSwDB = new SwDB(this);
		fun = new SwdbFun();
		device = fun.getSingleSW(mSwDB, pos);
		name.setText(device.getSwdeviceName());
		number.setText(device.getSwdeviceNumber());
		type.setText("短路开关");
		cs.setText("微蓝科技");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(back)) {
			finish();
		} else if (v.equals(layout)) {
			Intent mintent = new Intent();
			mintent.putExtra("position", pos);
			mintent.setClass(this, SetDeviceNameActivity.class);
			startActivity(mintent);

		}
	}

}
