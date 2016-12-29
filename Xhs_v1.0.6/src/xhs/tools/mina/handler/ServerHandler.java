package xhs.tools.mina.handler;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

import xhs.json.service.FastJsonService;
import xhs.json.service.MessagePacket;
import xhs.json.service.MessageType;
import xhs.json.service.PacketService;
import xhs.json.tools.FastJsonTools;
import xhs.project.main.WindowShow;
import xhs.socket.service.SocketDao;
import xhs.socket.service.SocketService;
import xhs.tools.mina.packet.StrPacket;

public class ServerHandler extends IoHandlerAdapter {

	// ConcurrentHashMap线程安全的用于并发量大的Hash表数据结构
	private ConcurrentHashMap<String, IoSession> UserSessionMap;
	private ConcurrentHashMap<String, IoSession> DeviceSessionMap;
	// ConcurrentLinkedQueue线程安全的用于并发量大的链表数据结构
	private ConcurrentLinkedQueue<String> sensitiveWordList;
	private SocketService service;

	private MessagePacket sendpacket;
	private String jsonString;

	public static int size;

	private String sendMsg = null;
	private StrPacket ack = new StrPacket();

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss");

	// public static Map<String, IoSession> sessionMap = new HashMap<String,
	// IoSession>();
	// private String va = "323";

	private byte[] command_open_14 = new byte[] { (byte) 0x02, 0x00, 0x00,
			(byte) 0x39, (byte) 0x39, (byte) 0x39, (byte) 0x39, (byte) 0x39,
			(byte) 0x39, 0x10, 0x01, 0x01, 0x00, 0x00 };
	private byte[] command_close_14 = new byte[] { (byte) 0x02, 0x00, 0x00,
			(byte) 0x39, (byte) 0x39, (byte) 0x39, (byte) 0x39, (byte) 0x39,
			(byte) 0x39, 0x10, 0x01, 0x00, 0x00, 0x00 };

	public ServerHandler() {
		service = new SocketDao();
		UserSessionMap = new ConcurrentHashMap<String, IoSession>();
		DeviceSessionMap = new ConcurrentHashMap<String, IoSession>();
		sensitiveWordList = new ConcurrentLinkedQueue<String>();
	}

	public void sessionCreated(IoSession session) {
		// SocketSessionConfig cfg = (SocketSessionConfig)session.getConfig();
		// cfg.setReceiveBufferSize( 1024 * 1024);
		// cfg.setReadBufferSize(1024 * 1024);
		// cfg.setKeepAlive(true);
		// cfg.setSoLinger(0); //这个是根本解决问题的设置

		// WindowShow.println("创建session" + session);
		// 拿到所有的客户端Session
		// UserSessionMap.put((int) session.getId(), session);
		// Collection<IoSession> sessions = session.getService()
		// .getManagedSessions().values();
		// for (IoSession sess : sessions) {
		// //UserSessionMap.put((int) sess.getId(), sess);
		// System.out.println("session:" + sess);
		// }
	}

	public void sessionOpened(IoSession session) {
		size = session.getService().getManagedSessions().values().size();
		WindowShow.println("session数量:" + size);
		// System.out.println("打开session" + session.getId());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// byte[] data = (byte[])message;
		// String content = new String(data,"UTF-8");
		// MessagePacket packet = FastJsonService.getPerson(
		// (String) content, MessagePacket.class);
		//
		// // MessagePacket packet = FastJsonService.getPerson((String) msg,
		// // MessagePacket.class);
	

		boolean isByte = true;
		try {
			byte[] data = (byte[]) message;
		} catch (Exception e) {
			isByte = false; // 当传输过来的数据不是byte类型，抛出异常。
		}
		if (isByte) {
			HandleDeviceData(session, message);
		} else {
			HandleUserData(session, message);
		}
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		WindowShow.println("空闲间隔时间：" + format.format(new Date()));
		// session.close(true);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		WindowShow.println("服务器出现异常：" + cause);
		session.close(true);
	}

