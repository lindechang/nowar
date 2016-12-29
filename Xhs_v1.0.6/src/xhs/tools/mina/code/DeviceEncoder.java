package xhs.tools.mina.code;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import xhs.project.main.WindowShow;
import xhs.tools.mina.packet.BytePacket;

public class DeviceEncoder implements MessageEncoder<BytePacket> {
	private final Charset charset;

	public DeviceEncoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public void encode(IoSession Session, BytePacket message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
//		 WindowShow.println("ProtocalEncoder_1------encode");
//		 IoBuffer buffer =
//		 IoBuffer.allocate(message.getPackageBodyLength()+5);
//		 buffer.put(message.getPackageHead());
//		 buffer.putInt(message.getPackageBodyLength());
//		 buffer.put(message.getPackageBodyContent().trim().getBytes());
//		 buffer.flip();
//		 out.write(buffer); 
	}

}
