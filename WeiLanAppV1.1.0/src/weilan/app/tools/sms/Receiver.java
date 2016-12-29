package weilan.app.tools.sms;

import weilan.app.db.CreateDB;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class Receiver extends Service {
	SMSReceiver receiver;

	private CreateDB rTrackerDB;
	private Cursor rCursor;

	// SMSReceiver Host_set_receiver;

	public void run() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCreate() {

		// ��ȡ���ݿ�����
		rTrackerDB = new CreateDB(this);
		// rCursor = rTrackerDB.select();
      
		receiver = new SMSReceiver();
		// Host_set_receiver = new SMSReceiver();
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		this.registerReceiver(receiver, filter);
		// this.registerReceiver(Host_set_receiver, filter);
		super.onCreate();
	}

	public class SMSReceiver extends BroadcastReceiver {

		// GeoCoderDemo geocode = new GeoCoderDemo();

		private static final String strRes = "android.provider.Telephony.SMS_RECEIVED";

		public String latitude = null;
		public String longitude = null;
		public String LatAndLon = null;

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// ��һ������ȡ���ŵ����ݺͷ�����
			StringBuilder body = new StringBuilder();// ��������
			StringBuilder number = new StringBuilder();// ���ŷ�����
			// TODO Auto-generated method stub
			if (strRes.equals(arg1.getAction())) {
				StringBuilder sb = new StringBuilder(); // ����������� �� ������һ��洢
				Bundle bundle = arg1.getExtras();
				if (bundle != null) {
					Object[] pdus = (Object[]) bundle.get("pdus");
					SmsMessage[] msg = new SmsMessage[pdus.length];
					for (int i = 0; i < pdus.length; i++) {
						msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

					}

					for (SmsMessage curMsg : msg) {
						sb.append("�ֻ���:��");
						sb.append(curMsg.getDisplayOriginatingAddress());
						sb.append("�����ݣ�");
						sb.append(curMsg.getDisplayMessageBody());

						number.append(curMsg.getDisplayOriginatingAddress());
						body.append(curMsg.getDisplayMessageBody());
					}
					// Toast.makeText(arg0,
					// "����:" + sb.toString(),
					// Toast.LENGTH_SHORT).show();

					// latitude = msg[0].getDisplayMessageBody().substring(0,
					// 8);
					// longitude = msg[0].getDisplayMessageBody().substring(9,
					// 18);
					LatAndLon = msg[0].getDisplayMessageBody();
					// System.out.println("γ�ȣ�"+latitude);
					// System.out.println("���ȣ�"+longitude);
					System.out.println("�������ݣ�" + LatAndLon);
					// ���͹㲥
					Intent intent = new Intent();
					intent.putExtra("count", LatAndLon);
					intent.setAction("yundian.tracker.sms.Receiver");
					sendBroadcast(intent);

					// String smsBody = body.toString();
					String smsNumber = number.toString();
					for (int i = 0; i < rCursor.getCount(); i++) {
						rCursor.moveToPosition(i);
						// if (rCursor.getInt(3) == 1)
						String telNo = rCursor.getString(2);
						if (smsNumber.equals("+86" + telNo)) {
							this.abortBroadcast();
						}
					}

				}

			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
