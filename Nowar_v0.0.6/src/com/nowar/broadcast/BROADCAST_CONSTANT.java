package com.nowar.broadcast;

public interface BROADCAST_CONSTANT {

	public interface BLUETOOTH {

		/** 蓝牙开启信息广播 **/
		public static final String ENABLE = "com.nowar.bluetooth.ENABLE";

		/** 查询信息广播 **/
		public static final String SEARCH = "com.nowar.bluetooth.SEARCH";

		/** 查询未绑定设备广播 **/
		public static final String SEARCH_NEWDEVICE = "com.nowar.bluetooth.SEARCH_NEWDEVICE";
		/** 查询已绑定设备广播 **/
		public static final String SEARCH_BONDDEVICE = "com.nowar.bluetooth.SEARCH_BONDDEVICE";

		/** 链接广播 **/
		public static final String CONNECTED = "com.nowar.bluetooth.CONNECTED";

		/** 断开链接广播 **/
		public static final String DISCONNECTED = "com.nowar.bluetooth.DISCONNECTED";

		/** 接收信息广播 **/
		public static final String MSG = "com.nowar.bluetooth.MSG";

		/** 查询软件版本信息广播 **/
		// public static final String SEARCH_VS = "com.nowar.SEARCH_VS";
		/** 控制设备信息广播 **/
		// public static final String CONTROL = "com.nowar.CONTROL";
	}

	public interface NETWORK {

		/** 登陆信息广播 **/
		public static final String LOGIN = "com.nowar.LOGIN";
		/** 注册信息广播 **/
		public static final String REG = "com.nowar.REG";
		public static final String ACTION = "com.nowar.network.ACTION";
		public static final String HEART = "com.nowar.network.HEART";

		/** 聊天信息广播 **/
		public static final String CHAT = "com.nowar.network.CHAT";

		public interface BASE {
			/** 聊天信息广播 **/
			public static final String ENABLE = "com.nowar.network.ack";

		}
	}

}
