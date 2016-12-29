package com.nowar.main;


import com.nowar.db.CreateDB;
import com.nowar.service.ConnectorService;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class JddApplication extends Application{
	private Context context;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Intent intent = new Intent(this, ConnectorService.class);
		startService(intent);
		CreateDB mTrackerDB  = new CreateDB(this);
	    mTrackerDB.Open();
	}

}
