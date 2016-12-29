package weilan.app.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;







import weilan.app.db.DeviceDB;
import weilan.app.db.CreateDB;
import weilan.app.main.R;
import weilan.app.tools.http.NetTool;
import weilan.app.tools.sms.Receiver;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * �豸������Ϣ����Activity
 * @author lindec
 *
 */
public class SetInfoActivity extends Activity implements OnClickListener {
	
	private String bindUrl = "http://23.245.26.254/index.php/home/device/bind";
	// private SendSMS mSendSMS;
	private DeviceDB iDeviceDB;
	private Cursor iCursor;

	private String telephone_number = null;
	private String telephone_maseger = null;

	private Button infoBackBtn;// ���ذ���
	private Button infoHostSend = null; // �������ü�
	private Button infoLostSend = null; // �˳���������

	private EditText deviceName;
	private EditText deviceNumber;

	private MyReceiver Host_set_receiver = null;

	private String OLD_NAME = null;
	private String OLD_NUMBER = null;
	private int DEVICE_ID = 0;
	//private int DEVICE_SELECT = 0;

	private boolean host_btn = false;
	private boolean lost_btn = false;

	// ��һ��Activity���ݹ����Ĳ���
	private int position;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_view);
		InfoviewInits();
		
		// mSendSMS.validate(telephone_number, telephone_maseger);
		// validate
	}

	protected void InfoviewInits() {

		iDeviceDB = new DeviceDB(this);
		iDeviceDB.Open();
		iCursor = iDeviceDB.SelectDevice();
		// �����ͼ
		Intent intent = getIntent();
		// ��ȡ����
		Bundle bundle = intent.getExtras();
		position = bundle.getInt("pos");
		iCursor.moveToPosition(position);
		
		DEVICE_ID = iCursor.getInt(0);
		OLD_NAME = iCursor.getString(1);
		OLD_NUMBER = iCursor.getString(2);		
		//DEVICE_SELECT = iCursor.getInt(3);
		//DEVICE_HOST = iCursor.getInt(4);

		infoBackBtn = (Button) findViewById(R.id.back);
		infoBackBtn.setOnClickListener(this);

		deviceName = (EditText) findViewById(R.id.info_editText1_id);
		deviceNumber = (EditText) findViewById(R.id.info_editText2_id);
		
		deviceName.setText(iCursor.getString(1));
		deviceNumber.setText(iCursor.getString(2));
		deviceName.setSelection(iCursor.getString(1).length());//�ƶ���굽�ı�����

		infoHostSend = (Button) findViewById(R.id.info_hostBtn_id);
		infoLostSend = (Button) findViewById(R.id.info_lostBtn_id);
		if (iCursor.getInt(4) == 1) {
			infoHostSend.setEnabled(false);
			infoLostSend.setEnabled(true);
		} else {
			infoHostSend.setEnabled(true);
			infoLostSend.setEnabled(false);
		}
		infoHostSend.setOnClickListener(this);
		infoLostSend.setOnClickListener(this);
	}

	// ���ŷ��Ͳ�ѯĿ�����λ��
	String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

	/**
	 * Send SMS
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	private void sendSMS(String phoneNumber, String message) {

		if (!validate(phoneNumber, message)) {
			return;
		}
		// create the sentIntent parameter
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
				0);
		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
				deliverIntent, 0);

		SmsManager sms = SmsManager.getDefault();
		if (message.length() > 70) {
			List<String> msgs = sms.divideMessage(message);
			for (String msg : msgs) {
				sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
			}
		} else {
			sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
		}
		// Toast.makeText(MainActivity.this, R.string.message,
		// Toast.LENGTH_LONG).show();
		Toast.makeText(SetInfoActivity.this, "����ɹ���ȴ���", Toast.LENGTH_LONG)
				.show();

		// register the Broadcast Receivers
		// �������
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(getBaseContext(),
					// "SMS sent success actions",
					// Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(),
							"SMS generic failure actions", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(),
							"SMS radio off failure actions", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(),
							"SMS null PDU failure actions", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		}, new IntentFilter(SENT_SMS_ACTION));

		// �Է��������
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				// Toast.makeText(getBaseContext(),
				// "SMS delivered actions",
				// Toast.LENGTH_SHORT).show();
			}
		}, new IntentFilter(DELIVERED_SMS_ACTION));

	}
	
	
	public boolean validate(String telNo, String content) {

		if ((null == telNo) || ("".equals(telNo.trim()))) {
			Toast.makeText(this, "�������ֻ�����!", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!checkTelNo(telNo)) {
			Toast.makeText(this, "��������ȷ���ֻ���!", Toast.LENGTH_LONG).show();
			return false;
		}
		if ((null == content) || ("".equals(content.trim()))) {
			Toast.makeText(this, "�������ֻ�����!", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	/**
	 * �б��ֻ��Ƿ�Ϊ��ȷ�ֻ����룻 ����η������£�
	 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
	 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
	 */
	private static boolean checkTelNo(String telNo) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
		// .compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		// .compile("^((13[0-9])|(15[^4,//D])|(18[0-9]))\\d{8}$");
		// .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
		Matcher m = p.matcher(telNo);
		System.out.println("_________telNo" + telNo);
		System.out.println("_________m" + m);
		System.out.println("_________m.matches()" + m.matches());
		return m.matches();

	}

	/**
	 * �б��ֻ��Ƿ�Ϊ��ȷ�ֻ����룻 ����η������£�
	 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
	 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
	 */
	// public static boolean isMobileNum(String mobiles)
	// {
	// Pattern p = Pattern
	// .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
	// Matcher m = p.matcher(mobiles);
	// return m.matches(); }

	/**
	 * ��ȡ�㲥����
	 * 
	 * @author jiqinlin
	 * 
	 */
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String count = bundle.getString("count");
			// System.out.println("֣����ϲ����"+count);
			// System.out.println("bundle��"+bundle);
			if (host_btn) {
				if (count.equals("�������óɹ�")) {
					// System.out.println("֣����ϲ�����ˣ�"+bundle);
					infoHostSend.setEnabled(false);
					infoLostSend.setEnabled(true);
					// if(mCursor.getInt(3) == 1)
					// {
					// iTrackerDB.update(DEVICE_ID, OLD_NAME, OLD_NUMBER,
					// 0, 1);
					iDeviceDB.UpdateDevice(DEVICE_ID, OLD_NAME, OLD_NUMBER,
							0, 1);
					// }
				}
				host_btn = false;
			}
			if (lost_btn) {
				if (count.equals("�˳����˳ɹ�")) {
					// System.out.println("֣����ϲ�����ˣ�"+bundle);
					infoHostSend.setEnabled(true);
					infoLostSend.setEnabled(false);
					// iTrackerDB.update(DEVICE_ID, OLD_NAME, OLD_NUMBER,
					// 0, 0);
					iDeviceDB.UpdateDevice(DEVICE_ID, OLD_NAME, OLD_NUMBER,
							0, 0);
				}
				lost_btn = false;
			}

		}
	}

	@Override
	public void onClick(View v) {
		if (v.equals(infoBackBtn)) {
			 
			 
			 iCursor = iDeviceDB.SelectDevice();
			 deviceName = (EditText) findViewById(R.id.info_editText1_id);
			 deviceNumber = (EditText) findViewById(R.id.info_editText2_id);
			 iCursor.moveToPosition(position);
			
			 //int iTrackerDB_ID = iCursor.getInt(0);
			 int iHost = iCursor.getInt(4);
			 String sdeviceName = deviceName.getText().toString().trim();
			 String sdeviceNumber = deviceNumber.getText().toString().trim();
			
			// iTrackerDB.update(DEVICE_ID, sdeviceName, sdeviceNumber, 0,
			// iHost);
			 iDeviceDB.UpdateDevice(DEVICE_ID, sdeviceName, sdeviceNumber, 0,
					 iHost);
			 iDeviceDB.Close();
			 finish();
			
			// String retStr = "";
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("token", "100f298611b43cf6aea51676fcecd34b");
			// try {
			// retStr = NetTool.sendPostRequest(bindUrl, map, "utf-8");
			// System.out.println("retStr-----:" + retStr);
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		if (v.equals(infoHostSend)) {
			// telephone_number = "18357151580";
			telephone_number = OLD_NUMBER;
			telephone_maseger = "����������123456";
			if (telephone_number != null) {
				// mSendSMS.SendSMS(telephone_number,telephone_maseger);
				sendSMS(telephone_number, telephone_maseger);
				Toast.makeText(SetInfoActivity.this, "����ɹ���ȴ�!",
						Toast.LENGTH_LONG).show();

				// ��������
				startService(new Intent(SetInfoActivity.this, Receiver.class));
				// ע��㲥������
				Host_set_receiver = new MyReceiver();
				IntentFilter filter = new IntentFilter();
				// filter.addAction("baidumapsdk.demo.ReceiverDemo");
				filter.addAction("yundian.tracker.sms.Receiver");
				SetInfoActivity.this
						.registerReceiver(Host_set_receiver, filter);
				host_btn = true;
			} else {
				Toast.makeText(SetInfoActivity.this, "��������ȷ��Ϣ!",
						Toast.LENGTH_LONG).show();
			}
		}
		if (v.equals(infoLostSend)) {
			// telephone_number = "18357151580";
			// telephone_maseger = "֣������Ů���Ѱ�";
			telephone_number = OLD_NUMBER;
			telephone_maseger = "�˳�����";
			if (telephone_number != null) {
				// mSendSMS.SendSMS(telephone_number,telephone_maseger);
				sendSMS(telephone_number, telephone_maseger);
				Toast.makeText(SetInfoActivity.this, "����ɹ���ȴ�!",
						Toast.LENGTH_LONG).show();

				// ��������
				startService(new Intent(SetInfoActivity.this, Receiver.class));
				// ע��㲥������
				Host_set_receiver = new MyReceiver();
				IntentFilter filter = new IntentFilter();
				// filter.addAction("baidumapsdk.demo.ReceiverDemo");
				filter.addAction("yundian.tracker.sms.Receiver");
				SetInfoActivity.this
						.registerReceiver(Host_set_receiver, filter);
				lost_btn = true;
			} else {
				Toast.makeText(SetInfoActivity.this, "��������ȷ��Ϣ!",
						Toast.LENGTH_LONG).show();
			}
		}
	}
    /**
     * 
     */
	void onDestory() {
		super.onDestroy();
		iCursor.close();
		iDeviceDB.Close();
	}

}
