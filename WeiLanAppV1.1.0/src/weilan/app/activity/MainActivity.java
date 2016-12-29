package weilan.app.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import weilan.app.db.CreateDB;
import weilan.app.main.R;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import weilan.app.ui.FragmentTabAdapter;
import weilan.app.ui.Tab_FW;
import weilan.app.ui.Tab_SW;
import weilan.app.ui.Tab_DW;
import weilan.app.ui.Tab_WO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

public class MainActivity extends FragmentActivity implements OnClickListener {
	weilan.app.main.MyRadioButton fe;
	//weilan.app.main.MyRadioButton dw;
	weilan.app.main.MyRadioButton sw;
	weilan.app.main.MyRadioButton wd;

	public static Activity TAG = null;
	private RadioGroup rgs;
	private List<Fragment> fragments = new ArrayList<Fragment>();

	private CreateDB mTrackerDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabmain);
		TAG = this;

		SharedPrefsUtil.putValue(this, "test", "lindec");

		fe = (weilan.app.main.MyRadioButton) findViewById(R.id.tab_f_id);
		sw = (weilan.app.main.MyRadioButton) findViewById(R.id.tab_a_id);
		wd = (weilan.app.main.MyRadioButton) findViewById(R.id.tab_c_id);
		fe.setOnClickListener(this);
		//dw.setOnClickListener(this);
		sw.setOnClickListener(this);
		wd.setOnClickListener(this);

		mTrackerDB = new CreateDB(this);
		mTrackerDB.Open();
		
		fragments.add(new Tab_SW());
		fragments.add(new Tab_FW());
		//fragments.add(new Tab_DW());
		// fragments.add(new TabCFm());
		fragments.add(new Tab_WO());
		// fragments.add(new TabEFm());
		rgs = (RadioGroup) findViewById(R.id.rgs);

		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments,
				R.id.tab_content_id, rgs);
		tabAdapter
				.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
					@Override
					public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
							int checkedId, int index) {
						System.out.println("Extra---- " + index
								+ " checked!!! ");
					}
				});

	}

	@Override
	public void onClick(View v) {
		
		// else if (v.equals(dw)) {
		// fe.setTextColor(this.getResources().getColor(R.color.white));
		// //dw.setTextColor(this.getResources().getColor(R.color.darkskyblue));
		// sw.setTextColor(this.getResources().getColor(R.color.white));
		// wd.setTextColor(this.getResources().getColor(R.color.white));
		// }
		 if (v.equals(sw)) {
			fe.setTextColor(this.getResources().getColor(R.color.white));
			//dw.setTextColor(this.getResources().getColor(R.color.white));
			sw.setTextColor(this.getResources().getColor(R.color.darkskyblue));
			wd.setTextColor(this.getResources().getColor(R.color.white));
		} else if (v.equals(wd)) {
			fe.setTextColor(this.getResources().getColor(R.color.white));
			//dw.setTextColor(this.getResources().getColor(R.color.white));
			sw.setTextColor(this.getResources().getColor(R.color.white));
			wd.setTextColor(this.getResources().getColor(R.color.darkskyblue));
		}else if (v.equals(fe)) {
			fe.setTextColor(this.getResources().getColor(R.color.darkskyblue));
			//dw.setTextColor(this.getResources().getColor(R.color.white));
			sw.setTextColor(this.getResources().getColor(R.color.white));
			wd.setTextColor(this.getResources().getColor(R.color.white));
		} 

	}

	// @Override
	// public void onBackPressed() {
	// // 实现Home键效果
	// // super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
	// //System.out.println("back键监听成功");
	//
	// // Intent intent = new Intent(Intent.ACTION_MAIN);
	// // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// // intent.addCategory(Intent.CATEGORY_HOME);
	// // startActivity(intent);
	//
	// }

	@Override
	protected void onResume() {
		// System.out.println("onResume");
		super.onResume();
	}

	@Override
	protected void onStop() {
		// System.out.println("onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// System.out.println("onDestroy");
		super.onDestroy();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		// super.finish();
		// System.out.println("finish");
		moveTaskToBack(true);// 设置该activity永不过期
	}

}
