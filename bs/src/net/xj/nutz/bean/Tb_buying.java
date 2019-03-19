package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("Tb_buying")//求购主题帖
public class Tb_buying {
	@Id
	private long buyingId;
	
	@Column
	private String buyingName;
	
	@Column
	@ColDefine(type=ColType.TEXT,width=800)
	private String buyingMsg;
	
	@Column
	private long userId;
	
	@Column
	private String userName;
	
	@Column
	private String buyingTime;
	
	@Column
	private int buyingStatus;
	
	public Tb_buying() {
		// TODO Auto-generated constructor stub
	}

	public long getBuyingId() {
		return buyingId;
	}

	public void setBuyingId(long buyingId) {
		this.buyingId = buyingId;
	}

	public String getBuyingName() {
		return buyingName;
	}

	public void setBuyingName(String buyingName) {
		this.buyingName = buyingName;
	}

	public String getBuyingMsg() {
		return buyingMsg;
	}

	public void setBuyingMsg(String buyingMsg) {
		this.buyingMsg = buyingMsg;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBuyingTime() {
		return buyingTime;
	}

	public void setBuyingTime(String buyingTime) {
		this.buyingTime = buyingTime;
	}

	public int getBuyingStatus() {
		return buyingStatus;
	}

	public void setBuyingStatus(int buyingStatus) {
		this.buyingStatus = buyingStatus;
	}
	
	
	
}
