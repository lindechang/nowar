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
	 * 当外面调用session.write(IoBuffer.wrap(data)); 则不会进入encode
	 * 原来在fireFilter中，对message做了判断，如果已经是IoBuffer类型的，就直接return了。
	 * session.write(非IoBuffer类)即可进入encode
	 * **/
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// MyProtocalPack value = (MyProtocalPack) message;
		try {
			//System.out.println("encode编码:" + message);
			String value = (String) message;
			byte[] data = value.getBytes(charset);	
			IoBuffer buf = IoBuffer.allocate((int)(data.length + 5));
			buf.setAutoExpand(true);
			//int ii = value.getBytes().length+5;
			//System.out.println("value.getBytes().length:"+ii);
			// buf.putInt(value.getLength());
			// buf.put(value.getFlag());
			if (value != null) {
				buf.put((byte) 0x2A);//标识字符*
				buf.putInt((int) data.length);	
				buf.put(data);
			}
			buf.flip();
			out.write(buf);
		} catch (Exception e) {
			//System.out.println("app 有问题");
		}
		
		// out.flush(); //这里有个坑
	}

	public void dispose() throws Exception {
	}
}
