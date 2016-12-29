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

	/** 30���ʱ */
	private static final int IDEL_TIMEOUT = 60;
	/** ��������ȴ���Ӧ��ʱ */
	 private static final int HEART_TIMEOUT = 5;
	/** 10�뷢��һ�������� */
	private static final int HEARTBEAT_RATE = 55;

	public ServerListener() {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		// ���ö�ȡ���ݵĻ�������С
		//acceptor.getSessionConfig().setReadBufferSize(1024);
		//acceptor.getSessionConfig().setReadBufferSize(1024*1024); 
		acceptor.getSessionConfig().setReadBufferSize(1024*1024);//���ͻ�����1M
		acceptor.getSessionConfig().setReceiveBufferSize(1024*1024);
		//acceptor.getSessionConfig().set
		
		//setReceiveBufferSize(1024*1024);//���ջ�����1M
		
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
		// ����ע�͵������Զ���Handler��ʽ
		// KeepAliveRequestTimeoutHandler heartBeatHandler = new
		// KeepAliveRequestTimeoutHandlerImpl();
		// ��������_������ ������Ҫ�ȴ�������ʱ����
		KeepAliveRequestTimeoutHandler heartBeatHandler = KeepAliveRequestTimeoutHandler.DEAF_SPEAKER;
		KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
				IdleStatus.BOTH_IDLE, heartBeatHandler,HEARTBEAT_RATE,HEART_TIMEOUT);
		// KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
		// IdleStatus.BOTH_IDLE);
		// �����Ƿ�forward����һ��filter
		// heartBeat.setForwardEvent(true);
		// ��������Ƶ��
		//heartBeat.setRequestInterval(HEARTBEAT_RATE);
		// ��������������� �ȴ�������ʱʱ�䡣 ������ʱ��������KeepAliveRequestTimeoutHandler
		// heartBeat.setRequestTimeout(HEART_TIMEOUT);
		acceptor.getFilterChain().addLast("heart", heartBeat);
		// acceptor.getSessionConfig().setBothIdleTime(IDEL_TIMEOUT);

		// ��дͨ������1���������κβ����ͽ������״̬
		// acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
		// IDEL_TIMEOUT);
		
		
		
		
		acceptor.setHandler(new ServerHandler());

		try {
			acceptor.bind(new InetSocketAddress(PORT1));
			acceptor.bind(new InetSocketAddress(PORT2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WindowShow.println("���������ڼ����豸�˿�" + PORT1 + "...");
		WindowShow.println("���������ڼ����豸�˿�" + PORT2 + "...");
	}

}
