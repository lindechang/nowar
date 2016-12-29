package com.nowar.service;


import com.nowar.mina.ClientListener;
import com.nowar.service.StrongService;
import com.nowar.bluetooth.BtReceiveListener;
import com.nowar.broadcast.AllBroadcast;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;


public class ConnectorService extends Service {

	public static Context context;
	public static Intent intent = null;
	public static LocalBroadcastManager mLocalBroadcastManager;
	// 用于判断通知进程是否运行
	private String Process_Name = "com.nowar.main：notifyservice";
	
	/**
	 * 启动ConnectorService
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
		keepNotifyService();
	}
	
	/**
	 * 判断NotifyService是否还在运行，如果不是则启动NotifyService
	 */
	private void keepNotifyService() {
		boolean isRun = ServiceUtils.isProessRunning(ConnectorService.this,
				Process_Name);
		if (isRun == false) {
			try {
				// Toast.makeText(getBaseContext(), "重新启动 NotifyService",
				// Toast.LENGTH_SHORT).show();
				notifyService.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public ConnectorService() {
		// TODO Auto-generated constructor stub
		//this.context=  getApplicationContext();
		//this.context= context;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return (IBinder)notifyService;
	}

	public void onCreate() {
		super.onCreate();
	    mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
	    context = getApplicationContext();
	    new AllBroadcast(context);
	    new BtReceiveListener(context).start();
	    new ClientListener(context).start();
	    
	    
		//new BtClientListener(context).start();
	    
		keepNotifyService();
		//System.out.println("-wo shi serivce oncreate-->>");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//System.out.println("-wo shi serivce onStartCommand-->>");		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
