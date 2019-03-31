package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import net.xj.nutz.ext.Messages;

@Table("Tb_message")
public class Tb_message {
	@Id
	private long messageId;
	
	@Column
	private long userId;
	
	@Column
	private String messageString;
	
	@Column
	private String MessageUrl;

	@Column
	private Boolean isRead;
	
	@Column
	private String messageTime;
	
	@Column
	private int messageLevel;

	/**
	 * @return the messageId
	 */
	public long getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	/**
	 * @return the messageUrl
	 */
	public String getMessageUrl() {
		return MessageUrl;
	}

	/**
	 * @param messageUrl the messageUrl to set
	 */
	public void setMessageUrl(String messageUrl) {
		MessageUrl = messageUrl;
	}
	/**
	 * @return the messageString
	 */
	public String getMessageString() {
		return messageString;
	}

	/**
	 * @param messageString the messageString to set
	 */
	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}

	/**
	 * @return the isRead
	 */
	public Boolean getIsRead() {
		return isRead;
	}

	/**
	 * @param isRead the isRead to set
	 */
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	/**
	 * @return the messageTime
	 */
	public String getMessageTime() {
		return messageTime;
	}

	/**
	 * @param messageTime the messageTime to set
	 */
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	/**
	 * @return the messageLevel
	 */
	public int getMessageLevel() {
		return messageLevel;
	}

	/**
	 * @param messageLevel the messageLevel to set
	 */
	public void setMessageLevel(int messageLevel) {
		this.messageLevel = messageLevel;
	}
	
	
}
