package xhs.tools.mina.packet;

public class StrPacket {
	private byte PacketHead; // ��ͷ
	private int PacketBodyLength; // ���峤�� ��Ϣint 4�ֽ�
	private String PacketBodyContent; // �������� ��С ΪpackageBodyLength
	
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
