package xhs.tools.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

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
public class KeepAliveRequestTimeoutHandlerImpl implements
		KeepAliveRequestTimeoutHandler {
	@Override
	public void keepAliveRequestTimedOut(KeepAliveFilter filter,
			IoSession session) throws Exception {
		System.out.println("������ʱ��");
	}

}