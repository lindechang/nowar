package com.nowar.packet;

public class PacketService {

	public PacketService() {
		// TODO Auto-generated constructor stub
	}

	public MessagePacket setPacket(String type, String username, String password,
			String deviceNumber, String content) {
		MessagePacket packet = new MessagePacket(type, username, password, deviceNumber,
				content);
		return packet;
	}
	
	 PackageHead ph = new PackageHead();
	 
	 public PackageHead setPackageHead(String type, String username, String password,
				String deviceNumber, String content) {
		 PackageHead ph = new PackageHead();
		 ph.setPackageHeadLength(10);
		 ph.setMessageType(0x01);
		 ph.setContentType(0x01);
		 ph.setPackageBodyLength(01);
		 
//			ContentPacket packet = new ContentPacket(type, username, password, deviceNumber,
//					content);
			
			
			return ph;
		}

}
