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
	/** 30秒发送一次心跳包 */
	// private static final int HEARTBEAT_RATE = 40;
	/** 15秒重连间隔时间 */
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
			connector.getSessionConfig().setReadBufferSize(1024*1024);//发送缓冲区1M

			connector.getSessionConfig().setReceiveBufferSize(1024*1024);//接收缓冲区1M
			// connector.setConnectTimeoutMillis(30000); // 设置连接超时

			// 创建接受数据的过滤器
			// DefaultIoFilterChainBuilder chain = connector.getFilterChain();

			// 设定这个过滤器将一行一行(/r/n)的读取数据
			// chain.addLast("myChin", new ProtocolCodecFilter(new
			// TextLineCodecFactory()));

			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new MyProtocalCodecFactory(Charset
							.forName("UTF-8"))));
			KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl(
					context);
			// 下面注释掉的是自定义Handler方式
			// KeepAliveRequestTimeoutHandler heartBeatHandler = new
			// KeepAliveRequestTimeoutHandlerImpl().DEAF_SPEAKER;
			// 心跳机制_聋子型 ：不需要等待反馈超时处理
			KeepAliveRequestTimeoutHandler heartBeatHandler = KeepAliveRequestTimeoutHandler.DEAF_SPEAKER;
			KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
					IdleStatus.BOTH_IDLE, heartBeatHandler);
			// KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
			// IdleStatus.BOTH_IDLE);
			// 设置是否forward到下一个filter
			// heartBeat.setForwardEvent(true);
			// 设置心跳频率
			int HEARTBEAT_RATE = 40;
			heartBeat.setRequestInterval(HEARTBEAT_RATE);
			connector.getFilterChain().addLast("heartbeat", heartBeat);
			// 服务器的消息处理器：一个SamplMinaServerHander对象
			connector.setHandler(new ClientHandler(context));
			// set connect timeout
			// connector.setConnectTimeout(30);
			// reconnector();
			// connector.setDefaultRemoteAddress(new InetSocketAddress(
			// dstname, PORT));// 设置默认访问地址
			NetWork netWork = new NetWork();
			if (netWork.isNetworkConnected()) {
				//String dstname = "www.mikimao.com";
				String dstname = "mikimao.vicp.cc";
				int PORT = 8889;
				connector.setDefaultRemoteAddress(new InetSocketAddress(
						dstname, PORT));// 设置默认访问地址
				for (;;) {
					try {
						// 连接到服务器：
						// ConnectFuture cf = connector.connect(new
						// InetSocketAddress(dstname,
						// PORT));
						ConnectFuture cf = connector.connect();
						System.out.println("这1");
						// Wait for the connection attempt to be finished.

						// cf.addListener(new ConnectListener()); //异步

						cf.awaitUninterruptibly(); // 同步 等待连接创建完成
						if (cf.isConnected() == true) {
							IoSession session = cf.getSession();// 获得session
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
						cf.getSession().getCloseFuture().awaitUninterruptibly(); // 等待连接断开
						System.out.println("这3");
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("链接不成功");
						break;
					}
				}

			}
			connector.dispose();// 网络断开，释放
			System.out.println("释放connector");
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
//				System.out.println("链接失败");
//				// logger.error("can not create the connection .");
//			}
//		}
//	}
	/**
	 * 初始链接不成功重连
	 * 
	 * @param session
	 */
	// private void reconnector() {
	// for (;;) {
	// if (netWork.isNetworkConnected()) {
	// if (cf == null || !cf.isConnected()) {
	// try {
	// // 连接到服务器：
	// cf = connector.connect(new InetSocketAddress(dstname,
	// PORT));
	// // cf = connector.connect();
	// System.out.println("这1");
	// // Wait for the connection attempt to be finished.
	// cf.awaitUninterruptibly(); // 等待连接创建完成
	// // if (!cf.isConnected()) {
	// // // cf.
	// // }
	// // if (cf.isCanceled())
	// System.out.println("这2");
	//
	// IoSession session = cf.getSession();// 获得session
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
	// cf.getSession().getCloseFuture().awaitUninterruptibly(); // 等待连接断开
	// System.out.println("这3");
	// } catch (Exception e) {
	// // TODO: handle exception
	// System.out.println("链接不成功");
	// }
	// }
	// // connector.dispose();//重链不能释放
	// }
	//
	// }
	// }

	/**
	 * 断线重连
	 * 
	 * @param session
	 */
	// private void relink(IoSession session) {
	// for (;;) {
	// try {
	// Thread.sleep(RLINK_TIME * 1000);
	// System.out.println("重链....");
	// cf = connector.connect(new InetSocketAddress(dstname, PORT));
	// cf.awaitUninterruptibly();// 等待连接创建成功
	// session = cf.getSession();// 获取会话
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
	// System.out.println("重链成功");
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
