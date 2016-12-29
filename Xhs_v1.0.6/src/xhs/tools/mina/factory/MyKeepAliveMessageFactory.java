package xhs.tools.mina.factory;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import xhs.json.service.MessagePacket;
import xhs.json.tools.FastJsonTools;
import xhs.project.main.WindowShow;
import xhs.tools.mina.packet.StrPacket;


/**
 * @author lindec
 * 心跳工厂，设置为聋子型。
 * 只由服务器发送心跳包到客户端。（并且无需等待客户端反馈）
 * 
 */
public class MyKeepAliveMessageFactory implements KeepAliveMessageFactory {
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss");
	MessagePacket sendpacket = new MessagePacket("heart", null, null, null,"service_heart");
	String jsonString = FastJsonTools.createFastJsonString(sendpacket);
	/** 心跳包内容 */
	private  final String HEARTBEATREQUEST = jsonString; 
	//private static final String HEARTBEATRESPONSE = "2";

	@Override
	public boolean isRequest(IoSession session, Object message) {
		// System.out.println("请求心跳包信息: " + message);
		// if (message.equals(HEARTBEATREQUEST))
		// return true;
		return false;
	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		// System.out.println("响应心跳包信息: " + message);
		// if (message.equals(HEARTBEATRESPONSE))
		// return true;
		return false;
	}

	@Override
	public Object getRequest(IoSession session) {
		 WindowShow.println(format.format(new Date()) + "请求预设信息: "
		 + HEARTBEATREQUEST);
		/** 返回预设语句 */
		
		String msg = "heart";
		StrPacket heart = new StrPacket();
		heart.setPacketHead((byte) 0x2a);
		heart.setPacketBodyLength(msg.trim().length());
		heart.setPacketBodyContent(msg);	
		//return HEARTBEATREQUEST;
		return heart;
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		// System.out.println("响应预设信息: " + HEARTBEATRESPONSE);
		// /** 返回预设语句 */
		// return HEARTBEATRESPONSE;
		return null;
	}

}