	public void sessionClosed(IoSession session) {
		// String UserNmae = (String) session.getAttribute("userName");
		// String DeviceNumber = (String) session.getAttribute("deviceNumber");
		// WindowShow.println("UserSessionMap.size()：" + UserSessionMap.size());
		//
		// if (UserNmae != null && UserNmae.trim() != "") {
		// WindowShow.println("UserNmae：" + UserNmae);
		// UserSessionMap.remove(UserNmae);
		// }
		// if (DeviceNumber != null && DeviceNumber.trim() != "") {
		// WindowShow.println("DeviceNumber：" + DeviceNumber);
		// DeviceSessionMap.remove(UserNmae);
		// }

		// Iterator iter = UserSessionMap.entrySet().iterator();
		// while (iter.hasNext()) {
		// Map.Entry entry = (Map.Entry) iter.next();
		// Object key = entry.getKey();
		// Object val = entry.getValue();
		// System.out.println("key：" + key + "---" + "val:" + val);
		// }
		size = session.getService().getManagedSessions().values().size();
		WindowShow.println("session数量:" + size);
		WindowShow.println("关闭了：" + session.getRemoteAddress());
	}

	// public void messageSent(IoSession session, Object message){
	// System.out.println("message：" + message);
	// }

	// 处理用户客户端请求
	private void HandleUserData(IoSession session, Object message) {
		String sql;
		MessagePacket packet = (MessagePacket) message;

		switch (packet.getType()) {
		case MessageType.LOGIN_VERIFY: // 登陆请求
			List<Object> params = new ArrayList<Object>();
			params.add(packet.getUsername());
			params.add(packet.getPassword());
			if (service.login(params)) {
				sendMsg = "login_success";
				// System.out.println("session" + session.getId());
				session.setAttribute("userName", packet.getUsername());
				UserSessionMap.put(packet.getUsername(), session);
				sql = "update userinfo set socket='" + 1 + "' where username='"
						+ packet.getUsername() + "'";
				service.addOrUpdate(sql, null);
			} else {
				sendMsg = "login_fail";
			}
			ack.setPacketHead((byte) 0x2a);
			sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
			jsonString = FastJsonTools.createFastJsonString(sendpacket);
			ack.setPacketBodyLength(jsonString.trim().length());
			ack.setPacketBodyContent(jsonString);
			session.write(ack);
			break;
		case MessageType.REG_VERIFY: // 注册请求
			sql = "select * from userinfo where username=?";
			List<Object> params_register = new ArrayList<Object>();
			params_register.add(packet.getUsername());
			Map<String, Object> map0 = service.checkDB(sql, params_register);
			if ((!map0.isEmpty() ? true : false)) {
				// /session.write("register_fail");// 用户存在
				sendMsg = "register_fail";
			} else {
				params_register.add(packet.getPassword());
				params_register.add(null);
				params_register.add(null);
				params_register.add(null);
				sql = "insert into userinfo(username,pswd,realname,email,socket) values(?,?,?,?,?)";
				if (service.addOrUpdate(sql, params_register)) {
					// session.write("register_success");
					sendMsg = "register_success";
				} else {
					// session.write("login_fail");
					sendMsg = "register_fail";
				}

			}
			ack.setPacketHead((byte) 0x2a);
			sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
			jsonString = FastJsonTools.createFastJsonString(sendpacket);
			ack.setPacketBodyLength(jsonString.trim().length());
			ack.setPacketBodyContent(jsonString);
			session.write(ack);
			break;
		case MessageType.CHECK_DEVICE:// 查询设备请求
			List<Object> params_check = new ArrayList<Object>();
			params_check.add(packet.getGoal());
			sql = "select * from device_sw where number=?";
			Map<String, Object> map = service.checkDB(sql, params_check);
			// boolean flag4 = !map.isEmpty() ? true : false;
			if (!map.isEmpty() ? true : false) {
				// session.write("check_success");
				sendMsg = "check_success";
			} else {
				// session.write("check_fail");
				sendMsg = "check_fail";
			}
			ack.setPacketHead((byte) 0x2a);
			sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
			jsonString = FastJsonTools.createFastJsonString(sendpacket);
			ack.setPacketBodyLength(jsonString.trim().length());
			ack.setPacketBodyContent(jsonString);
			session.write(ack);
			break;

		case MessageType.BINDING_DEVICE:// 绑定设备请求
			sql = "update device_sw set binduser='" + packet.getUsername()
					+ "' where number='" + packet.getGoal() + "'";
			// boolean flag5 = service.addOrUpdate(sql, null);
			if (service.addOrUpdate(sql, null)) {
				// session.write("binding_success");
				sendMsg = "binding_success";

			} else {
				// session.write("binding_fail");
				sendMsg = "binding_fail";
			}
			ack.setPacketHead((byte) 0x2a);
			sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
			jsonString = FastJsonTools.createFastJsonString(sendpacket);
			ack.setPacketBodyLength(jsonString.trim().length());
			ack.setPacketBodyContent(jsonString);
			session.write(ack);
			break;
		case MessageType.CHECK_NEWFRIEND:// 查询新好友请求
			List<Object> params_check_friend = new ArrayList<Object>();
			params_check_friend.add(packet.getGoal());
			sql = "select * from userinfo where username=?";
			Map<String, Object> friend_map = service.checkDB(sql,
					params_check_friend);
			// boolean flag4 = !map.isEmpty() ? true : false;
			if (!friend_map.isEmpty() ? true : false) {
				// session.write("check_success");
				sendMsg = "check_friend_success";
			} else {
				// session.write("check_fail");
				sendMsg = "check_friend_fail";
			}
			ack.setPacketHead((byte) 0x2a);
			sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
			jsonString = FastJsonTools.createFastJsonString(sendpacket);
			ack.setPacketBodyLength(jsonString.trim().length());
			ack.setPacketBodyContent(jsonString);
			session.write(ack);
			break;
		case MessageType.ADD_NEWFRIEND:// 添加新好友请求
			// sql = "update device_sw set binduser='" + packet.getUsername()
			// + "' where number='" + packet.getGoal() + "'";
			// boolean flag5 = service.addOrUpdate(sql, null);
			// String content = packet.getContent();
			String newfriend = packet.getGoal();

			List<Object> add_friend = new ArrayList<Object>();
			add_friend.add(packet.getUsername());
			add_friend.add(newfriend);
			int isok = 1;
			add_friend.add(isok);
			sql = "insert into friends(userID1,userID2,isok) values(?,?,?)";
			if (service.addOrUpdate(sql, add_friend)) {
				// session.write("binding_success");
				sendMsg = "request_success";

				// IoSession session = UserSessionMap.get(newfriend);
				// MessagePacket mpacket = new MessagePacket("notification",
				// "aaa", null, newfriend, null);
				// String msg = FastJsonTools.createFastJsonString(mpacket);
				ack.setPacketHead((byte) 0x2a);
				sendpacket = new MessagePacket("notification", "aaa", null,
						newfriend, null);
				jsonString = FastJsonTools.createFastJsonString(sendpacket);
				ack.setPacketBodyLength(jsonString.trim().length());
				ack.setPacketBodyContent(jsonString);
				session.write(ack);

				IoSession friendSocket = UserSessionMap.get(newfriend);
				if (friendSocket != null) {
					if (!friendSocket.isClosing() && friendSocket.isConnected()) {
						sendpacket = new MessagePacket("notification", "aaa",
								null, packet.getUsername(), null);
						jsonString = FastJsonTools
								.createFastJsonString(sendpacket);
						ack.setPacketBodyLength(jsonString.length());
						ack.setPacketBodyContent(jsonString);
						friendSocket.write(ack);
					}
				}

			} else {
				// session.write("binding_fail");
				sendMsg = "request_fail";
			}
			ack.setPacketHead((byte) 0x2a);
			sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
			jsonString = FastJsonTools.createFastJsonString(sendpacket);
			ack.setPacketBodyLength(jsonString.trim().length());
			ack.setPacketBodyContent(jsonString);
			session.write(ack);
			break;

		case MessageType.CHAT:
			String friend = packet.getGoal();
			 IoSession friendSocket = UserSessionMap.get(friend);
			sendpacket = new MessagePacket("chat", packet.getUsername(), null,
					null, packet.getContent());
			WindowShow.println(packet.getContent());
			ack.setPacketHead((byte) 0x2a);
			if (friendSocket != null) {
				if (!friendSocket.isClosing() && friendSocket.isConnected()) {
					jsonString = FastJsonTools.createFastJsonString(sendpacket);
					int jsonLength = jsonString.getBytes(Charset.forName("UTF-8")).length;
					WindowShow.println("聊天字节长度：" + jsonLength);
					ack.setPacketBodyLength(jsonLength);
					ack.setPacketBodyContent(jsonString);
					friendSocket.write(ack);
				}else{
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
						}
					}).start();
				}
			}else{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
					}
				}).start();
			}

