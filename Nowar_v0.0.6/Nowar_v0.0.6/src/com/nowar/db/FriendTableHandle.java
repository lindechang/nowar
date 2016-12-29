package com.nowar.db;

import java.util.ArrayList;

import android.database.Cursor;

public class FriendTableHandle {

	public FriendTableHandle() {
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
	public long InsertFriends(FriendTable DB, String name, String number) {
		long row = 0;
		try {
			DB.Open();
			row = DB.Insert(name, number);
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
	public boolean DelSingleFriend(FriendTable DB, int id) {
		boolean flag = false;

		try {
			DB.Open();
			Cursor mCursor = DB.SelectBook();
			mCursor.moveToPosition(id);
			flag = DB.Delete(mCursor.getInt(0));
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
	public boolean DelAllFriends(FriendTable DB) {
		boolean flag = false;
		try {
			DB.Open();
			flag = DB.DeleteAllBook();
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
	public void UpdateFriend(FriendTable DB, int id, String name, String number,
			int select, int host) {

		try {
			DB.Open();
			DB.Update(id, name, number);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}

	}

	/**
	 * @param number
	 * @return
	 * @return 查询字段是否存在 BookDB表中
	 * 
	 */
	public boolean CheckFriend(FriendTable DB, String number) {
		boolean flag = false;
		String sql = "select * from " + FriendTable.FRIEND_TABLE
				+ " where friend_number=?";
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
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public Friend getSingleFriend(FriendTable DB, int pos) {
		Friend friend = new Friend();
		try {
			DB.Open();
			friend = DB.getBook(pos);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			DB.Close();
		}
		return friend;
	}

	/**
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public ArrayList<Friend> FetchAll(FriendTable DB) {
		ArrayList<Friend> allTicketsList = new ArrayList<Friend>();
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
