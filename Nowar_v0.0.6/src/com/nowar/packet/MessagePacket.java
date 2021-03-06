package com.nowar.packet;

public class MessagePacket {
	
	private String type; //包类型
	private String username;
	private String password;
	private String goal; //设备序号或好友用户名
	private String content; //内容
    
	
	public MessagePacket(String type, String username, String password,String goal, String content) {
		super();
		
		this.type = type;
		this.username = username;
		this.password = password;
		this.goal = goal;
		this.content = content;
		
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
