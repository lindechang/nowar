package xhs.tools.mina.packet;

public class StrPacket {
	private byte PacketHead; // 包头
	private int PacketBodyLength; // 包体长度 消息int 4字节
	private String PacketBodyContent; // 包体内容 大小 为packageBodyLength
	
	public byte getPacketHead() {
		return PacketHead;
	}
	public void setPacketHead(byte packetHead) {
		PacketHead = packetHead;
	}
	public int getPacketBodyLength() {
		return PacketBodyLength;
	}
	public void setPacketBodyLength(int packetBodyLength) {
		PacketBodyLength = packetBodyLength;
	}
	public String getPacketBodyContent() {
		return PacketBodyContent;
	}
	public void setPacketBodyContent(String packetBodyContent) {
		PacketBodyContent = packetBodyContent;
	}
}
