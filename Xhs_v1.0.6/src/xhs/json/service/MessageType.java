package xhs.json.service;

public class MessageType {

	/** 登录验证请求消息类型 **/
	public final static String LOGIN_VERIFY = "login";
	/** 注册验证请求消息类型 **/
	public final static String REG_VERIFY = "register";
	/** 心跳请求消息类型 **/
	public final static String HEART_BEAT = "heart";

	/** 查询设备消息类型 **/
	public final static String CHECK_DEVICE = "check";
	/** 绑定设备消息类型 **/
	public final static String BINDING_DEVICE = "binding";
	/** 版本更新消息类型 **/
	public final static String CHECK_VERSION = "check_version";
	/** 指令控制设备消息类型 **/
	public final static String COMMAND_DEVICE = "sw";
	
	
	/** 查询新好友消息类型 **/
	public final static String CHECK_NEWFRIEND = "check_newfriend";
	/** 添加好友消息类型 **/
	public final static String ADD_NEWFRIEND = "add_newfriend";
	/** 聊天消息类型 **/
	public final static String CHAT = "chat";
	

	
	
	
	/** 好友列表请求消息类型 **/
	public final static int FRIEND_LIST = 0x0004;
	/** 好友列表响应消息类型 **/
	public final static int FRIEND_LIST_ACK = 0x0005;
	/** 发送消息请求 **/
	public final static int SEND_MESSAGE = 0x0006;
	/** 发送消息响应 **/
	public final static int SEND_MESSAGE_ACK = 0x0007;
	/** 发送消息通知响应 **/
	public final static int SEND_MESSAGE_ACK_NOTICE = 0x1000;
	/** 通知用户上下线 **/
	public final static int USER_ON_OFF_LINE_NOTICE = 0X1001;

	/** 包头大小 **/
	public final static int HEAD_LENGTH = 10;
	/** 返回的消息类型 0服务端推送 **/
	public final static int MESSAGE_TYPE_PUSH = 0;
	/** 返回的消息类型 1请求响应 **/
	public final static int MESSAGE_TYPE_REQUEST = 1;
	/** 返回的内容类型 0 JsonObject **/
	public final static int CONTENT_TYPE_OBJECT = 0;
	/** 返回的内容类型 1 JsonArray **/
	public final static int CONTENT_TYPE_ARRAY = 1;

}
