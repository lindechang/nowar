package xhs.tools.mina.code;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import xhs.project.main.WindowShow;
import xhs.tools.mina.packet.StrPacket;

public class UserEncoder implements MessageEncoder<StrPacket> {
	private final Charset charset;

	public UserEncoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public void encode(IoSession Session, StrPacket message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		// WindowShow.println("ProtocalEncoder_1------encode");
		CharsetEncoder ce = charset.newEncoder();
		// WindowShow.println("进入编码");
		IoBuffer buffer = IoBuffer.allocate(message.getPacketBodyLength() + 5);
		buffer.setAutoExpand(true);
		buffer.put(message.getPacketHead());
		buffer.putInt(message.getPacketBodyLength());
		// try {
		// WindowShow.println("进入编码:" + message.getPacketBodyContent());
		buffer.put(message.getPacketBodyContent().getBytes(charset));
		buffer.flip();
		out.write(buffer);
		// } catch (Exception e) {
		// e.printStackTrace();
		// WindowShow.println("这里出错");
		// }

		// out.flush();//这个问题是之前测试上万个连接的pingpong发现的
		// 加了这行会在收发负载量大时因为mina的OrderedThreadPoolExecutor多线程模型导致内部抛异常、状态异常。。

	}

}
