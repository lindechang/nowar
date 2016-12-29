package weilan.app.db;

import java.util.ArrayList;

import android.content.Context;

public class MsgExTableHandle {
	
	MsgExTable msgExDao;
	public MsgExTableHandle(Context context) {
		this.msgExDao = new MsgExTable(context);
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
	public long InsertMsg(int type, int status, int isSend, String isShowTimer,
			String createTime, String talker, String content, String imgPath,
			String reserved) {
		long row = 0;
		try {
			msgExDao.Open();
			row = msgExDao.Insert(type, status, isSend, isShowTimer, createTime,
					talker, content, imgPath, reserved);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			msgExDao.Close();
		}
		return row;
	}


	/**
	 * 删除表中所有字段数据
	 * 
	 * @return
	 */
	public boolean DelAllMsgEx() {
		boolean flag = false;
		try {
			msgExDao.Open();
			flag = msgExDao.DeleteAllMsgEx();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			msgExDao.Close();
		}
		return flag;
	}


	/**
	 * 获取表中所有字段
	 * 
	 * @return
	 */
	public ArrayList<MsgEx> FetchAll() {
		ArrayList<MsgEx> allTicketsList = new ArrayList<MsgEx>();
		try {
			msgExDao.Open();
			allTicketsList = msgExDao.fetchAll();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			msgExDao.Close();
		}
		return allTicketsList;
	}

}
