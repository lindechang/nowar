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

public class SetDeviceNameActivity extends Activity implements OnClickListener {
	private int pos;
	private Button save;
	private Button back;
	private EditText editText;
	private SwDB db;
	private SwdbFun fun;
	private SwDevice device;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setdevicename_view);
		Intent intent = getIntent();
		pos = intent.getIntExtra("position", 0);

		db = new SwDB(this);
		fun = new SwdbFun();
		device = fun.getSingleSW(db, pos);

		save = (Button) findViewById(R.id.set_device_save_but_id);
		back = (Button) findViewById(R.id.set_device_name_back_id);
		editText = (EditText) findViewById(R.id.set_device_name_editText_id);
		editText.setText(device.getSwdeviceName());
		editText.setSelection(device.getSwdeviceName().length());

		save.setOnClickListener(this);
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.equals(save)) {
			String new_name = editText.getText().toString().trim();
			if (new_name != null && !new_name.equals("")) {
				boolean flag = fun.Update(db, device.getSwdeviceNumber(),
						SwDB.SWDEVICE_NAME, new_name);
				if (flag) {
					finish();
				}
			} else {
				Toast.makeText(this, "名称不能为空", 2000).show();
			}

		} else if (v.equals(back)) {
			finish();
		}
	}

}
