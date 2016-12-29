package weilan.app.tools.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.zbar.lib.CaptureActivity;

import weilan.app.activity.MainActivity;
import weilan.app.data.StaticVariable;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.network.NetWork;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ClientListener extends Thread {
	/** 30�뷢��һ�������� */
	// private static final int HEARTBEAT_RATE = 40;
	/** 15���������ʱ�� */
	// private static final int RLINK_TIME = 15;
	//public static SharedPreferences sharedPreferences;
	// public static final String dstname = "www.mikimao.com";
	// public static final String dstname = "mikimao.vicp.cc";
	// public static final int PORT = 8889;
	// private NioSocketConnector connector;
	// private static ConnectFuture cf = null;
	// private NetWork netWork;
	private Context context;
	public ClientListener(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		// Create TCP/IP connection
		do {
			// if (MainActivity.TAG != null) {
			// if (!MainActivity.TAG.isFinishing()) {
			// System.out.println("MainActivity-----isFinishing:false");
			// } else {
			// System.out.println("MainActivity-----isFinishing:true");
			// }
			// } else {
			// System.out.println("MainActivity-----TAG = null");
			// }
			NioSocketConnector connector = new NioSocketConnector();
			//connector.getSessionConfig().setReadBufferSize(1024);
			//connector.getSessionConfig().setReadBufferSize(1024*1024);
			connector.getSessionConfig().setReadBufferSize(1024*1024);//���ͻ�����1M

			connector.getSessionConfig().setReceiveBufferSize(1024*1024);//���ջ�����1M
			// connector.setConnectTimeoutMillis(30000); // �������ӳ�ʱ

			// �����������ݵĹ�����
			// DefaultIoFilterChainBuilder chain = connector.getFilterChain();

			// �趨�����������һ��һ��(/r/n)�Ķ�ȡ����
			// chain.addLast("myChin", new ProtocolCodecFilter(new
			// TextLineCodecFactory()));

			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new MyProtocalCodecFactory(Charset
							.forName("UTF-8"))));
			KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl(
					context);
			// ����ע�͵������Զ���Handler��ʽ
			// KeepAliveRequestTimeoutHandler heartBeatHandler = new
			// KeepAliveRequestTimeoutHandlerImpl().DEAF_SPEAKER;
			// ��������_������ ������Ҫ�ȴ�������ʱ����
			KeepAliveRequestTimeoutHandler heartBeatHandler = KeepAliveRequestTimeoutHandler.DEAF_SPEAKER;
			KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
					IdleStatus.BOTH_IDLE, heartBeatHandler);
			// KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
			// IdleStatus.BOTH_IDLE);
			// �����Ƿ�forward����һ��filter
			// heartBeat.setForwardEvent(true);
			// ��������Ƶ��
			int HEARTBEAT_RATE = 40;
			heartBeat.setRequestInterval(HEARTBEAT_RATE);
			connector.getFilterChain().addLast("heartbeat", heartBeat);
			// ����������Ϣ��������һ��SamplMinaServerHander����
			connector.setHandler(new ClientHandler(context));
			// set connect timeout
			// connector.setConnectTimeout(30);
			// reconnector();
			// connector.setDefaultRemoteAddress(new InetSocketAddress(
			// dstname, PORT));// ����Ĭ�Ϸ��ʵ�ַ
			NetWork netWork = new NetWork();
			if (netWork.isNetworkConnected()) {
				//String dstname = "www.mikimao.com";
				String dstname = "mikimao.vicp.cc";
				int PORT = 8889;
				connector.setDefaultRemoteAddress(new InetSocketAddress(
						dstname, PORT));// ����Ĭ�Ϸ��ʵ�ַ
				for (;;) {
					try {
						// ���ӵ���������
						// ConnectFuture cf = connector.connect(new
						// InetSocketAddress(dstname,
						// PORT));
						ConnectFuture cf = connector.connect();
						System.out.println("��1");
						// Wait for the connection attempt to be finished.

						// cf.addListener(new ConnectListener()); //�첽

						cf.awaitUninterruptibly(); // ͬ�� �ȴ����Ӵ������
						if (cf.isConnected() == true) {
							IoSession session = cf.getSession();// ���session
							if (!session.isClosing() && session.isConnected()) {
								PacketService packet = new PacketService();
								String msg;
								boolean flag = SharedPrefsUtil.getValue(
										context, "isAutoLogin", false);
								if (flag) {
									if (((String) session
											.getAttribute("userName")) != null) {
										msg = FastJsonTools
												.createFastJsonString(packet
														.setPacket("heart",
																null, null,
																null, null));
									} else {
										String name = SharedPrefsUtil.getValue(
												context, "userName", "");
										String pswd = SharedPrefsUtil.getValue(
												context, "passWord", "");
										msg = FastJsonTools
												.createFastJsonString(packet
														.setPacket("login",
																name, pswd,
																null, null));
									}
								} else {
									msg = FastJsonTools
											.createFastJsonString(packet
													.setPacket("heart", null,
															null, null, null));
								}

								session.write(msg);
							}
						}
						cf.getSession().getCloseFuture().awaitUninterruptibly(); // �ȴ����ӶϿ�
						System.out.println("��3");
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("���Ӳ��ɹ�");
						break;
					}
				}

			}
			connector.dispose();// ����Ͽ����ͷ�
			System.out.println("�ͷ�connector");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (true);
	}

