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
			connector.getSessionConfig().setReadBufferSize(1024 * 1024);// ���ͻ�����1M
			connector.getSessionConfig().setReceiveBufferSize(1024 * 1024);// ���ջ�����1M
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
			// connector.setDefaultRemoteAddress(new InetSocketAddress(
			// dstname, PORT));// ����Ĭ�Ϸ��ʵ�ַ
			NetworkUtil netWork = new NetworkUtil();
			if (netWork.isNetworkConnected(context)) {
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
						//System.out.println("��1");
						// Wait for the connection attempt to be finished.

						// cf.addListener(new ConnectListener()); //�첽

						cf.awaitUninterruptibly(); // ͬ�� �ȴ����Ӵ������
						if (cf.isConnected() == true) {
							IoSession session = cf.getSession();// ���session
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
						cf.getSession().getCloseFuture().awaitUninterruptibly(); // �ȴ����ӶϿ�
						//System.out.println("��3");
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("���Ӳ��ɹ�");
						break;
					}
				}

			}
			connector.dispose();// ����Ͽ����ͷ�
			//System.out.println("�ͷ�connector");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (true);
	}

}
