package weilan.app.tools.mina;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import weilan.app.activity.ChatActivity;
import weilan.app.activity.MainActivity;
import weilan.app.broadcast.BCH_CONSTANT;
import weilan.app.broadcast.MyBroadcast;
import weilan.app.data.StaticVariable;
import weilan.app.db.FriendTable;
import weilan.app.db.FriendTableHandle;
import weilan.app.db.MsgExTableHandle;
import weilan.app.notification.MyNotification;
import weilan.app.service.ConnectorService;
import weilan.app.tools.fastjosn.FastJsonService;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.MessagePacket;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

public class ClientHandler extends IoHandlerAdapter {

	// public static Map<String, IoSession> sessionMap = new HashMap<String,
	// IoSession>();

	public static IoSession session;
	private Context context;

	private FriendTable mFriendDB;
	// private Cursor mCursor;
	private FriendTableHandle mFriendFun;
	private MessagePacket packet ;
	private MsgExTableHandle msgExHandle;
	
	private MyBroadcast myBroadcast;

	public ClientHandler(Context context) {
		this.context = context;
		mFriendDB = new FriendTable(context);
		mFriendFun = new FriendTableHandle();
		msgExHandle = new MsgExTableHandle(context);
		myBroadcast = new MyBroadcast(context);
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

		System.out.println("incomming client:" + session.getRemoteAddress());
		this.session = session;
		// System.out.println("this.session" + this.session);
		// session.write("{我来啦");

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		// WindowShow.println("服务器出现异常：" + cause);
		System.out.println("链接异常");
		session.close(true);
	}

	/**
	 * 当客户端接受到消息时
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// try {
		// byte[] data = (byte[])message;
		// String content = new String(data,"UTF-8");
		// packet = FastJsonService.getPerson(
		// (String) content, MessagePacket.class);
		// } catch (Exception e) {
		// System.out.println("是啦");
		// }
		packet = (MessagePacket)message;
		// 我们已设定了服务器的消息规则是一行一行读取，这里就可以转为String:
	  //packet = (MessagePacket) message;

		// Context context = this;
		// Writer the received data back to remote peer

		if (packet.getType().equals("heart")) {// 处理心跳回复
			System.out.println("收到服务心跳：heart");
			// Intent intent = new Intent(ConnectorService.HEART_BEAT_ACTION);
			// ConnectorService.mLocalBroadcastManager.sendBroadcast(intent);
		} else if (packet.getType().equals("notification")) {
			new Thread(new Runnable() {				
				@Override
				public void run() {
					// TODO Auto-generated method stub
				mFriendFun.InsertFriends(mFriendDB, packet.getUsername(),
							packet.getGoal());
				}
			}).start();			
			MyNotification.showIntentActivityNotify(context,MainActivity.class);
		} else if(packet.getType().equals("chat")){
			new Thread(new Runnable() {				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int m1 = 1;
					int m2 = 0;
					msgExHandle.InsertMsg(m1, m1, m2, "123",
							"2015-10-13-14:53", packet.getGoal(), packet.getContent(),
							"path", "nothing");
					Intent intent = new Intent(BCH_CONSTANT.TYPE.CHAT);
					intent.putExtra("message", packet.getContent());
					myBroadcast.sendBroadcast(intent);
				
				}
			}).start();	
			MyNotification.showIntentActivityNotify(context,ChatActivity.class);
		}
		else {
			// System.out.println("服务器发来的收到消息: " + packet.getContent());
			// 其他消息回复
			Intent intent = new Intent(BCH_CONSTANT.ACTION);
			if (packet.getContent().equals("login_success")) { // 重连时重新设置setAttribute
				if (SharedPrefsUtil.getValue(context, "isAutoLogin", false)) {
					session.setAttribute("userName",
							SharedPrefsUtil.getValue(context, "userName", ""));
				}
			}
			intent.putExtra("message", packet.getContent());
			myBroadcast.sendBroadcast(intent);
		}

		// 测试将消息回送给客户端
		// session.write(s);

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("");
	}

	/**
	 * 当一个客户端被关闭时
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("client Disconnect");
		// this.session = null;
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub

	}

}
