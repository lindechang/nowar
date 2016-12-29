package com.nowar.packet;

import java.io.Serializable;

public class PackageHead implements Serializable {

	  //private static final long serialVersionUID = 3965541808116510722L;
	  //private int id; //id
	  private int packageHeadLength; //包头长度  short 2个字节  长度为10
	  private int messageType; //消息类型  byte 1字节
	  private int contentType; //内容类型  1字节 
	  private int messageCommand;  //消息命令 short 2字节
	  private int packageBodyLength;  //包体长度 消息int 4字节
	  private String packageBodyContent; //包体内容   大小 为packageBodyLength
	  
//	  public int getId() {
//	    return id;
//	  }
//
//	  public void setId(int id) {
//	    this.id = id;
//	  }

	  public int getPackageHeadLength() {
	    return packageHeadLength;
	  }

	  public void setPackageHeadLength(int packageHeadLength) {
	    this.packageHeadLength = packageHeadLength;
	  }

	  public int getMessageType() {
	    return messageType;
	  }

	  public void setMessageType(int messageType) {
	    this.messageType = messageType;
	  }

	  public int getContentType() {
	    return contentType;
	  }

	  public void setContentType(int contentType) {
	    this.contentType = contentType;
	  }

	  public int getMessageCommand() {
	    return messageCommand;
	  }

	  public void setMessageCommand(int messageCommand) {
	    this.messageCommand = messageCommand;
	  }

	  public int getPackageBodyLength() {
	    return packageBodyLength;
	  }

	  public void setPackageBodyLength(int packageBodyLength) {
	    this.packageBodyLength = packageBodyLength;
	  }

	  public String getPackageBodyContent() {
	    return packageBodyContent;
	  }

	  public void setPackageBodyContent(String packageBodyContent) {
	    this.packageBodyContent = packageBodyContent;
	  }

	  @Override
	  public String toString() {
	    return "Messeage is: command=" + getMessageCommand() + ", type=" + getMessageType() + ", contentLength=" + getPackageBodyLength() + ", content=" + getPackageBodyContent();
	  }
	  
	}
