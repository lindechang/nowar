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
		final int packHeadLength = 5; // �����ֵ��Ӱ�쵽�����ȡ Ҫ�Ż�
		// �Ȼ�ȡ�ϴεĴ��������ģ����п�����δ�����������
		in.order(ByteOrder.LITTLE_ENDIAN);
		Context ctx = getContext(session);
		// �Ȱѵ�ǰbuffer�е�����׷�ӵ�Context��buffer����
		ctx.append(in);
		// ��positionָ��0λ�ã���limitָ��ԭ����positionλ��
		IoBuffer buf = ctx.getBuffer();
		buf.flip();
		// int bufLength = buf.remaining();
		// �ж��ǲ����ַ���ָ��

		// Ȼ�����ݰ���Э����ж�ȡ
		while (buf.remaining() >= packHeadLength) {
			buf.mark();
			// ��ȡ��Ϣͷ����

			if (buf.get(0) == (byte) 0x2A) {// �ͻ�������
				// ����ȡ�İ�ͷ�Ƿ��������������Ļ����buffer
				byte flag = buf.get();
				int length = buf.getInt();
				// System.out.println("length:"+length);
				if (length < 0 || length > maxPackLength) {
					buf.clear();
					break;
				}
				// ��ȡ��������Ϣ������д��������У��Ա�IoHandler���д���
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
						WindowShow.println(format.format(new Date()) + "�յ��ͻ���"
								+ session.getRemoteAddress() + "��������");
					} else {
						out.write(packet);
						WindowShow.println(format.format(new Date()) + "�յ��ͻ���"
								+ packet.getUsername() + "����Ϣ��" + content);
					}

					// out.write(content);
				} else {
					// �����Ϣ��������
					// ��ָ�������ƶ���Ϣͷ����ʼλ��
					buf.reset();
					break;
				}

			} else {// �豸������
					// IoBuffer ll = buf.get(mm, 0, 11);
				int length = buf.get(10) + 13;
				System.out.println("���ȣ�" + length);

				// ����ȡ�İ�ͷ�Ƿ��������������Ļ����buffer
				if (length < 0 || length > maxPackLength) {
					buf.clear();
					break;
				}
				// ��ȡ��������Ϣ������д��������У��Ա�IoHandler���д���
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
					// �����Ϣ��������
					// ��ָ�������ƶ���Ϣͷ����ʼλ��
					buf.reset();
					break;
				}

			}

		}

		// ������Ϣ�����buffer�л������ݣ��ʹ�������
		if (buf.hasRemaining()) {
			// �ϰ�������ʣ�����ݷ���CONTEXT��
			// �������Ƶ�buffer����ǰ��
			IoBuffer temp = IoBuffer.allocate(maxPackLength)
					.setAutoExpand(true);
			temp.put(buf);
			temp.flip();
			buf.clear();
			buf.put(temp);

		} else {// ��������Ѿ�������ϣ��������
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

	// ��¼�����ģ���Ϊ���ݴ���û�й�ģ���ܿ���ֻ�յ����ݰ���һ��
	// ���ԣ���Ҫ������ƴ�������������Ĵ���
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
