package weilan.app.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendTable {

	public final static String FRIEND_TABLE = "friend_table";

	public final static String FRIEND_ID = "friend_id";
	public final static String FRIEND_NAME = "friend_name";
	public final static String FRIEND_NUMBER = "friend_number";

	private SQLiteDatabase bDB;
	private DatabaseHelper bDBHelper;
	private final Context bCtx;

	public FriendTable(Context Ctx) {
		this.bCtx = Ctx;
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

		}

	}

	public FriendTable Open() throws SQLException {
		bDBHelper = new DatabaseHelper(bCtx);
		bDB = bDBHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		if (bDBHelper != null) {
			bDBHelper.close();
		}
	}

	/**
	 * 表中添加新好友
	 * 
	 * @param name
	 * @param author
	 * @param select
	 * @param set
	 * @return
	 */
	public long Insert(String name, String number) {
		long row = 0;
		ContentValues values = new ContentValues();
		values.put(FRIEND_NAME, name);
		values.put(FRIEND_NUMBER, number);
		try {
			row = bDB.insert(FRIEND_TABLE, null, values);
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
	public boolean DeleteAllBook() {
		int doneDelete = 0;
		try {
			doneDelete = bDB.delete(FRIEND_TABLE, null, null);
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
	public boolean Delete(int id) {
		int isDelete = 0;
		String[] stringID;
		stringID = new String[] { Integer.toString(id) };
		isDelete = bDB.delete(FRIEND_TABLE, FRIEND_ID + " = ?", stringID);
		return isDelete > 0;
	}

	/**
	 * 选择表中的字段
	 * 
	 * @return
	 */
	public Cursor SelectBook() {
		Cursor cursor = bDB.query(FRIEND_TABLE, new String[] { FRIEND_ID,
				FRIEND_NAME, FRIEND_NUMBER }, null, null, null, null, null);
		if (cursor.moveToFirst()) {

		}
		return cursor;
	}

	public boolean check(String sql, String number) {
		boolean flag = false;
		Cursor cursor = bDB.rawQuery(sql, new String[] { number });
		// cursor.moveToFirst（）指向查询结果的第一个位置。
		// 一般通过判断cursor.moveToFirst()的值为true或false来确定查询结果是否为空
		if (cursor != null && cursor.moveToFirst()) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 选择表中的字段
	 * 
	 * @return
	 */
	public Friend getBook(int position) {
		Friend book = new Friend();
		Cursor cursor = bDB.query(FRIEND_TABLE, new String[] { FRIEND_ID,
				FRIEND_NAME, FRIEND_NUMBER }, null, null, null, null, null);
		cursor.moveToPosition(position);
		book.setFriendName(cursor.getString(1));
		book.setFriendNumber(cursor.getString(2));
		return book;
	}
	/**
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public ArrayList<Friend> fetchAll() {
		ArrayList<Friend> allBooksList = new ArrayList<Friend>();
		Cursor mCursor = null;
		mCursor = bDB.query(FRIEND_TABLE, new String[] { FRIEND_ID, FRIEND_NAME,
				FRIEND_NUMBER }, null, null, null, null, null);
		if (mCursor.moveToFirst()) {
			do {
				Friend book = new Friend();
				book.setFriendName(mCursor.getString(mCursor
						.getColumnIndexOrThrow(FRIEND_NAME)));
				book.setFriendNumber(mCursor.getString(mCursor
						.getColumnIndexOrThrow(FRIEND_NUMBER)));
				allBooksList.add(book);
			} while (mCursor.moveToNext());
		}
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		return allBooksList;
	}
	
	/**
	 * 根据ID修改好友信息
	 * 
	 * @param id
	 * @param name
	 * @param number
	 * @param select
	 * @param host
	 */
	public void Update(int id, String name, String number)
			throws Exception {
		String where = FRIEND_ID + " = ?";
		String[] stringID = { Integer.toString(id) };
		ContentValues values = new ContentValues();
		values.put(FRIEND_NAME, name);
		values.put(FRIEND_NUMBER, number);
		bDB.update(FRIEND_TABLE, values, where, stringID);
	}
	
	

}
