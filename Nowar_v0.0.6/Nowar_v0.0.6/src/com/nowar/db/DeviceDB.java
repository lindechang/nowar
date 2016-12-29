package com.nowar.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeviceDB {

	public final static String DEVICE_TABLE = "device_table";
	public final static String DEVICE_ID = "device_id";

	public final static String DEVICE_SELECT = "device_select";
	public final static String DEVICE_NAME = "device_name";
	public final static String DEVICE_AUTHOR = "device_author";
	public final static String DEVICE_SET = "device_set";

	private SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private final Context mCtx;

	public DeviceDB(Context mCtx) {
		this.mCtx = mCtx;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, CreateDB.DATABASE_NAME, null,
					CreateDB.DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + DEVICE_TABLE);
			onCreate(db);
		}
	}

	public DeviceDB Open() throws SQLException {
		mDBHelper = new DatabaseHelper(mCtx);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		if (mDBHelper != null) {
			mDBHelper.close();
		}
	}

	/**
	 * 表中添加新设备
	 * 
	 * @param name
	 * @param author
	 * @param select
	 * @param set
	 * @return
	 */
	public long InsertDevice(String name, String author, int select, int set) {
		long row = 0;
		ContentValues values = new ContentValues();
		values.put(DEVICE_NAME, name);
		values.put(DEVICE_AUTHOR, author);
		values.put(DEVICE_SELECT, select);
		values.put(DEVICE_SET, set);
		try {
			row = mDB.insert(DEVICE_TABLE, null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return row;
	}

	/**
	 * 删除表中所有字段数据
	 * 
	 * @return
	 */
	public boolean DeleteAllDevice() {
		int doneDelete = 0;
		try {
			doneDelete = mDB.delete(DEVICE_TABLE, null, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return doneDelete > 0;
	}

	/**
	 * 根据Id删除表中数据
	 * 
	 * @param id
	 * @return
	 */
	public boolean DeleteDeviceByID(int id) {
		int isDelete = 0;
		String[] stringID;
		stringID = new String[] { Integer.toString(id) };
		isDelete = mDB.delete(DEVICE_TABLE, DEVICE_ID + " = ?", stringID);
		return isDelete > 0;
	}

	/**
	 * 选择表中的字段
	 * 
	 * @return
	 */
	public Cursor SelectDevice() {
		Cursor cursor = mDB.query(DEVICE_TABLE, new String[] { DEVICE_ID,
				DEVICE_NAME, DEVICE_AUTHOR, DEVICE_SELECT, DEVICE_SET }, null,
				null, null, null, null);
		if (cursor.moveToFirst()) {
		}
		return cursor;
	}

	/**
	 * 根据ID修改设备数据
	 * 
	 * @param id
	 * @param name
	 * @param author
	 * @param select
	 * @param host
	 */
	public void UpdateDevice(int id, String name, String author, int select,
			int host) {
		String where = DEVICE_ID + " = ?";
		String[] stringID = { Integer.toString(id) };
		ContentValues values = new ContentValues();
		values.put(DEVICE_NAME, name);
		values.put(DEVICE_AUTHOR, author);
		values.put(DEVICE_SELECT, select);
		values.put(DEVICE_SET, host);
		mDB.update(DEVICE_TABLE, values, where, stringID);
	}

	/**
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public ArrayList<Device> fetchAll() {
		ArrayList<Device> allTicketsList = new ArrayList<Device>();
		Cursor mCursor = null;
		mCursor = mDB.query(DEVICE_TABLE, new String[] { DEVICE_ID,
				DEVICE_NAME, DEVICE_AUTHOR, DEVICE_SELECT, DEVICE_SET }, null,
				null, null, null, null);
		if (mCursor.moveToFirst()) {
			do {
				Device st = new Device();
				st.setDeviceSet(mCursor.getString(mCursor
						.getColumnIndexOrThrow(DEVICE_SET)));
				st.setDeviceNumber(mCursor.getString(mCursor
						.getColumnIndexOrThrow(DEVICE_AUTHOR)));
				st.setDeviceName(mCursor.getString(mCursor
						.getColumnIndexOrThrow(DEVICE_NAME)));
				allTicketsList.add(st);
			} while (mCursor.moveToNext());
		}
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		return allTicketsList;
	}
}
