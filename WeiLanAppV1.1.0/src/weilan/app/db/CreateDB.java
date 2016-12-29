package weilan.app.db;


import weilan.app.data.StaticVariable;
import weilan.app.db.DB_CONSTANT.TABLES.MSGEX;
import weilan.app.tools.mina.ClientListener;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class CreateDB  {
//	public final static String DATABASE_NAME = ClientListener.sharedPreferences.getString("userName", "")+"_DB";
	public  static  String DATABASE_NAME;
	public final static int DATABASE_VERSION = 1;
	
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDB;
	private Context mCtx;
	
	// 创建数据库下的定位设备表的语句
	private final static String CREATE_TABLE_device = "CREATE TABLE IF NOT EXISTS "
				+ DeviceDB.DEVICE_TABLE
				+ "("
				+ DeviceDB.DEVICE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DeviceDB.DEVICE_NAME
				+ " TEXT,"
				+ DeviceDB.DEVICE_AUTHOR
				+ " TEXT,"
				+ DeviceDB.DEVICE_SELECT
				+ " INTEGER,"
				+ DeviceDB.DEVICE_SET
				+ " INTEGER)";
	
	// 创建数据库下的开关设备表的语句
		private final static String CREATE_TABLE_SWdevice = "CREATE TABLE IF NOT EXISTS "
					+ SwDB.SWDEVICE_TABLE
					+ "("
					+ SwDB.SWDEVICE_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ SwDB.SWDEVICE_NAME
					+ " TEXT,"
					+ SwDB.SWDEVICE_NUMBER
					+ " TEXT,"
					+ SwDB.SWDEVICE_SELECT
					+ " INTEGER,"
					+ SwDB.SWDEVICE_SET
					+ " INTEGER)";
	
	// 创建数据库下的通讯录表的语句
		private final static String CREATE_TABLE_book = "CREATE TABLE IF NOT EXISTS "
					+ FriendTable.FRIEND_TABLE
					+ "("
					+ FriendTable.FRIEND_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ FriendTable.FRIEND_NAME
					+ " TEXT,"
					+ FriendTable.FRIEND_NUMBER
					+ " TEXT)";
		
		// 创建数据库下的通讯录表的语句
				private final static String CREATE_TABLE_MSGEX = "CREATE TABLE IF NOT EXISTS "
							+ MSGEX.MSGEX_TABLE
							+ "("
							+ MSGEX.FIELDS.MSGEX_ID
							+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ MSGEX.FIELDS.MSGEX_TYPE
							+ " INTEGER,"
							+ MSGEX.FIELDS.MSGEX_STATUS
					        + " INTEGER,"
					        + MSGEX.FIELDS.MSGEX_ISSEND
					        + " INTEGER,"
							+ MSGEX.FIELDS.MSGEX_ISSHOWTIME
					        + " TEXT,"
					        + MSGEX.FIELDS.MSGEX_CREATETIME
					        + " TEXT,"
					        + MSGEX.FIELDS.MSGEX_TALKER
					        + " TEXT,"
					        + MSGEX.FIELDS.MSGEX_CONTENT
					        + " TEXT,"
					        + MSGEX.FIELDS.MSGEX_IMGPATH
					        + " TEXT,"
							+ MSGEX.FIELDS.MSGEX_RESERVED
							+ " TEXT)";
	
	
	
	
	public CreateDB(Context ctx){	
		mCtx = ctx;	
		DATABASE_NAME = SharedPrefsUtil.getValue(mCtx,"userName", "")+"_DB";
		mDBHelper = new DatabaseHelper(this.mCtx);	
		
	}

	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		

		public DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE_device);
			db.execSQL(CREATE_TABLE_SWdevice);
			db.execSQL(CREATE_TABLE_book);
			db.execSQL(CREATE_TABLE_MSGEX);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			// String sql = "DROP TABLE IF EXISTS " + DeviceDB.DEVICE_TABLE;
			// db.execSQL(sql);
			// onCreate(db);		
		}	
	}
	
	public CreateDB Open() throws SQLException {
		this.mDB = this.mDBHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		this.mDBHelper.close();
	}

	// 创建table
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(CREATE_TABLE_device);
//	}

//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		String sql = "DROP TABLE IF EXISTS " + DeviceDB.DEVICE_TABLE;
//		db.execSQL(sql);
//		onCreate(db);
//	}

//	public Cursor select() {
////		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = mDB
//				.query(DeviceDB.DEVICE_TABLE, null, null, null, null, null, null);
//		return cursor;
//	}

	// 增加操作
	public long insert(String bookname, String author, int select, int host) {

		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(DeviceDB.DEVICE_NAME, bookname);
		cv.put(DeviceDB.DEVICE_AUTHOR, author);
		cv.put(DeviceDB.DEVICE_SELECT, select);
		cv.put(DeviceDB.DEVICE_SET, host);
		long row = mDB.insert(DeviceDB.DEVICE_TABLE, null, cv);
		return row;
		// return 1;
	}

	// 删除操作
	public void delete(int id) {
		String where = DeviceDB.DEVICE_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		mDB.delete(DeviceDB.DEVICE_TABLE, where, whereValue);
	}

	// 修改操作
	public void update(int id, String bookname, String author, int select,
			int host) {
		String where = DeviceDB.DEVICE_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put(DeviceDB.DEVICE_NAME, bookname);
		cv.put(DeviceDB.DEVICE_AUTHOR, author);
		cv.put(DeviceDB.DEVICE_SELECT, select);
		cv.put(DeviceDB.DEVICE_SET, host);
		mDB.update(DeviceDB.DEVICE_TABLE, cv, where, whereValue);
	}
	// public Cursor rawQuery(String string, Object object) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(string, (String[]) object);
	// return cursor;
	// }
}
