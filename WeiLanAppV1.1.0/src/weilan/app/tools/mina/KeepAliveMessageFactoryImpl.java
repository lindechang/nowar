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
 * @author lindec ��������������Ϊ�����͡� ֻ�ɷ������������������ͻ��ˡ�����������ȴ��ͻ��˷�����
 * 
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss");
	/** ���������� */
	private static String HEARTBEATREQUEST = "{\"type\":\"login_heart\"}";

	// private static final String HEARTBEATRESPONSE = "2";
	private Context context;

	public KeepAliveMessageFactoryImpl(Context context) {
		this.context = context;
	}

	@Override
	public boolean isRequest(IoSession session, Object message) {
		// System.out.println("������������Ϣ: " + message);
		// if (message.equals(HEARTBEATREQUEST))
		// return true;
		return false;
	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		// System.out.println("��Ӧ��������Ϣ: " + message);
		// if (message.equals(HEARTBEATRESPONSE))
		// return true;
		return false;
	}

	@Override
	public Object getRequest(IoSession session) {
		// System.out.println(format.format(new Date()) + "����Ԥ����Ϣ: "
		// + HEARTBEATREQUEST);
		/** ����Ԥ����� */
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
		// System.out.println("��ӦԤ����Ϣ: " + HEARTBEATRESPONSE);
		// /** ����Ԥ����� */
		// return HEARTBEATRESPONSE;
		return null;
	}

}

/**
 * ��Ӧ�����ע��
 * KeepAliveFilter(heartBeatFactory,IdleStatus.BOTH_IDLE,heartBeatHandler)
 * ������ʱ���� KeepAliveFilter ��û���յ�������Ϣ����Ӧʱ���ᱨ�����KeepAliveRequestTimeoutHandler��
 * Ĭ�ϵĴ����� KeepAliveRequestTimeoutHandler.CLOSE
 * �����������handler���������ʹ��Ĭ�ϵĴӶ�Close���Session��
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
// System.out.println("������ʱ��");
// }
//
// }
