package xhs.json.service;

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

}
