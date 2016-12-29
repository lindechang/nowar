package xhs.tools.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyProtocalCodecFactory implements ProtocolCodecFactory{
	
	 private final MyProtocalEncoder encoder;  
     private final MyProtocalDecoder decoder;
     public MyProtocalCodecFactory(Charset charset) {
    	 //DemuxingProtocolDecoder sdecoder = new DemuxingProtocolDecoder();  
    	 encoder=new MyProtocalEncoder(charset); 
    	 decoder=new MyProtocalDecoder(charset);
		// TODO Auto-generated constructor stub
	}
     
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

}
