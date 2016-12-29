package com.nowar.db;

public interface DB_CONSTANT {

	public static final String DATABASE_NAME = "mydb";

	public interface TABLES {
		public interface DEVICE {

		}
		/**
		 * @author lindec 开关表
		 */
		public interface SWDEVICE {
			public final static String SWDEVICE_TABLE = "swdevice_table";

			public interface FIELDS {
				public final static String SWDEVICE_ID = "swdevice_id";
				public final static String SWDEVICE_SELECT = "swdevice_select";
				public final static String SWDEVICE_NAME = "swdevice_name";
				public final static String SWDEVICE_NUMBER = "swdevice_number";
				public final static String SWDEVICE_SET = "swdevice_set";
			}

			public interface SQL {

			}

		}

		/**
		 * @author lindec 好友表
		 */
		public interface FRIENDS {

		}

		/**
		 * @author lindec 聊天记录
		 */
		public interface MSGEX {
			public final static String MSGEX_TABLE = "msgex_table";
			public interface FIELDS {
				public final static String MSGEX_ID = "msgId";
				public final static String MSGEX_TYPE = "type";
				public final static String MSGEX_STATUS = "status";
				
				public final static String MSGEX_ISSEND = "isSend";
				public final static String MSGEX_ISSHOWTIME = "isShowTime";
				public final static String MSGEX_CREATETIME = "createTime";
				
				public final static String MSGEX_TALKER = "talker";
				public final static String MSGEX_CONTENT = "content";
				public final static String MSGEX_IMGPATH = "imgPath";
				public final static String MSGEX_RESERVED = "reserved";
			}
		}

	}

}
