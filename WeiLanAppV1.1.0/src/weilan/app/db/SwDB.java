package weilan.app.db;

import java.util.ArrayList;

import com.baidu.location.f;

import weilan.app.data.StaticVariable;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author lindec
 *
 */
public class SwDB {
	// public final String username = ;
	public final static String SWDEVICE_TABLE = "swdevice_table";
	public final static String SWDEVICE_ID = "swdevice_id";

	public final static String SWDEVICE_SELECT = "swdevice_select";
	public final static String SWDEVICE_NAME = "swdevice_name";
	public final static String SWDEVICE_NUMBER = "swdevice_number";
	public final static String SWDEVICE_SET = "swdevice_set";

	private SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private final Context mCtx;

	public SwDB(Context mCtx) {
		this.mCtx = mCtx;
		// sharedPreferences = this
		// .getSharedPreferences("loginInfo", MODE_PRIVATE);
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
			db.execSQL("DROP TABLE IF EXISTS" + SWDEVICE_TABLE);
			onCreate(db);
		}
	}

	public SwDB Open() throws SQLException {
		mDBHelper = new DatabaseHelper(mCtx);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		if (mDBHelper != null) {
			mDBHelper.close();
		}
		if (mDB != null) {
			mDB.close();
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
	public long Insert(String name, String author, int select, int set)
			throws Exception {
		long row = 0;
		ContentValues values = new ContentValues();
		values.put(SWDEVICE_NAME, name);
		values.put(SWDEVICE_NUMBER, author);
		values.put(SWDEVICE_SELECT, select);
		values.put(SWDEVICE_SET, set);
		row = mDB.insert(SWDEVICE_TABLE, null, values);
		return row;
	}

	/**
	 * @param number
	 * @return 查询字段是否存在表中
	 */
	// public boolean checkDevice(String number) {
	// boolean flag = false;
	// Cursor cursor = mDB.rawQuery("select * from " + SWDEVICE_TABLE
	// + " where swdevice_number=?", new String[] { number });
	// // cursor.moveToFirst（）指向查询结果的第一个位置。
	// // 一般通过判断cursor.moveToFirst()的值为true或false来确定查询结果是否为空
	// if (cursor != null && cursor.moveToFirst()) {
	// flag = true;
	// }
	// return flag;
	// }

	public boolean check(String sql, String number) {
		boolean flag = false;
		Cursor cursor = mDB.rawQuery(sql, new String[] { number });
		// cursor.moveToFirst（）指向查询结果的第一个位置。
		// 一般通过判断cursor.moveToFirst()的值为true或false来确定查询结果是否为空
		if (cursor != null && cursor.moveToFirst()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除表中所有字段数据
	 * 
	 * @return
	 */
	public boolean DeleteAllDevice() {
		int doneDelete = 0;
		try {
			doneDelete = mDB.delete(SWDEVICE_TABLE, null, null);
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
		isDelete = mDB.delete(SWDEVICE_TABLE, SWDEVICE_ID + " = ?", stringID);
		return isDelete > 0;
	}

	/**
	 * 选择表中的字段
	 * 
	 * @return
	 */
	public Cursor SelectSW() {
		Cursor cursor = mDB.query(SWDEVICE_TABLE,
				new String[] { SWDEVICE_ID, SWDEVICE_NAME, SWDEVICE_NUMBER,
						SWDEVICE_SELECT, SWDEVICE_SET }, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {

		}
		return cursor;
	}

	/**
	 * 根据ID修改设备数据
	 * 
	 * @param id
	 * @param name
	 * @param number
	 * @param select
	 * @param host
	 */
	public void Update(int id, String name, String number, int select, int host)
			throws Exception {
		String where = SWDEVICE_ID + " = ?";
		String[] stringID = { Integer.toString(id) };
		ContentValues values = new ContentValues();
		values.put(SWDEVICE_NAME, name);
		values.put(SWDEVICE_NUMBER, number);
		values.put(SWDEVICE_SELECT, select);
		values.put(SWDEVICE_SET, host);
		mDB.update(SWDEVICE_TABLE, values, where, stringID);
	}

	/**
	 * @param number
	 *            判断条件
	 * @param name
	 *            修改字段
	 * @param content
	 *            修改内容
	 * @throws Exception
	 */
	public boolean up(String number, String updata_name, int updata_content) throws Exception {
		boolean flag = false;
		String where = SWDEVICE_NUMBER + " = ?";
		String[] string = { number };
		ContentValues values = new ContentValues();
		values.put(updata_name, updata_content);
		int result = mDB.update(SWDEVICE_TABLE, values, where, string);
		flag = result > 0 ? true : false;
		return flag;
	}
	
	/**
	 * @param number
	 *            判断条件
	 * @param name
	 *            修改字段
	 * @param content
	 *            修改内容
	 * @throws Exception
	 */
	public boolean updata(String number, String updata_name, String updata_content) throws Exception {
		boolean flag = false;
		String where = SWDEVICE_NUMBER + " = ?";
		String[] string = { number };
		ContentValues values = new ContentValues();
		values.put(updata_name, updata_content);
		int result = mDB.update(SWDEVICE_TABLE, values, where, string);
		flag = result > 0 ? true : false;
		return flag;
	}
	
	

	/**
	 * 选择表中的字段
	 * 
	 * @return
	 */
	public SwDevice getSW(int position) {
		SwDevice sw = new SwDevice();
		Cursor cursor = mDB.query(SWDEVICE_TABLE,
				new String[] { SWDEVICE_ID, SWDEVICE_NAME, SWDEVICE_NUMBER,
						SWDEVICE_SELECT, SWDEVICE_SET }, null, null, null,
				null, null);
		// if (cursor.moveToFirst()) {
		// cursor =
		// }
		cursor.moveToPosition(position);
		sw.setSwdeviceName(cursor.getString(1));
		sw.setSwdeviceNumber(cursor.getString(2));
		sw.setSwdeviceSelect(cursor.getInt(3));
		sw.setSwdeviceSet(cursor.getInt(4));
		return sw;
	}

	/**
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public ArrayList<SwDevice> fetchAll() {
		ArrayList<SwDevice> allTicketsList = new ArrayList<SwDevice>();
		Cursor mCursor = null;
		mCursor = mDB.query(SWDEVICE_TABLE,
				new String[] { SWDEVICE_ID, SWDEVICE_NAME, SWDEVICE_NUMBER,
						SWDEVICE_SELECT, SWDEVICE_SET }, null, null, null,
				null, null);
		if (mCursor.moveToFirst()) {
			do {
				SwDevice st = new SwDevice();
				st.setSwdeviceName(mCursor.getString(mCursor
						.getColumnIndexOrThrow(SWDEVICE_NAME)));
				st.setSwdeviceNumber(mCursor.getString(mCursor
						.getColumnIndexOrThrow(SWDEVICE_NUMBER)));
				st.setSwdeviceSelect(mCursor.getInt(mCursor
						.getColumnIndexOrThrow(SWDEVICE_SELECT)));
				st.setSwdeviceSet(mCursor.getInt(mCursor
						.getColumnIndexOrThrow(SWDEVICE_SET)));
				allTicketsList.add(st);
			} while (mCursor.moveToNext());
		}
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		return allTicketsList;
	}

}
