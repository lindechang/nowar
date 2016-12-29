package xhs.tools.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import xhs.project.main.WindowShow;

public class MyProtocalEncoder extends ProtocolEncoderAdapter {
	private final Charset charset;

	public MyProtocalEncoder(Charset charset) {
		this.charset = charset;
	}

	/**
	 * ���������session.write(IoBuffer.wrap(data)); �򲻻����encode
	 * ԭ����fireFilter�У���message�����жϣ�����Ѿ���IoBuffer���͵ģ���ֱ��return�ˡ�
	 * session.write(��IoBuffer��)���ɽ���encode
	 * **/
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// MyProtocalPack value = (MyProtocalPack) message;
		//WindowShow.println("encode����:" + message);
		String value = (String) message;
		IoBuffer buf = IoBuffer.allocate(value.getBytes().length + 5);
		buf.setAutoExpand(true);
		// buf.putInt(value.getLength());
		// buf.put(value.getFlag());
		if (value != null) {
			buf.put((byte) 0x2A);//��ʶ�ַ�*
			buf.putInt((int) value.getBytes().length);		
			buf.put(value.trim().getBytes());
		}
		buf.flip();
		out.write(buf);
		// out.flush(); //�����и���

	}

	public void dispose() throws Exception {
	}
}
