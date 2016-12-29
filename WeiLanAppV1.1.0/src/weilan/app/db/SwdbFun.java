package weilan.app.db;

import java.util.ArrayList;

import android.database.Cursor;

public class SwdbFun {

	public SwdbFun() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param DB
	 * @param name
	 * @param author
	 * @param select
	 * @param set
	 * @return
	 * @throws Exception
	 */
	public long InsertDevice(SwDB DB, String name, String author, int select,
			int set) {
		long row = 0;
		try {
			DB.Open();
			row = DB.Insert(name, author, select, set);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return row;
	}

	/**
	 * @param DB
	 * @param id
	 * @return
	 */
	public boolean DelSingleDevice(SwDB DB, int id) {
		boolean flag = false;

		try {
			DB.Open();
			Cursor mCursor = DB.SelectSW();
			mCursor.moveToPosition(id);
			flag = DB.DeleteDeviceByID(mCursor.getInt(0));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return flag;
	}

	/**
	 * ɾ�����������ֶ�����
	 * 
	 * @return
	 */
	public boolean DelAllDevice(SwDB DB) {
		boolean flag = false;
		try {
			DB.Open();
			flag = DB.DeleteAllDevice();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return flag;
	}

	/**
	 * @param DB
	 * @param id
	 * @param name
	 * @param author
	 * @param select
	 * @param host
	 */
	public void UpdateDevice(SwDB DB, int id, String name, String author,
			int select, int host) {

		try {
			DB.Open();
			DB.Update(id, name, author, select, host);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}

	}

	/**
	 * @param number
	 * @return
	 * @return ��ѯ�ֶ��Ƿ���� SwDB����
	 * 
	 */
	public boolean CheckSwDevice(SwDB DB, String number) {
		boolean flag = false;
		String sql = "select * from " + SwDB.SWDEVICE_TABLE
				+ " where swdevice_number=?";
		try {
			DB.Open();
			flag = DB.check(sql, number);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return flag;
	}

	/**
	 * @param DB
	 * @param number
	 * @param name
	 * @param content
	 * @return
	 */
	public boolean Update(SwDB DB, String number, String name, int content) {
		boolean flag = false;
		try {
			DB.Open();
			flag = DB.up(number, name, content);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return flag;
	}
	
	public boolean Update(SwDB DB, String number, String name, String content) {
		boolean flag = false;
		try {
			DB.Open();
			flag = DB.updata(number, name, content);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return flag;
	}

	/**
	 * ��ȡ���������ֶ�
	 * 
	 * @return
	 */
	public SwDevice getSingleSW(SwDB DB, int pos) {
		SwDevice sw = new SwDevice();
		try {
			DB.Open();
			sw = DB.getSW(pos);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return sw;
	}

	/**
	 * ��ȡ���������ֶ�
	 * 
	 * @return
	 */
	public ArrayList<SwDevice> FetchAll(SwDB DB) {
		ArrayList<SwDevice> allTicketsList = new ArrayList<SwDevice>();
		try {
			DB.Open();
			allTicketsList = DB.fetchAll();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return allTicketsList;
	}

}
