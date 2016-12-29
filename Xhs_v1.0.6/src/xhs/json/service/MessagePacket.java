package xhs.json.service;

public class MessagePacket {
	
	private String type; //������
	private String username; //�û���
	private String password; //�û�����
	private String goal; //�豸���
	private String content; //����
    
	
	public MessagePacket(String type, String username, String password,String goal, String content) {
		super();
		
		this.type = type;
		this.username = username;
		this.password = password;
		this.content = content;
		this.goal = goal;
	}

	public String getGoal() {
		return goal;
	}



	public void setGoal(String goal) {
		this.goal = goal;
	}


	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}


	public MessagePacket() {
		// TODO Auto-generated constructor stub
	}


}
