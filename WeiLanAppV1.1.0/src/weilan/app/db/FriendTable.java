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
	 * ��������º���
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
	 * ɾ�����������ֶ�����
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
	 * ����Idɾ����������
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
	 * ѡ����е��ֶ�
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
		// cursor.moveToFirst����ָ���ѯ����ĵ�һ��λ�á�
		// һ��ͨ���ж�cursor.moveToFirst()��ֵΪtrue��false��ȷ����ѯ����Ƿ�Ϊ��
		if (cursor != null && cursor.moveToFirst()) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * ѡ����е��ֶ�
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
	 * ��ȡ���������ֶ�
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
	 * ����ID�޸ĺ�����Ϣ
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
