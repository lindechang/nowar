package xhs.tools.mina;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import xhs.json.service.FastJsonService;
import xhs.json.service.MessagePacket;
import xhs.json.service.MessageType;
import xhs.project.main.WindowShow;

public class MyProtocalDecoder implements ProtocolDecoder {
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss");
	private final Charset charset;
	private int maxPackLength = 100;

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
		final int packHeadLength = 5; // 这个数值修影响到下面读取 要优化
		// 先获取上次的处理上下文，其中可能有未处理完的数据
		in.order(ByteOrder.LITTLE_ENDIAN);
		Context ctx = getContext(session);
		// 先把当前buffer中的数据追加到Context的buffer当中
		ctx.append(in);
		// 把position指向0位置，把limit指向原来的position位置
		IoBuffer buf = ctx.getBuffer();
		buf.flip();
		// int bufLength = buf.remaining();
		// 判断是不是字符串指令

		// 然后按数据包的协议进行读取
		while (buf.remaining() >= packHeadLength) {
			buf.mark();
			// 读取消息头部分

			if (buf.get(0) == (byte) 0x2A) {// 客户端数据
				// 检查读取的包头是否正常，不正常的话清空buffer
				byte flag = buf.get();
				int length = buf.getInt();
				// System.out.println("length:"+length);
				if (length < 0 || length > maxPackLength) {
					buf.clear();
					break;
				}
				// 读取正常的消息包，并写入输出流中，以便IoHandler进行处理
				else if (length >= packHeadLength
						&& length  <= buf.remaining()) {
					int oldLimit2 = buf.limit();
					//buf.limit(buf.position() + length - packHeadLength);
					buf.limit(buf.position() + length);
					String content = buf.getString(ctx.getDecoder());

					// System.out.println("content:" + content);
					buf.limit(oldLimit2);
					// MyProtocalPack pack = new MyProtocalPack(flag, content);

					MessagePacket packet = FastJsonService.getPerson(
							(String) content, MessagePacket.class);
					if (packet.getType().equals(MessageType.HEART_BEAT)) {
						WindowShow.println(format.format(new Date()) + "收到客户："
								+ session.getRemoteAddress() + "的心跳包");
					} else {
						out.write(packet);
						WindowShow.println(format.format(new Date()) + "收到客户："
								+ packet.getUsername() + "的消息：" + content);
					}

					// out.write(content);
				} else {
					// 如果消息包不完整
					// 将指针重新移动消息头的起始位置
					buf.reset();
					break;
				}

			} else {// 设备端数据
					// IoBuffer ll = buf.get(mm, 0, 11);
				int length = buf.get(10) + 13;
				System.out.println("长度：" + length);

				// 检查读取的包头是否正常，不正常的话清空buffer
				if (length < 0 || length > maxPackLength) {
					buf.clear();
					break;
				}
				// 读取正常的消息包，并写入输出流中，以便IoHandler进行处理
				else if (length >= packHeadLength
						&& length - packHeadLength <= buf.remaining()) {
					byte[] data = new byte[length];
					buf.get(data, 0, length);
					// if ((data[0] == (byte) 0xfe) || (data[0] == (byte) 0xff))
					// {
					// session.write(IoBuffer.wrap(data));
					// }else{
					// out.write(data);
					// }
					out.write(data);
					break;
				} else {
					// 如果消息包不完整
					// 将指针重新移动消息头的起始位置
					buf.reset();
					break;
				}

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
			buf = IoBuffer.allocate(80).setAutoExpand(true);
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
