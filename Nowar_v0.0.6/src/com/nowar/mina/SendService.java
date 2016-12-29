package com.nowar.mina;

import com.nowar.fastjson.FastJsonTools;
import com.nowar.packet.MessagePacket;



public class SendService {
	public static synchronized boolean sendData(MessagePacket packet) {
		boolean flag = false;
		if (ClientHandler.session == null) {
			return false;
		}
		try {
			String msg = FastJsonTools.createFastJsonString(packet);
			if (!ClientHandler.session.isClosing()
					&& ClientHandler.session.isConnected()) {				
				ClientHandler.session.write(msg);
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			//System.out.println("·¢ËÍ´íÎó");
		}
		return flag;
	}

}
