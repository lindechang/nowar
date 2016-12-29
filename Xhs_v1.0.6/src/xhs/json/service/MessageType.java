package xhs.json.service;

public class MessageType {

	/** ��¼��֤������Ϣ���� **/
	public final static String LOGIN_VERIFY = "login";
	/** ע����֤������Ϣ���� **/
	public final static String REG_VERIFY = "register";
	/** ����������Ϣ���� **/
	public final static String HEART_BEAT = "heart";

	/** ��ѯ�豸��Ϣ���� **/
	public final static String CHECK_DEVICE = "check";
	/** ���豸��Ϣ���� **/
	public final static String BINDING_DEVICE = "binding";
	/** �汾������Ϣ���� **/
	public final static String CHECK_VERSION = "check_version";
	/** ָ������豸��Ϣ���� **/
	public final static String COMMAND_DEVICE = "sw";
	
	
	/** ��ѯ�º�����Ϣ���� **/
	public final static String CHECK_NEWFRIEND = "check_newfriend";
	/** ��Ӻ�����Ϣ���� **/
	public final static String ADD_NEWFRIEND = "add_newfriend";
	/** ������Ϣ���� **/
	public final static String CHAT = "chat";
	

	
	
	
	/** �����б�������Ϣ���� **/
	public final static int FRIEND_LIST = 0x0004;
	/** �����б���Ӧ��Ϣ���� **/
	public final static int FRIEND_LIST_ACK = 0x0005;
	/** ������Ϣ���� **/
	public final static int SEND_MESSAGE = 0x0006;
	/** ������Ϣ��Ӧ **/
	public final static int SEND_MESSAGE_ACK = 0x0007;
	/** ������Ϣ֪ͨ��Ӧ **/
	public final static int SEND_MESSAGE_ACK_NOTICE = 0x1000;
	/** ֪ͨ�û������� **/
	public final static int USER_ON_OFF_LINE_NOTICE = 0X1001;

	/** ��ͷ��С **/
	public final static int HEAD_LENGTH = 10;
	/** ���ص���Ϣ���� 0��������� **/
	public final static int MESSAGE_TYPE_PUSH = 0;
	/** ���ص���Ϣ���� 1������Ӧ **/
	public final static int MESSAGE_TYPE_REQUEST = 1;
	/** ���ص��������� 0 JsonObject **/
	public final static int CONTENT_TYPE_OBJECT = 0;
	/** ���ص��������� 1 JsonArray **/
	public final static int CONTENT_TYPE_ARRAY = 1;

}