//	class ConnectListener implements IoFutureListener<ConnectFuture> {
//
//		public void operationComplete(ConnectFuture future) {
//
//			if (future.isConnected()) {
//				// get session
//				IoSession session = future.getSession();
//
//				if (!session.isClosing() && session.isConnected()) {
//					PacketService packet = new PacketService();
//					String msg;
//					if (sharedPreferences.getBoolean("isAutoLogin", false)) {
//						if (((String) session.getAttribute("userName")) != null) {
//							msg = FastJsonTools
//									.createFastJsonString(packet.setPacket(
//											"heart", null, null, null, null));
//						} else {
//							String name = sharedPreferences.getString(
//									"userName", "");
//							String pswd = sharedPreferences.getString(
//									"passWord", "");
//							msg = FastJsonTools
//									.createFastJsonString(packet.setPacket(
//											"login", name, pswd, null, null));
//						}
//					} else {
//						msg = FastJsonTools.createFastJsonString(packet
//								.setPacket("heart", null, null, null, null));
//					}
//					session.write(msg);
//				}
//
//			} else {
//				System.out.println("����ʧ��");
//				// logger.error("can not create the connection .");
//			}
//		}
//	}
	/**
	 * ��ʼ���Ӳ��ɹ�����
	 * 
	 * @param session
	 */
	// private void reconnector() {
	// for (;;) {
	// if (netWork.isNetworkConnected()) {
	// if (cf == null || !cf.isConnected()) {
	// try {
	// // ���ӵ���������
	// cf = connector.connect(new InetSocketAddress(dstname,
	// PORT));
	// // cf = connector.connect();
	// System.out.println("��1");
	// // Wait for the connection attempt to be finished.
	// cf.awaitUninterruptibly(); // �ȴ����Ӵ������
	// // if (!cf.isConnected()) {
	// // // cf.
	// // }
	// // if (cf.isCanceled())
	// System.out.println("��2");
	//
	// IoSession session = cf.getSession();// ���session
	//
	//
	// if (!session.isClosing() && session.isConnected()) {
	// PacketService packet = new PacketService();
	// String msg;
	// if (StaticVariable.sharedPreferences.getBoolean(
	// "isAutoLogin", false)) {
	// if (((String) session.getAttribute("userName")) != null) {
	// msg = FastJsonTools
	// .createFastJsonString(packet
	// .setPacket("heart", null,
	// null, null, null));
	// } else {
	// String name = StaticVariable.sharedPreferences
	// .getString("userName", "");
	// String pswd = StaticVariable.sharedPreferences
	// .getString("passWord", "");
	// msg = FastJsonTools
	// .createFastJsonString(packet
	// .setPacket("login", name,
	// pswd, null, null));
	// }
	// } else {
	// msg = FastJsonTools.createFastJsonString(packet
	// .setPacket("heart", null, null, null,
	// null));
	// }
	// session.write(msg);
	// }
	// cf.getSession().getCloseFuture().awaitUninterruptibly(); // �ȴ����ӶϿ�
	// System.out.println("��3");
	// } catch (Exception e) {
	// // TODO: handle exception
	// System.out.println("���Ӳ��ɹ�");
	// }
	// }
	// // connector.dispose();//���������ͷ�
	// }
	//
	// }
	// }

	/**
	 * ��������
	 * 
	 * @param session
	 */
	// private void relink(IoSession session) {
	// for (;;) {
	// try {
	// Thread.sleep(RLINK_TIME * 1000);
	// System.out.println("����....");
	// cf = connector.connect(new InetSocketAddress(dstname, PORT));
	// cf.awaitUninterruptibly();// �ȴ����Ӵ����ɹ�
	// session = cf.getSession();// ��ȡ�Ự
	// if (!session.isClosing() && session.isConnected()) {
	// PacketService packet = new PacketService();
	// String msg;
	// if (StaticVariable.sharedPreferences.getBoolean(
	// "isAutoLogin", false)) {
	// if (((String) session.getAttribute("userName")) != null) {
	// msg = FastJsonTools
	// .createFastJsonString(packet.setPacket(
	// "heart", null, null, null, null));
	// } else {
	// String name = StaticVariable.sharedPreferences
	// .getString("userName", "");
	// String pswd = StaticVariable.sharedPreferences
	// .getString("passWord", "");
	// msg = FastJsonTools
	// .createFastJsonString(packet.setPacket(
	// "login", name, pswd, null, null));
	// }
	//
	// } else {
	// msg = FastJsonTools.createFastJsonString(packet
	// .setPacket("heart", null, null, null, null));
	// }
	//
	// session.write(msg);
	// System.out.println("�����ɹ�");
	// break;
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	// }

	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// new testMain();
	// }

}
