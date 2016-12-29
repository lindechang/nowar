package weilan.app.notification;

import weilan.app.main.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class MyNotification {

	/** Notification构造器 */
	public static NotificationCompat.Builder mBuilder;
	/** Notification的ID */
	public static int notifyId = 100;

	/** Notification管理 */
	public static NotificationManager mNotificationManager;

	private Context context;

	public MyNotification(Context context) {
		this.context = context;
		initService();
		initNotify();
	}

	/** 初始化通知栏 */
	private void initNotify() {
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle("测试标题")
				.setContentText("测试内容")
				.setContentIntent(
						getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
				// .setNumber(number)//显示数量
				.setTicker("测试通知来啦")// 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
				// requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);
	}

	/** 显示通知栏 */
	public void showNotify() {
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// new Intent(this, MainActivity.class), 0);
		mBuilder.setContentTitle("测试标题").setContentText("测试内容")
		// .setContentIntent(pendingIntent)
		// .setNumber(number)//显示数量
				.setTicker("测试通知来啦");// 通知首次出现在通知栏，带上升动画效果的
		mNotificationManager.notify(notifyId, mBuilder.build());
		// mNotification.notify(getResources().getString(R.string.app_name),
		// notiId, mBuilder.build());
	}

	/**
	 * 显示通知栏点击跳转到指定Activity
	 * 
	 * @param <T>
	 */
	public static <T> void showIntentActivityNotify(Context context,
			Class<T> cls) {
		// Notification.FLAG_ONGOING_EVENT --设置常驻
		// Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// //在通知栏上点击此通知后自动清除此通知
		mBuilder.setAutoCancel(true)// 点击后让通知将消失
				.setContentTitle("测试标题").setContentText("点击跳转").setTicker("点我");
		// 点击的意图ACTION是跳转到Intent
		Intent resultIntent = new Intent(context, cls);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);
		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	/**
	 * 初始化要用到的系统服务
	 */
	private void initService() {
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public PendingIntent getDefalutIntent(int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
				new Intent(), flags);
		return pendingIntent;
	}
}
