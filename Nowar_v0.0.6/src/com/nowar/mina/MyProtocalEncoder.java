package com.nowar.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import android.R.integer;

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
		try {
			//System.out.println("encode����:" + message);
			String value = (String) message;
			byte[] data = value.getBytes(charset);	
			IoBuffer buf = IoBuffer.allocate((int)(data.length + 5));
			buf.setAutoExpand(true);
			//int ii = value.getBytes().length+5;
			//System.out.println("value.getBytes().length:"+ii);
			// buf.putInt(value.getLength());
			// buf.put(value.getFlag());
			if (value != null) {
				buf.put((byte) 0x2A);//��ʶ�ַ�*
				buf.putInt((int) data.length);	
				buf.put(data);
			}
			buf.flip();
			out.write(buf);
		} catch (Exception e) {
			//System.out.println("app ������");
		}
		
		// out.flush(); //�����и���
	}

	public void dispose() throws Exception {
	}
}
