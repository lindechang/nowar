package weilan.app.db;

import java.util.ArrayList;

import com.baidu.location.f;

import weilan.app.data.StaticVariable;
import weilan.app.db.DB_CONSTANT.TABLES.MSGEX;
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
public class MsgExTable {

	private SQLiteDatabase mDB; // ������ݿ�
	private DatabaseHelper mDBHelper;
	private final Context mCtx;

	public MsgExTable(Context mCtx) {
		this.mCtx = mCtx;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, CreateDB.DATABASE_NAME, null,
					CreateDB.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + MSGEX.MSGEX_TABLE);
			onCreate(db);
		}
	}

	public MsgExTable Open() throws SQLException {
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

	public long Insert(int type, int status, int isSend, String isShowTimer,
			String createTime, String talker, String content, String imgPath,
			String reserved) throws Exception {
		long row = 0;
		ContentValues values = new ContentValues();
		values.put(MSGEX.FIELDS.MSGEX_TYPE, type);
		values.put(MSGEX.FIELDS.MSGEX_STATUS, status);
		values.put(MSGEX.FIELDS.MSGEX_ISSEND, isSend);

		values.put(MSGEX.FIELDS.MSGEX_ISSHOWTIME, isShowTimer);
		values.put(MSGEX.FIELDS.MSGEX_CREATETIME, createTime);
		values.put(MSGEX.FIELDS.MSGEX_TALKER, talker);
		values.put(MSGEX.FIELDS.MSGEX_CONTENT, content);
		values.put(MSGEX.FIELDS.MSGEX_IMGPATH, imgPath);
		values.put(MSGEX.FIELDS.MSGEX_RESERVED, reserved);

		row = mDB.insert(MSGEX.MSGEX_TABLE, null, values);
		return row;
	}

	/**
	 * @param number
	 * @return ��ѯ�ֶ��Ƿ���ڱ���
	 */
	// public boolean checkDevice(String number) {
	// boolean flag = false;
	// Cursor cursor = mDB.rawQuery("select * from " + SWDEVICE_TABLE
	// + " where swdevice_number=?", new String[] { number });
	// // cursor.moveToFirst����ָ���ѯ����ĵ�һ��λ�á�
	// // һ��ͨ���ж�cursor.moveToFirst()��ֵΪtrue��false��ȷ����ѯ����Ƿ�Ϊ��
	// if (cursor != null && cursor.moveToFirst()) {
	// flag = true;
	// }
	// return flag;
	// }

	public boolean check(String sql, String number) {
		boolean flag = false;
		Cursor cursor = mDB.rawQuery(sql, new String[] { number });
		// cursor.moveToFirst����ָ���ѯ����ĵ�һ��λ�á�
		// һ��ͨ���ж�cursor.moveToFirst()��ֵΪtrue��false��ȷ����ѯ����Ƿ�Ϊ��
		if (cursor != null && cursor.moveToFirst()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * ɾ�����������ֶ�����
	 * 
	 * @return
	 */
	public boolean DeleteAllMsgEx() {
		int doneDelete = 0;
		try {
			doneDelete = mDB.delete(MSGEX.MSGEX_TABLE, null, null);
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
	public boolean DeleteMsgExByID(int id) {
		int isDelete = 0;
		String[] stringID;
		stringID = new String[] { Integer.toString(id) };
		isDelete = mDB.delete(MSGEX.MSGEX_TABLE,
				MSGEX.FIELDS.MSGEX_ID + " = ?", stringID);
		return isDelete > 0;
	}

	/**
	 * ѡ����е��ֶ�
	 * 
	 * @return
	 */
	public Cursor SelectSW() {
		Cursor cursor = mDB.query(MSGEX.MSGEX_TABLE, new String[] {
				MSGEX.FIELDS.MSGEX_ID, MSGEX.FIELDS.MSGEX_TYPE,
				MSGEX.FIELDS.MSGEX_STATUS, MSGEX.FIELDS.MSGEX_ISSEND,
				MSGEX.FIELDS.MSGEX_ISSHOWTIME, MSGEX.FIELDS.MSGEX_CREATETIME,
				MSGEX.FIELDS.MSGEX_TALKER, MSGEX.FIELDS.MSGEX_CONTENT,
				MSGEX.FIELDS.MSGEX_IMGPATH, MSGEX.FIELDS.MSGEX_RESERVED },
				null, null, null, null, null);
		if (cursor.moveToFirst()) {

		}
		return cursor;
	}

	/**
	 * ����ID�޸��豸����
	 * 
	 * @param id
	 * @param name
	 * @param number
	 * @param select
	 * @param host
	 */
	public void Update(int id,int type, int status, int isSend, String isShowTimer,
			String createTime, String talker, String content, String imgPath,
			String reserved)
			throws Exception {
		String where = MSGEX.FIELDS.MSGEX_ID + " = ?";
		String[] stringID = { Integer.toString(id) };
		ContentValues values = new ContentValues();
		values.put(MSGEX.FIELDS.MSGEX_TYPE, type);
		values.put(MSGEX.FIELDS.MSGEX_STATUS, status);
		values.put(MSGEX.FIELDS.MSGEX_ISSEND, isSend);

		values.put(MSGEX.FIELDS.MSGEX_ISSHOWTIME, isShowTimer);
		values.put(MSGEX.FIELDS.MSGEX_CREATETIME, createTime);
		values.put(MSGEX.FIELDS.MSGEX_TALKER, talker);
		values.put(MSGEX.FIELDS.MSGEX_CONTENT, content);
		values.put(MSGEX.FIELDS.MSGEX_IMGPATH, imgPath);
		values.put(MSGEX.FIELDS.MSGEX_RESERVED, reserved);
		mDB.update(MSGEX.MSGEX_TABLE, values, where, stringID);
	}

	/**
	 * @param number
	 *            �ж�����
	 * @param name
	 *            �޸��ֶ�
	 * @param content
	 *            �޸�����
	 * @throws Exception
	 */
	// public boolean up(String number, String updata_name, int updata_content)
	// throws Exception {
	// boolean flag = false;
	// String where = SWDEVICE_NUMBER + " = ?";
	// String[] string = { number };
	// ContentValues values = new ContentValues();
	// values.put(updata_name, updata_content);
	// int result = mDB.update(SWDEVICE_TABLE, values, where, string);
	// flag = result > 0 ? true : false;
	// return flag;
	// }

	/**
	 * @param number
	 *            �ж�����
	 * @param name
	 *            �޸��ֶ�
	 * @param content
	 *            �޸�����
	 * @throws Exception
	 */
	// public boolean updata(String number, String updata_name,
	// String updata_content) throws Exception {
	// boolean flag = false;
	// String where = SWDEVICE_NUMBER + " = ?";
	// String[] string = { number };
	// ContentValues values = new ContentValues();
	// values.put(updata_name, updata_content);
	// int result = mDB.update(SWDEVICE_TABLE, values, where, string);
	// flag = result > 0 ? true : false;
	// return flag;
	// }

	/**
	 * ѡ����е��ֶ�
	 * 
	 * @return
	 */
	// public SwDevice getSW(int position) {
	// SwDevice sw = new SwDevice();
	// Cursor cursor = mDB.query(MSGEX.MSGEX_TABLE, new String[] {
	// MSGEX.FIELDS.MSGEX_ID, MSGEX.FIELDS.MSGEX_TYPE,
	// MSGEX.FIELDS.MSGEX_STATUS, MSGEX.FIELDS.MSGEX_ISSEND,
	// MSGEX.FIELDS.MSGEX_ISSHOWTIME, MSGEX.FIELDS.MSGEX_CREATETIME,
	// MSGEX.FIELDS.MSGEX_TALKER, MSGEX.FIELDS.MSGEX_CONTENT,
	// MSGEX.FIELDS.MSGEX_IMGPATH, MSGEX.FIELDS.MSGEX_RESERVED },
	// null, null, null, null, null);
	// // if (cursor.moveToFirst()) {
	// // cursor =
	// // }
	// cursor.moveToPosition(position);
	// sw.setSwdeviceName(cursor.getString(1));
	// sw.setSwdeviceNumber(cursor.getString(2));
	// sw.setSwdeviceSelect(cursor.getInt(3));
	// sw.setSwdeviceSet(cursor.getInt(4));
	// return sw;
	// }

	/**
	 * ��ȡ���������ֶ�
	 * 
	 * @return
	 */
	public ArrayList<MsgEx> fetchAll() {
		ArrayList<MsgEx> allTicketsList = new ArrayList<MsgEx>();
		Cursor mCursor = null;
		mCursor = mDB.query(MSGEX.MSGEX_TABLE, new String[] {
				MSGEX.FIELDS.MSGEX_ID, MSGEX.FIELDS.MSGEX_TYPE,
				MSGEX.FIELDS.MSGEX_STATUS, MSGEX.FIELDS.MSGEX_ISSEND,
				MSGEX.FIELDS.MSGEX_ISSHOWTIME, MSGEX.FIELDS.MSGEX_CREATETIME,
				MSGEX.FIELDS.MSGEX_TALKER, MSGEX.FIELDS.MSGEX_CONTENT,
				MSGEX.FIELDS.MSGEX_IMGPATH, MSGEX.FIELDS.MSGEX_RESERVED },
				null, null, null, null, null);
		if (mCursor.moveToFirst()) {
			do {
				MsgEx msg = new MsgEx();
				msg.setType(mCursor.getInt(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_TYPE)));
				msg.setStatus(mCursor.getInt(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_STATUS)));
				msg.setIsSend(mCursor.getInt(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_ISSEND)));
				
				msg.setIsShowTimer(mCursor.getString(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_ISSHOWTIME)));
				msg.setCreateTime(mCursor.getString(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_CREATETIME)));
				msg.setTalker(mCursor.getString(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_TALKER)));
				msg.setContent(mCursor.getString(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_CONTENT)));
				msg.setImgPath(mCursor.getString(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_IMGPATH)));
				msg.setReserved(mCursor.getString(mCursor
						.getColumnIndexOrThrow(MSGEX.FIELDS.MSGEX_RESERVED)));
				
				allTicketsList.add(msg);
			} while (mCursor.moveToNext());
		}
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		return allTicketsList;
	}

}
