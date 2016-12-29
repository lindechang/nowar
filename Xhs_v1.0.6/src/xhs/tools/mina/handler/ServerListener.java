package xhs.tools.mina.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import xhs.project.main.WindowShow;
import xhs.tools.mina.KeepAliveRequestTimeoutHandlerImpl;
import xhs.tools.mina.MyProtocalCodecFactory;
import xhs.tools.mina.factory.MyDemuxingProtocalCodecFactory;
import xhs.tools.mina.factory.MyKeepAliveMessageFactory;

public class ServerListener {

	private static final int PORT1 = 8881;
	private static final int PORT2 = 8889;

	/** 30秒后超时 */
	private static final int IDEL_TIMEOUT = 60;
	/** 心跳请求等待响应超时 */
	 private static final int HEART_TIMEOUT = 5;
	/** 10秒发送一次心跳包 */
	private static final int HEARTBEAT_RATE = 55;

	public ServerListener() {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		// 设置读取数据的缓冲区大小
		//acceptor.getSessionConfig().setReadBufferSize(1024);
		//acceptor.getSessionConfig().setReadBufferSize(1024*1024); 
		acceptor.getSessionConfig().setReadBufferSize(1024*1024);//发送缓冲区1M
		acceptor.getSessionConfig().setReceiveBufferSize(1024*1024);
		//acceptor.getSessionConfig().set
		
		//setReceiveBufferSize(1024*1024);//接收缓冲区1M
		
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new MyDemuxingProtocalCodecFactory(Charset
						.forName("UTF-8"))));
		KeepAliveMessageFactory heartBeatFactory = new MyKeepAliveMessageFactory();
		
		// acceptor.getFilterChain().addLast(
		// "codec",
		// new ProtocolCodecFilter(new MyProtocalCodecFactory(Charset
		// .forName("UTF-8"))));
		
		// IoFilter filter = new ProtocolCodecFilter(new
		// TextLineCodecFactory());
		// acceptor.getFilterChain().addLast("vestigge", filter);
		
		//KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
		// 下面注释掉的是自定义Handler方式
		// KeepAliveRequestTimeoutHandler heartBeatHandler = new
		// KeepAliveRequestTimeoutHandlerImpl();
		// 心跳机制_聋子型 ：不需要等待反馈超时处理
		KeepAliveRequestTimeoutHandler heartBeatHandler = KeepAliveRequestTimeoutHandler.DEAF_SPEAKER;
		KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
				IdleStatus.BOTH_IDLE, heartBeatHandler,HEARTBEAT_RATE,HEART_TIMEOUT);
		// KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
		// IdleStatus.BOTH_IDLE);
		// 设置是否forward到下一个filter
		// heartBeat.setForwardEvent(true);
		// 设置心跳频率
		//heartBeat.setRequestInterval(HEARTBEAT_RATE);
		// 设置心跳包请求后 等待反馈超时时间。 超过该时间后则调用KeepAliveRequestTimeoutHandler
		// heartBeat.setRequestTimeout(HEART_TIMEOUT);
		acceptor.getFilterChain().addLast("heart", heartBeat);
		// acceptor.getSessionConfig().setBothIdleTime(IDEL_TIMEOUT);

		// 读写通道均在1分钟内无任何操作就进入空闲状态
		// acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
		// IDEL_TIMEOUT);
		
		
		
		
		acceptor.setHandler(new ServerHandler());

		try {
			acceptor.bind(new InetSocketAddress(PORT1));
			acceptor.bind(new InetSocketAddress(PORT2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WindowShow.println("服务器正在监听设备端口" + PORT1 + "...");
		WindowShow.println("服务器正在监听设备端口" + PORT2 + "...");
	}

}
