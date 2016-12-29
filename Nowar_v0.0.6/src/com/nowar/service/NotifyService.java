package com.nowar.service;

import com.nowar.service.StrongService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * 
 * @author 掌缘生灭
 *
 */
public class NotifyService extends Service {
	private String TAG = getClass().getName();
	// 用于判断主进程是否运�?
	private String Process_Name = "com.nowar.main";

	/**
	 * 启动ConnectorService
	 */
	private StrongService connectorService = new StrongService.Stub() {

		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), ConnectorService.class);
			getBaseContext().stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), ConnectorService.class);
			getBaseContext().startService(i);

		}
	};

	@Override
	public void onTrimMemory(int level) {
		// Toast.makeText(getBaseContext(), "NotifyService onTrimMemory..." +
		// level,
		// Toast.LENGTH_SHORT).show();
		keepService();
	}

	public void onCreate() {
		// Toast.makeText(NotifyService.this, "NotifyService onCreate...",
		// Toast.LENGTH_SHORT).show();
		keepService();
	}

	/**
	 * 判断ConnectorService是否还在运行，如果不是则启动ConnectorService
	 */
	private void keepService() {
		boolean isRun = ServiceUtils.isProessRunning(NotifyService.this,
				Process_Name);
		if (isRun == false) {
			try {
				// Toast.makeText(getBaseContext(), "重新启动 ConnectService",
				// Toast.LENGTH_SHORT).show();
				connectorService.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) connectorService;
	}

}
