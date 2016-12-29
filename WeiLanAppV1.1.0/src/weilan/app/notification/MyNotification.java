package weilan.app.notification;

import weilan.app.main.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class MyNotification {

	/** Notification������ */
	public static NotificationCompat.Builder mBuilder;
	/** Notification��ID */
	public static int notifyId = 100;

	/** Notification���� */
	public static NotificationManager mNotificationManager;

	private Context context;

	public MyNotification(Context context) {
		this.context = context;
		initService();
		initNotify();
	}

	/** ��ʼ��֪ͨ�� */
	private void initNotify() {
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle("���Ա���")
				.setContentText("��������")
				.setContentIntent(
						getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
				// .setNumber(number)//��ʾ����
				.setTicker("����֪ͨ����")// ֪ͨ�״γ�����֪ͨ��������������Ч����
				.setWhen(System.currentTimeMillis())// ֪ͨ������ʱ�䣬����֪ͨ��Ϣ����ʾ
				.setPriority(Notification.PRIORITY_DEFAULT)// ���ø�֪ͨ���ȼ�
				// .setAutoCancel(true)//���������־���û��������Ϳ�����֪ͨ���Զ�ȡ��
				.setOngoing(false)// ture��������Ϊһ�����ڽ��е�֪ͨ������ͨ����������ʾһ����̨����,�û���������(�粥������)����ĳ�ַ�ʽ���ڵȴ�,���ռ���豸(��һ���ļ�����,ͬ������,������������)
				.setDefaults(Notification.DEFAULT_VIBRATE)// ��֪ͨ������������ƺ���Ч������򵥡���һ�µķ�ʽ��ʹ�õ�ǰ���û�Ĭ�����ã�ʹ��defaults���ԣ�������ϣ�
				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND ������� //
				// requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);
	}

	/** ��ʾ֪ͨ�� */
	public void showNotify() {
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// new Intent(this, MainActivity.class), 0);
		mBuilder.setContentTitle("���Ա���").setContentText("��������")
		// .setContentIntent(pendingIntent)
		// .setNumber(number)//��ʾ����
				.setTicker("����֪ͨ����");// ֪ͨ�״γ�����֪ͨ��������������Ч����
		mNotificationManager.notify(notifyId, mBuilder.build());
		// mNotification.notify(getResources().getString(R.string.app_name),
		// notiId, mBuilder.build());
	}

	/**
	 * ��ʾ֪ͨ�������ת��ָ��Activity
	 * 
	 * @param <T>
	 */
	public static <T> void showIntentActivityNotify(Context context,
			Class<T> cls) {
		// Notification.FLAG_ONGOING_EVENT --���ó�פ
		// Flag;Notification.FLAG_AUTO_CANCEL ֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// //��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
		mBuilder.setAutoCancel(true)// �������֪ͨ����ʧ
				.setContentTitle("���Ա���").setContentText("�����ת").setTicker("����");
		// �������ͼACTION����ת��Intent
		Intent resultIntent = new Intent(context, cls);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);
		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	/**
	 * ��ʼ��Ҫ�õ���ϵͳ����
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
