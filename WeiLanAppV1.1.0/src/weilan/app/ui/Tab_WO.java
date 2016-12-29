package weilan.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weilan.app.activity.AboutActivity;
import weilan.app.activity.SetheadActivity;
import weilan.app.activity.SwOperateActivity;
import weilan.app.activity.UserNameActivity;
import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.data.StaticVariable;
import weilan.app.main.R;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.fastjosn.FastJsonService;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.mina.ClientHandler;
import weilan.app.tools.mina.ClientListener;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import weilan.app.version.UpdateVersions;
import weilan.app.version.VersionUtils;
//import yundian.tracker.tcp.TcpService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA. Author: wangjie email:wangjie@cyyun.com Date:
 * 13-6-14 Time: 下午2:39
 */
public class Tab_WO extends Fragment implements OnClickListener,
		OnItemClickListener {

	private ListView aboutListview;
	private TableRow exit;
	private TableRow aboutWL;
	private TableRow newWL;

	private TextView name;
	private TextView number;

	private LinearLayout layout;
	private ImageView head_image;
	private Bitmap bt;
	private String path;
	private String userName;

	private class MessageBackReciver extends BroadcastReceiver {
		// private WeakReference<TextView> textView;

		public MessageBackReciver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BCH_CONSTANT.ACTION)) {
				String message = intent.getStringExtra("message");
				// ContentPacket packet = FastJsonService
				// .getPerson(message, ContentPacket.class);
				System.out.println("-接收到广播来的message-->>" + message);

				if (message.equals("old_version")) {
					Toast.makeText(getActivity(), "已是最新版本", Toast.LENGTH_LONG)
							.show();
				} else if (message.equals("new_version")) {
					System.out.println("进入更新");
					UpdateVersions mUpdate = new UpdateVersions(getActivity());
					mUpdate.checkUpdateInfo();
				}
			} else {

			}
		};
	}

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	// private SharedPreferences sharedPreferences;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("DDDDDDDDD____onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("DDDDDDDDD____onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("DDDDDDDDD____onCreateView");
		return inflater.inflate(R.layout.about_view, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// exitBtn = (Button)
		// this.getView().findViewById(R.id.about_exitbtn_id); // 设备添加按键
		// exitBtn.setOnClickListener(this);

		name = (TextView) this.getView().findViewById(R.id.about_name_id);
		number = (TextView) this.getView().findViewById(R.id.about_number_id);
		exit = (TableRow) this.getView().findViewById(R.id.about_exit_id);
		aboutWL = (TableRow) this.getView().findViewById(R.id.more_page_row6);
		newWL = (TableRow) this.getView().findViewById(R.id.more_page_row5);
		layout = (LinearLayout) this.getView().findViewById(
				R.id.about_user_info_layout);

		exit.setOnClickListener(this);
		aboutWL.setOnClickListener(this);
		newWL.setOnClickListener(this);
		layout.setOnClickListener(this);

		head_image = (ImageView) this.getView()
				.findViewById(R.id.head_image_id);
		head_image.setOnClickListener(this);
		userName = SharedPrefsUtil.getValue(getActivity(), "userName", "");
		path = "/sdcard/myHead/" + userName + "/";// sd路径
		bt = BitmapFactory.decodeFile(path + "head.jpg");// 从Sd中找头像，转换成Bitmap
		if (bt != null) {
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
			head_image.setImageDrawable(drawable);
		} else {
			/**
			 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
			 * 
			 */
		}

		// sharedPreferences = getActivity().getSharedPreferences("loginInfo",
		// getActivity().MODE_PRIVATE);

		String user = SharedPrefsUtil.getValue(getActivity(), userName, "");
		if (user != null && !user.equals("")) {
			name.setText(user);
		}
		number.setText("账号:" + userName);

		mLocalBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		mReciver = new MessageBackReciver();
		// mServiceIntent = new Intent(this, BackService.class);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BCH_CONSTANT.ACTION);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		bt = BitmapFactory.decodeFile(path + "head.jpg");
		if (bt != null) {
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
			head_image.setImageDrawable(drawable);
		} else {
			/**
			 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
			 * 
			 */
		}
		String user = SharedPrefsUtil.getValue(getActivity(), userName, "");
		if (user != null && !user.equals("")) {
			name.setText(user);
		}
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);

	}

	@Override
	public void onPause() {
		super.onPause();
		mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.about_exit_id) {
			getActivity().getApplicationContext().stopService(
					ConnectorService.intent);

			// Editor editor = ClientListener.sharedPreferences.edit();
			// editor.putBoolean("isAutoLogin", false);
			// editor.commit();
			SharedPrefsUtil.putValue(getActivity(), "isAutoLogin", false);
			getActivity().finish();
			System.exit(0);
		}

		if (v.equals(aboutWL)) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), AboutActivity.class);
			getActivity().startActivity(intent);

		}
		if (v.equals(newWL)) {
			// VersionUtils.getVersion(getActivity());
			// VersionUtils.getVersionCode(getActivity());
			sendToSevice("check_version", null, null, null,
					VersionUtils.getVersion(getActivity()));
			// 下载更新�?
			// UpdateVersions mUpdate = new UpdateVersions(getActivity());
			// mUpdate.checkUpdateInfo();
		}
		if (v.equals(head_image)) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), SetheadActivity.class);
			startActivity(intent);
		}
		if (v.equals(layout)) {
			Intent intent = new Intent();
			intent.putExtra("user", name.getText().toString().trim());
			intent.setClass(getActivity(), UserNameActivity.class);
			startActivity(intent);
		}

	}

	public synchronized boolean sendToSevice(String type, String username,
			String password, String devicenumber, String content) {
		boolean flag = false;
		if (ClientHandler.session == null) {
			return false;
		}
		try {
			// PrintWriter writer = new PrintWriter(
			// TcpService.socket.getOutputStream(), true);
			PacketService service = new PacketService();
			String msg = FastJsonTools.createFastJsonString(service.setPacket(
					type, username, password, devicenumber, content));
			// out.println(msg);
			if (!ClientHandler.session.isClosing()
					&& ClientHandler.session.isConnected()) {
				ClientHandler.session.write(msg);
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			System.out.println(e.toString());
		}
		return flag;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position != ListView.INVALID_POSITION) {
			if (position == 3) {
				SharedPreferences sharedPreferences = getActivity()
						.getSharedPreferences("loginInfo",
								getActivity().MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.putBoolean("isAutoLogin", false);
				editor.commit();
				getActivity().finish();
			}
		}
	}

}
