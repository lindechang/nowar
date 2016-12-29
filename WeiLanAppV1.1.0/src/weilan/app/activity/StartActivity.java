package weilan.app.activity;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;




import weilan.app.data.StaticVariable;
import weilan.app.main.R;
import weilan.app.service.ConnectorService;
import weilan.app.tools.mina.ClientListener;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {

	//private SharedPreferences sharedPreferences;
	private Map<String, String> loginMap;
	private String name;
	private String pass;
	private String retStr = "";
	// 需要将下面的IP改为服务器端IP
	private String url = "http://mikimao.vicp.cc:8080/XhsServers/servlet/LoginAction";

	// private Socket socket;

	PrintWriter writer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.start);
		setContentView(R.layout.start_view);

		// TcpService.intent = new Intent(getApplicationContext(),
		// TcpService.class);
		// startService(TcpService.intent);
		//new ConnectorService(this);
		ConnectorService.intent = new Intent(getApplicationContext(), ConnectorService.class);
		startService(ConnectorService.intent);
		//new ClientListener();

		//ClientListener.sharedPreferences = getApplicationContext().getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
		loginMap = new HashMap<String, String>();
		name = SharedPrefsUtil.getValue(this, "userName", "");
				//ClientListener.sharedPreferences.getString("userName", "");
		pass = SharedPrefsUtil.getValue(this, "passWord", "");
				//ClientListener.sharedPreferences.getString("passWord", "");
		// System.out.println("name-----:" + name);
		// System.out.println("pass-----:" + pass);
		loginMap.put("username", name);
		loginMap.put("pswd", pass);

		if (SharedPrefsUtil.getValue(this,"isAutoLogin", false)) {
			new Handler().postDelayed(new Runnable() {
				// @Override
				public void run() {
					Intent intent = new Intent();
					Bundle tokenBun = new Bundle();
					tokenBun.putString("token", "SSSSSSSSS");// 需要修改
					intent.putExtras(tokenBun);
					intent.setClass(StartActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}, 2000);
			// remberPassword.setChecked(true);
			// new MyTask().execute("");
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(StartActivity.this,
							LoginActivity.class);
					// Intent intent = new Intent(StartActivity.this,
					// TestActivity.class);
					
					startActivity(intent);
					finish();
				}
			}, 2000);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	

}
