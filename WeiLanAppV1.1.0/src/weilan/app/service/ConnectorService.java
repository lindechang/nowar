package weilan.app.service;

import java.util.List;

import weilan.app.activity.MainActivity;
import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.data.StaticVariable;
import weilan.app.main.R;
import weilan.app.notification.MyNotification;
import weilan.app.tools.mina.ClientListener;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class ConnectorService extends Service {

	public static Context context;
	public static Intent intent = null;
	// �����ж�֪ͨ�����Ƿ�����
	private String Process_Name = "weilan.app.main��notifyservice";

	/** Notification������ */
	public static NotificationCompat.Builder mBuilder;
	/** Notification��ID */
	public static int notifyId = 100;

	/** Notification���� */
	public static NotificationManager mNotificationManager;

	/**
	 * ����ConnectorService
	 */
	private StrongService notifyService = new StrongService.Stub() {

		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), NotifyService.class);
			getBaseContext().stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), NotifyService.class);
			getBaseContext().startService(i);

		}
	};

	@Override
	public void onTrimMemory(int level) {
		// Toast.makeText(getBaseContext(), "ConnectorService onTrimMemory..." +
		// level,
		// Toast.LENGTH_SHORT).show();
		keepNotifyService();
	}

	/**
	 * �ж�NotifyService�Ƿ������У��������������NotifyService
	 */
	private void keepNotifyService() {
		boolean isRun = ServiceUtils.isProessRunning(ConnectorService.this,
				Process_Name);
		if (isRun == false) {
			try {
				// Toast.makeText(getBaseContext(), "�������� NotifyService",
				// Toast.LENGTH_SHORT).show();
				notifyService.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public ConnectorService() {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return (IBinder) notifyService;
	}

	public void onCreate() {
		super.onCreate();
		// StaticVariable.sharedPreferences = getSharedPreferences("loginInfo",
		// MODE_PRIVATE);
		// new InitSocketThread().start();
		// mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		// System.setProperty("java.net.preferIPv6Addresses", "false");
		context = getApplicationContext();
		new ClientListener(context).start();
		// new Relink().start();
		//mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		keepNotifyService();
		// ������
		// Intent notificationIntent = new Intent(this, MainActivity.class);
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);
		// Notification notification = new Notification();
		// notification.icon = R.drawable.ic_launcher;// ����֪ͨ��ͼ��
		// notification.when = System.currentTimeMillis();
		// notification.tickerText = "��֪ͨ��";
		// notification.contentIntent = pendingIntent;
		// //startForeground() �������ǽ�������Ϊǰ̨���񡣲���1�������֪ͨΨһ��id��ֻҪ��Ϊ0����
		// startForeground(1, notification);

	   System.out.println("-wo shi serivce oncreate-->>");
		new MyNotification(context);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("-wo shi serivce onStartCommand-->>");

		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		// ������
		// stopForeground(true);
		// startService(intent);

		System.out.println("����Service,�ҽ������١�");
		super.onDestroy();
	}
}
