package xhs.tools.mina.code;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import xhs.json.service.FastJsonService;
import xhs.json.service.MessagePacket;
import xhs.json.service.MessageType;
import xhs.project.main.WindowShow;

public class UserDecoder implements MessageDecoder {

	private final Charset charset;
	private final int maxPackLength = 1024;
	private final byte DECODER_FLAG = (byte) 0x2a; // 解码标志
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss");

	public UserDecoder() {
		this(Charset.defaultCharset());
	}

	public UserDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public MessageDecoderResult decodable(IoSession saession, IoBuffer buf) {
		// WindowShow.println("UserDecoder------decodable");
		if (buf.remaining() < 5) {
			return MessageDecoderResult.NEED_DATA;
		} else {
			byte head = buf.get();
			int length = buf.getInt();
			if (length < 0 || length > maxPackLength) {
				return MessageDecoderResult.NOT_OK;
			} else {
				if (length <= buf.remaining()) {
					if (head == DECODER_FLAG) {
						return MessageDecoderResult.OK;
					} else {
						return MessageDecoderResult.NOT_OK;
					}
				} else {
					return MessageDecoderResult.NEED_DATA;
				}
			}

		}
		// return null;
	}

	@Override
	public MessageDecoderResult decode(IoSession session, IoBuffer buf,
			ProtocolDecoderOutput out) throws Exception {
		// WindowShow.println("UserDecoder------decode");
		buf.get();
		int packet_size = buf.getInt();
		// byte[] data = new byte[packet_size];
		// buf.get(data, 0, packet_size);
		String msg = buf.getString(packet_size, charset.newDecoder());
		// WindowShow.println("MSG:"+msg);
		// out.write(data);
		MessagePacket packet = FastJsonService.getPerson((String) msg,
				MessagePacket.class);

		if (packet.getType().equals(MessageType.HEART_BEAT)) {
			WindowShow.println(format.format(new Date()) + "收到客户："
					+ session.getRemoteAddress() + "的心跳包");
		} else {

			WindowShow.println(format.format(new Date()) + "收到客户："
					+ packet.getUsername() + "的消息：" + msg);
			// HandleUserData(session, packet);
			out.write(packet);
		}

		return MessageDecoderResult.OK;
	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
