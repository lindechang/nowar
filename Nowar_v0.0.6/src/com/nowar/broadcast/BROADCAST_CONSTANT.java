package com.nowar.broadcast;

public interface BROADCAST_CONSTANT {

	public interface BLUETOOTH {

		/** ����������Ϣ�㲥 **/
		public static final String ENABLE = "com.nowar.bluetooth.ENABLE";

		/** ��ѯ��Ϣ�㲥 **/
		public static final String SEARCH = "com.nowar.bluetooth.SEARCH";

		/** ��ѯδ���豸�㲥 **/
		public static final String SEARCH_NEWDEVICE = "com.nowar.bluetooth.SEARCH_NEWDEVICE";
		/** ��ѯ�Ѱ��豸�㲥 **/
		public static final String SEARCH_BONDDEVICE = "com.nowar.bluetooth.SEARCH_BONDDEVICE";

		/** ���ӹ㲥 **/
		public static final String CONNECTED = "com.nowar.bluetooth.CONNECTED";

		/** �Ͽ����ӹ㲥 **/
		public static final String DISCONNECTED = "com.nowar.bluetooth.DISCONNECTED";

		/** ������Ϣ�㲥 **/
		public static final String MSG = "com.nowar.bluetooth.MSG";

		/** ��ѯ����汾��Ϣ�㲥 **/
		// public static final String SEARCH_VS = "com.nowar.SEARCH_VS";
		/** �����豸��Ϣ�㲥 **/
		// public static final String CONTROL = "com.nowar.CONTROL";
	}

	public interface NETWORK {

		/** ��½��Ϣ�㲥 **/
		public static final String LOGIN = "com.nowar.LOGIN";
		/** ע����Ϣ�㲥 **/
		public static final String REG = "com.nowar.REG";
		public static final String ACTION = "com.nowar.network.ACTION";
		public static final String HEART = "com.nowar.network.HEART";

		/** ������Ϣ�㲥 **/
		public static final String CHAT = "com.nowar.network.CHAT";

		public interface BASE {
			/** ������Ϣ�㲥 **/
			public static final String ENABLE = "com.nowar.network.ack";

		}
	}

}
