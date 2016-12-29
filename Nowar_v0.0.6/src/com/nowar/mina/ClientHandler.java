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
	 * 当一个客户端连接进入时
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
		//System.out.println("链接异常");
		session.close(true);
	}

	/**
	 * 当客户端接受到消息时
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		packet = (MessagePacket) message;
		if (packet.getType().equals("heart")) {// 处理心跳回复
			//System.out.println("收到服务心跳：heart");
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
			// System.out.println("服务器发来的收到消息: " + packet.getContent());
			// 其他消息回复
			Intent intent = new Intent(NETWORK.ACTION);
			if (packet.getContent().equals("login_success")) { // 重连时重新设置setAttribute
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

		// 测试将消息回送给客户端
		// session.write(s);

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	/**
	 * 当一个客户端被关闭时
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		//System.out.println("client Disconnect");
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {

	}

}
