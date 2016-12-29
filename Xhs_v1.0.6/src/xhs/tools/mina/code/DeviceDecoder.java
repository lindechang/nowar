package xhs.tools.mina.code;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class DeviceDecoder implements MessageDecoder{

	private final Charset charset;
	private final int maxPackLength = 1024;
	private final byte DECODER_FLAG = 0x23; // Ω‚¬Î±Í÷æ
	
	public DeviceDecoder() {
		this(Charset.defaultCharset());
	}

	public DeviceDecoder(Charset charset) {
		this.charset = charset;
	}
	@Override
	public MessageDecoderResult decodable(IoSession session, IoBuffer buf) {
		// TODO Auto-generated method stub
		if (buf.remaining() < 5) {
			return MessageDecoderResult.NEED_DATA;
		} else {
			int length = buf.get(10) + 13;		
			if (length < 0 || length > maxPackLength) {
				return MessageDecoderResult.NOT_OK;
			} else {
				if (length <= buf.remaining()) {
					return MessageDecoderResult.OK;
				} else {
					return MessageDecoderResult.NEED_DATA;
				}
			}
		}
	}

	@Override
	public MessageDecoderResult decode(IoSession session, IoBuffer buf,
			ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		int length = buf.get(10) + 13;
		byte[] data = new byte[length];
		buf.get(data, 0, length);
		out.write(data);
		return MessageDecoderResult.OK;
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
