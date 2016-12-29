package com.nowar.packet;

public class FriendPacket {
	
	private String type; //包类型
	private String username;
	private String password;
	private String friendname; //设备序号
	private String content; //内容
    
	
	public FriendPacket(String type, String username, String password,String friendName, String content) {
		super();
		
		this.type = type;
		this.username = username;
		this.password = password;
		this.friendname = friendName;
		this.content = content;
		
	}

	public String getFriendNamer() {
		return friendname;
	}



	public void setFriendName(String friendName) {
		this.friendname = friendName;
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



	



	public FriendPacket() {
		// TODO Auto-generated constructor stub
	}



	

	

}
