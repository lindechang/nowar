package com.nowar.mina;

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

import com.lindec.app.tools.NetworkUtil;
import com.nowar.fastjson.FastJsonTools;
import com.nowar.packet.PacketService;
import com.nowar.sharedprefs.SharedPrefsUtil;

import android.content.Context;

public class ClientListener extends Thread {

	private Context context;

	public ClientListener(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		do {
			NioSocketConnector connector = new NioSocketConnector();
			connector.getSessionConfig().setReadBufferSize(1024 * 1024);// 发送缓冲区1M
			connector.getSessionConfig().setReceiveBufferSize(1024 * 1024);// 接收缓冲区1M
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
			// connector.setDefaultRemoteAddress(new InetSocketAddress(
			// dstname, PORT));// 设置默认访问地址
			NetworkUtil netWork = new NetworkUtil();
			if (netWork.isNetworkConnected(context)) {
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
						//System.out.println("这1");
						// Wait for the connection attempt to be finished.

						// cf.addListener(new ConnectListener()); //异步

						cf.awaitUninterruptibly(); // 同步 等待连接创建完成
						if (cf.isConnected() == true) {
							IoSession session = cf.getSession();// 获得session
							if (!session.isClosing() && session.isConnected()) {
								PacketService packet = new PacketService();
								String msg;
								boolean flag = SharedPrefsUtil
										.getSharedPrefsUtil().getValue(context,
												"isAutoLogin", false);
								if (flag) {
									if (((String) session
											.getAttribute("userName")) != null) {
										msg = FastJsonTools
												.createFastJsonString(packet
														.setPacket("heart",
																null, null,
																null, null));
									} else {
										String name = SharedPrefsUtil
												.getSharedPrefsUtil()
												.getValue(context, "userName",
														"");
										String pswd = SharedPrefsUtil
												.getSharedPrefsUtil()
												.getValue(context, "passWord",
														"");
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
						//System.out.println("这3");
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("链接不成功");
						break;
					}
				}

			}
			connector.dispose();// 网络断开，释放
			//System.out.println("释放connector");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (true);
	}

}
