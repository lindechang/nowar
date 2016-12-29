package com.nowar.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.nowar.broadcast.BROADCAST_CONSTANT.NETWORK;
import com.nowar.broadcast.NetworkBroadcast;
import com.nowar.db.FriendTableHandle;
import com.nowar.db.MsgExTableHandle;
import com.nowar.packet.MessagePacket;
import com.nowar.sharedprefs.SharedPrefsUtil;

import android.content.Context;
import android.content.Intent;

public class ClientHandler extends IoHandlerAdapter {

	// public static Map<String, IoSession> sessionMap = new HashMap<String,
	// IoSession>();

	public static IoSession session;
	private Context context;

	// private FriendTable mFriendDB;
	// private Cursor mCursor;
	private FriendTableHandle mFriendFun;
	private MessagePacket packet;
	private MsgExTableHandle msgExHandle;

	public ClientHandler(Context context) {
		this.context = context;
		// mFriendDB = new FriendTable(context);
		mFriendFun = new FriendTableHandle();
		msgExHandle = new MsgExTableHandle(context);
	}

	// @Override
	// public void sessionCreated(IoSession session) throws Exception {
	// // TODO Auto-generated method stub
	//
	// }
	/**
	 * ��һ���ͻ������ӽ���ʱ
	 * 
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {

		//System.out.println("incomming client:" + session.getRemoteAddress());
		this.session = session;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		//System.out.println("�����쳣");
		session.close(true);
	}

	/**
	 * ���ͻ��˽��ܵ���Ϣʱ
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		packet = (MessagePacket) message;
		if (packet.getType().equals("heart")) {// ���������ظ�
			//System.out.println("�յ�����������heart");
		} else if (packet.getType().equals("notification")) {
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// mFriendFun.InsertFriends(mFriendDB, packet.getUsername(),
			// packet.getGoal());
			// }
			// }).start();
			// MyNotification.showIntentActivityNotify(context,MainActivity.class);
		} else {
			// System.out.println("�������������յ���Ϣ: " + packet.getContent());
			// ������Ϣ�ظ�
			Intent intent = new Intent(NETWORK.ACTION);
			if (packet.getContent().equals("login_success")) { // ����ʱ��������setAttribute
				if (SharedPrefsUtil.getSharedPrefsUtil().getValue(context,
						"isAutoLogin", false)) {
					session.setAttribute(
							"userName",
							SharedPrefsUtil.getSharedPrefsUtil().getValue(
									context, "userName", ""));
				}
			}
			intent.putExtra("message", packet.getContent());
			NetworkBroadcast.sendLocalBroadcast(intent);

		}

		// ���Խ���Ϣ���͸��ͻ���
		// session.write(s);

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	/**
	 * ��һ���ͻ��˱��ر�ʱ
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		//System.out.println("client Disconnect");
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {

	}

}
