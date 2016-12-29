package xhs.socket.service;

import java.util.UUID;

public class UUIDTools {

	public UUIDTools() {
		// TODO Auto-generated constructor stub
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		//System.out.println("-uuid-->>"+uuid);
		return uuid.toString().replaceAll("-", "").substring(0, 8);
	}
	
	// public static void main(String[] args) {
	// String id = getUUID();
	// System.out.println("-uuid-->>"+id);
	// }
}
