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
	 * 当外面调用session.write(IoBuffer.wrap(data)); 则不会进入encode
	 * 原来在fireFilter中，对message做了判断，如果已经是IoBuffer类型的，就直接return了。
	 * session.write(非IoBuffer类)即可进入encode
	 * **/
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// MyProtocalPack value = (MyProtocalPack) message;
		//WindowShow.println("encode编码:" + message);
		String value = (String) message;
		IoBuffer buf = IoBuffer.allocate(value.getBytes().length + 5);
		buf.setAutoExpand(true);
		// buf.putInt(value.getLength());
		// buf.put(value.getFlag());
		if (value != null) {
			buf.put((byte) 0x2A);//标识字符*
			buf.putInt((int) value.getBytes().length);		
			buf.put(value.trim().getBytes());
		}
		buf.flip();
		out.write(buf);
		// out.flush(); //这里有个坑

	}

	public void dispose() throws Exception {
	}
}
