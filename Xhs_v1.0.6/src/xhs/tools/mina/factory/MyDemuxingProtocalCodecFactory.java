package xhs.tools.mina.factory;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
import org.apache.mina.filter.codec.demux.DemuxingProtocolEncoder;
import xhs.tools.mina.code.*;
import xhs.tools.mina.packet.BytePacket;
import xhs.tools.mina.packet.StrPacket;

/**
 * @author lindec 多路编解码器
 */
public class MyDemuxingProtocalCodecFactory extends
		DemuxingProtocolCodecFactory {

	// private MyProtocalDecoder decoder;
	// // 编码协议类编码器
	DemuxingProtocolDecoder mDecoder = new DemuxingProtocolDecoder();
	DemuxingProtocolEncoder mEncoder = new DemuxingProtocolEncoder();

	//
	public MyDemuxingProtocalCodecFactory(Charset charset) {
		// 解码器
		mDecoder.addMessageDecoder(new UserDecoder(charset));
		mDecoder.addMessageDecoder(new DeviceDecoder(charset));
		// 编码器
		mEncoder.addMessageEncoder(StrPacket.class, new UserEncoder(charset));
		//mEncoder.addMessageEncoder(BytePacket.class, new DeviceEncoder(charset));
		mEncoder.addMessageEncoder(String.class,new StringEncoder(charset));
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		return mDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		return mEncoder;
	}
}