//			jsonString = FastJsonTools.createFastJsonString(sendpacket);
//			int jsonLength = jsonString.getBytes(Charset.forName("UTF-8")).length;
//			WindowShow.println("聊天字节长度：" + jsonLength);
//			ack.setPacketBodyLength(jsonLength);
//			ack.setPacketBodyContent(jsonString);
//			session.write(ack);
			break;
		case MessageType.CHECK_VERSION:// apk版本更新请求
			String version = packet.getContent();
			String msg = null;
			if (version.equals("1.0")) {
				// msg = FastJsonTools.createFastJsonString(packetService
				// .setPacket("check_version_ack", null, null, null,
				// "old_version"));
				// session.write(msg);
				// session.write("old_version");
				sendMsg = "old_version";
			} else {
				// msg = FastJsonTools
				// .createFastJsonString(packetService
				// .setPacket("check_version_ack", null, null,
				// null,
				// "http://bcs.91.com/pcsuite-dev/apk/962d1d13a75150173666a99368081bde.apkhttp://bcs.91.com/pcsuite-dev/apk/962d1d13a75150173666a99368081bde.apk"));
				// WindowShow.println("发送msg长度："+msg.length());
				// session.write(msg);
				// session.write("new_version");
				sendMsg = "new_version";
			}
			ack.setPacketHead((byte) 0x2a);
			sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
			jsonString = FastJsonTools.createFastJsonString(sendpacket);
			ack.setPacketBodyLength(jsonString.trim().length());
			ack.setPacketBodyContent(jsonString);
			session.write(ack);
			break;
		case MessageType.COMMAND_DEVICE:// 指令控制设备请求
			String content = packet.getContent();
			String deviceNumber = packet.getGoal();
			IoSession client = DeviceSessionMap.get(deviceNumber);
			if (client != null) {
				if (!client.isClosing() && client.isConnected()) {
					if (content.equals("1")) {
						client.write(IoBuffer.wrap(command_open_14));
						WindowShow.print(format.format(new Date()) + "往设备端"
								+ session.getRemoteAddress() + "发送消息："
								+ "length:" + command_open_14.length + "->",
								command_open_14);
					}
					if (content.equals("0")) {
						client.write(IoBuffer.wrap(command_close_14));
						WindowShow.print(format.format(new Date()) + "往设备端"
								+ session.getRemoteAddress() + "发送消息："
								+ "length:" + command_close_14.length + "->",
								command_close_14);
					}
				} else {
					// session.write("device_disconnection");
					sendMsg = "device_disconnection";
					ack.setPacketHead((byte) 0x2a);
					sendpacket = new MessagePacket("ack", null, null, null,
							sendMsg);
					jsonString = FastJsonTools.createFastJsonString(sendpacket);
					ack.setPacketBodyLength(jsonString.trim().length());
					ack.setPacketBodyContent(jsonString);
					session.write(ack);
				}
			} else {
				// session.write("device_disconnection");
				sendMsg = "device_disconnection";
				ack.setPacketHead((byte) 0x2a);
				sendpacket = new MessagePacket("ack", null, null, null, sendMsg);
				jsonString = FastJsonTools.createFastJsonString(sendpacket);
				ack.setPacketBodyLength(jsonString.trim().length());
				ack.setPacketBodyContent(jsonString);
				session.write(ack);
			}

			break;
		default:
			break;
		}
	}

	private void HandleDeviceData(IoSession session, Object message) {
		String sql;

		byte[] data = (byte[]) message;

		WindowShow.print(
				format.format(new Date()) + "收到设备端"
						+ session.getRemoteAddress() + "消息：" + "length:"
						+ data.length + "->", data);
		byte[] ty = new byte[4];
		byte[] num = new byte[12];

		// for (int i = 0; i < data.length; i++) {
		// System.out.print(data[i] + ",");
		// if (i == (data.length - 1)) {
		// System.out.println(".");
		// }
		// }

		ty = tranString(data, 1, 3);
		num = tranString(data, 3, 9);
		String deviceType = new String(ty, 0, ty.length);
		String deviceNumber = new String(num, 0, num.length);
		// WindowShow.println("-deviceNumber-->>" + deviceNumber);
		if (data[0] == (byte) 0xFE) {
			if ((data[1] == 0x02) && (data[2] == 0x01)) {
				sql = "select * from device_sw where number=?";
				List<Object> params = new ArrayList<Object>();
				params.add(deviceNumber);
				Map<String, Object> map = service.checkDB(sql, params);
				// WindowShow.println("-socket--->>>" + map.get("socket"));
				boolean flag = !map.isEmpty() ? true : false;
				session.setAttribute("deviceNumber", deviceNumber);
				DeviceSessionMap.put(deviceNumber, session);
				if (!flag) {
					sql = "insert into device_sw(type,number,binduser,socket,status) values(?,?,?,?,?)";
					session.write(IoBuffer.wrap(data));
					// String address = client.getInetAddress().toString()
					// .replace("/", "");
					List<Object> addParams = new ArrayList<Object>();
					addParams.add(deviceType);
					addParams.add(deviceNumber);
					addParams.add(null);
					addParams.add("1");
					addParams.add(0);
					service.addOrUpdate(sql, addParams);
				} else {
					sql = "update device_sw set socket='" + "1"
							+ "' where number='" + deviceNumber + "'";
					// System.out.println("--数据库已存有该设备--->");
					// sendCommandToDevice(client, device_netin_ack_14);
					session.write(IoBuffer.wrap(data));
					service.addOrUpdate(sql, null);
				}
			}
		}
		if (data[0] == 0x02) {
			if ((data[1] == 0x02) && (data[2] == 0x01)) {
				IoSession socket = getBindUserSocket(deviceNumber);
				if (socket != null) {
					if (data[11] == 0x01) {
						// socket.write("open_success");
						sendMsg = "open_success";
						ack.setPacketHead((byte) 0x2a);
						sendpacket = new MessagePacket("ack", null, null, null,
								sendMsg);
						jsonString = FastJsonTools
								.createFastJsonString(sendpacket);
						ack.setPacketBodyLength(jsonString.trim().length());
						ack.setPacketBodyContent(jsonString);
						socket.write(ack);
					}
					if (data[11] == 0x00) {
						// socket.write("close_success");
						sendMsg = "close_success";
						ack.setPacketHead((byte) 0x2a);
						sendpacket = new MessagePacket("ack", null, null, null,
								sendMsg);
						jsonString = FastJsonTools
								.createFastJsonString(sendpacket);
						ack.setPacketBodyLength(jsonString.trim().length());
						ack.setPacketBodyContent(jsonString);
						socket.write(ack);
					}
				} else {
					WindowShow.println("用户不在线");

				}
			}
		}
	}

	/**
	 * @param deviceNumber
	 * @return 获得设备绑定用户的链接
	 */
	private IoSession getBindUserSocket(String deviceNumber) {
		IoSession client = null;
		String sql;
		sql = "select * from device_sw where number=?";
		List<Object> params = new ArrayList<Object>();
		params.add(deviceNumber);
		// Map<String, Object> map = service.checkDeviceDB(sql, params);
		String username = service.getSingleParam(sql, params, "binduser");
		if (username == null) {
			return client;
		}
		client = UserSessionMap.get(username);
		return client;
	}

	/**
	 * byte[]转换String
	 * 
	 * @param bmsg
	 * @param start
	 * @param end
	 * @return
	 */
	private byte[] tranString(byte[] bmsg, int start, int end) {
		int t = (end - start) * 2;
		byte[] tmpe = new byte[t];
		for (int j = 0, i = start; i < end; i++) {
			tmpe[j] = (byte) ((bmsg[i] >> 4) & 0x0f);
			if (tmpe[j] >= 0x0a)
				tmpe[j] += 0x37;
			else
				tmpe[j] += 0x30;
			tmpe[j + 1] = (byte) (bmsg[i] & 0x0f);
			if (tmpe[j + 1] >= 0x0a)
				tmpe[j + 1] += 0x37;
			else
				tmpe[j + 1] += 0x30;
			j += 2;
		}
		return tmpe;
	}

}
