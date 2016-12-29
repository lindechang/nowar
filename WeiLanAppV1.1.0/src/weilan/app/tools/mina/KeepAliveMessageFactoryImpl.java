package weilan.app.tools.mina;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import android.content.Context;
import weilan.app.data.StaticVariable;
import weilan.app.tools.fastjosn.FastJsonTools;
import weilan.app.tools.fastjosn.PacketService;
import weilan.app.tools.sharedprefs.SharedPrefsUtil;

/**
 * @author lindec 心跳工厂，设置为聋子型。 只由服务器发送心跳包到客户端。（并且无需等待客户端反馈）
 * 
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss");
	/** 心跳包内容 */
	private static String HEARTBEATREQUEST = "{\"type\":\"login_heart\"}";

	// private static final String HEARTBEATRESPONSE = "2";
	private Context context;

	public KeepAliveMessageFactoryImpl(Context context) {
		this.context = context;
	}

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
		// System.out.println(format.format(new Date()) + "请求预设信息: "
		// + HEARTBEATREQUEST);
		/** 返回预设语句 */
		PacketService packet = new PacketService();
		boolean flag = SharedPrefsUtil.getValue(context, "isAutoLogin", false);
		if (flag) {
			if (((String) session.getAttribute("userName")) != null) {
				HEARTBEATREQUEST = FastJsonTools.createFastJsonString(packet
						.setPacket("heart", null, null, null, null));
			} else {
				HEARTBEATREQUEST = FastJsonTools.createFastJsonString(packet
						.setPacket("login", SharedPrefsUtil.getValue(context,
								"userName", ""), SharedPrefsUtil.getValue(
								context, "passWord", ""), null, null));
			}

		} else {
			HEARTBEATREQUEST = FastJsonTools.createFastJsonString(packet
					.setPacket("heart", null, null, null, null));
		}

		return HEARTBEATREQUEST;
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		// System.out.println("响应预设信息: " + HEARTBEATRESPONSE);
		// /** 返回预设语句 */
		// return HEARTBEATRESPONSE;
		return null;
	}

}

/**
 * 对应上面的注释
 * KeepAliveFilter(heartBeatFactory,IdleStatus.BOTH_IDLE,heartBeatHandler)
 * 心跳超时处理 KeepAliveFilter 在没有收到心跳消息的响应时，会报告给的KeepAliveRequestTimeoutHandler。
 * 默认的处理是 KeepAliveRequestTimeoutHandler.CLOSE
 * （即如果不给handler参数，则会使用默认的从而Close这个Session）
 * 
 * @author cruise
 * 
 */

// private static class KeepAliveRequestTimeoutHandlerImpl implements
// KeepAliveRequestTimeoutHandler {
//
// @Override
// public void keepAliveRequestTimedOut(KeepAliveFilter filter,
// IoSession session) throws Exception {
// System.out.println("心跳超时！");
// }
//
// }
