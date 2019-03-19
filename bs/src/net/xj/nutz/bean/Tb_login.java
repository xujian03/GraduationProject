package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tb_login")
public class Tb_login {
	@Id
	private long loginId;
	
	@Column
	//登录次数
	private int loginCount;
	
	@Column 
	//登录时间
	private String loginDate;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "登录"+this.loginDate+this.loginCount;
	}
	public Tb_login() {
		// TODO Auto-generated constructor stub
	}
	public long getLoginId() {
		return loginId;
	}
	public void setLoginId(long loginId) {
		this.loginId = loginId;
	}
	public int getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	
}
