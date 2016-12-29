package com.nowar.db;

import java.util.ArrayList;

import android.database.Cursor;

public class BtdbFun {

	public BtdbFun() {
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
	public long InsertDevice(BtDB DB, String name, String author, int select,
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
	public boolean DelSingleDevice(BtDB DB, int id) {
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
	 * 删除表中所有字段数据
	 * 
	 * @return
	 */
	public boolean DelAllDevice(BtDB DB) {
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
	public void UpdateDevice(BtDB DB, int id, String name, String author,
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
	 * @return 查询字段是否存在 SwDB表中
	 * 
	 */
	public boolean CheckSwDevice(BtDB DB, String number) {
		boolean flag = false;
		String sql = "select * from " + BtDB.SWDEVICE_TABLE
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
	public boolean Update(BtDB DB, String number, String name, int content) {
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
	
	public boolean Update(BtDB DB, String number, String name, String content) {
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
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public BtDevice getSingleSW(BtDB DB, int pos) {
		BtDevice sw = new BtDevice();
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
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public ArrayList<BtDevice> FetchAll(BtDB DB) {
		ArrayList<BtDevice> allTicketsList = new ArrayList<BtDevice>();
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
