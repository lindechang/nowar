package weilan.app.tools.mina;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import weilan.app.tools.fastjosn.FastJsonService;
import weilan.app.tools.fastjosn.MessagePacket;

public class MyProtocalDecoder implements ProtocolDecoder {
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
	private final Charset charset;
	private int maxPackLength = 1024;

	public MyProtocalDecoder() {
		this(Charset.defaultCharset());
	}

	public MyProtocalDecoder(Charset charset) {
		this.charset = charset;
	}

	public int getMaxLineLength() {
		return maxPackLength;
	}

	public void setMaxLineLength(int maxLineLength) {
		if (maxLineLength <= 0) {
			throw new IllegalArgumentException("maxLineLength: "
					+ maxLineLength);
		}
		this.maxPackLength = maxLineLength;
	}

	private Context getContext(IoSession session) {
		Context ctx;
		ctx = (Context) session.getAttribute(CONTEXT);
		if (ctx == null) {
			ctx = new Context();
			session.setAttribute(CONTEXT, ctx);
		}
		return ctx;
	}

	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		final int packHeadLength = 5;
		// 先获取上次的处理上下文，其中可能有未处理完的数据
		in.order(ByteOrder.LITTLE_ENDIAN);
		Context ctx = getContext(session);
		// 先把当前buffer中的数据追加到Context的buffer当中
		ctx.append(in);
		// 把position指向0位置，把limit指向原来的position位置
		IoBuffer buf = ctx.getBuffer();
		buf.flip();
		// int bufLength = buf.remaining();
		// System.out.println("buf:" + buf);
		// if (bufLength >= packHeadLength) {
		// String message = buf.getString(ctx.getDecoder());
		// out.write(message);
		// }

		// 然后按数据包的协议进行读取
		while (buf.remaining() >= packHeadLength) {
			buf.mark();
			// 读取消息头部分
			byte flag = buf.get();
			int length = buf.getInt();
			// 检查读取的包头是否正常，不正常的话清空buffer
			if (length < 0 || length > maxPackLength) {
				buf.clear();
				break;
			}
			// 读取正常的消息包，并写入输出流中，以便IoHandler进行处理
			else if (length >= packHeadLength && length <= buf.remaining()) {
				int oldLimit2 = buf.limit();
				buf.limit(buf.position() + length);
				// byte[] data = new byte[length];
				// buf.get(data, 0, length);
				// String content = new String(data,"UTF-8");
              
				String content = buf.getString(ctx.getDecoder());
				
				System.out.println("内容长度："+length+"内容包:" + content);

				
				try {
					 MessagePacket packet = FastJsonService.getPerson(
							 (String) content, MessagePacket.class);
							// out.write(packet);
					buf.limit(oldLimit2);
					// MyProtocalPack pack = new MyProtocalPack(flag, content);
					out.write(packet);
				} catch (Exception e) {
					e.printStackTrace();

					System.out.println("这里出错了");
				}

			} else {
				// 如果消息包不完整
				// 将指针重新移动消息头的起始位置
				buf.reset();
				break;
			}
		}

		// 处理消息，如果buffer中还有数据，就处理数据
		if (buf.hasRemaining()) {
			// 断包处理，将剩余数据放入CONTEXT中
			// 将数据移到buffer的最前面
			IoBuffer temp = IoBuffer.allocate(maxPackLength)
					.setAutoExpand(true);
			temp.put(buf);
			temp.flip();
			buf.clear();
			buf.put(temp);

		} else {// 如果数据已经处理完毕，进行清空
			buf.clear();
		}

	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}

	public void dispose(IoSession session) throws Exception {
		Context ctx = (Context) session.getAttribute(CONTEXT);
		if (ctx != null) {
			session.removeAttribute(CONTEXT);
		}
	}

	// 记录上下文，因为数据触发没有规模，很可能只收到数据包的一半
	// 所以，需要上下文拼起来才能完整的处理
	private class Context {
		private final CharsetDecoder decoder;
		private IoBuffer buf;
		private int matchCount = 0;
		private int overflowPosition = 0;

		private Context() {
			decoder = charset.newDecoder();
			buf = IoBuffer.allocate(800).setAutoExpand(true);
		}

		public CharsetDecoder getDecoder() {
			return decoder;
		}

		public IoBuffer getBuffer() {
			return buf;
		}

		public int getOverflowPosition() {
			return overflowPosition;
		}

		public int getMatchCount() {
			return matchCount;
		}

		public void setMatchCount(int matchCount) {
			this.matchCount = matchCount;
		}

		public void reset() {
			overflowPosition = 0;
			matchCount = 0;
			decoder.reset();
		}

		public void append(IoBuffer in) {
			getBuffer().put(in);

		}

	}
}
