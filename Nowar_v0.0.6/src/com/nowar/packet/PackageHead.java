package com.nowar.packet;

import java.io.Serializable;

public class PackageHead implements Serializable {

	  //private static final long serialVersionUID = 3965541808116510722L;
	  //private int id; //id
	  private int packageHeadLength; //��ͷ����  short 2���ֽ�  ����Ϊ10
	  private int messageType; //��Ϣ����  byte 1�ֽ�
	  private int contentType; //��������  1�ֽ� 
	  private int messageCommand;  //��Ϣ���� short 2�ֽ�
	  private int packageBodyLength;  //���峤�� ��Ϣint 4�ֽ�
	  private String packageBodyContent; //��������   ��С ΪpackageBodyLength
	  
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
