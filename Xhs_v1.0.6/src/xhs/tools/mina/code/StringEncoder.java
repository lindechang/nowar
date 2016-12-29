package xhs.tools.mina.code;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import xhs.project.main.WindowShow;
import xhs.tools.mina.packet.StrPacket;

public class StringEncoder implements MessageEncoder<String> {
	private final Charset charset;

	public StringEncoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public void encode(IoSession Session, String message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		// WindowShow.println("ProtocalEncoder_1------encode");
		try {
			System.out.println("进入String编码");
			int length = message.getBytes().length;
			IoBuffer buffer = IoBuffer
					.allocate(length+5);
			buffer.put((byte)0x2a);
			
			buffer.putInt(length);
			// String srtbyte = null;
			// try {
			// byte[] mm= message.getPacketBodyContent().trim().getBytes();
			// srtbyte = new String(mm);
			// } catch (Exception e) {
			// e.printStackTrace();
			// WindowShow.println("这里出错");
			// }
			buffer.put(message.getBytes());
			buffer.flip();
			out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
