package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tb_user")
public class Tb_user {
	
	@Id
	//用户id
	private long userId;
	
	@Column
	//用户邮箱
	private String userEmail;
	
	@Column
	//用户登录密码 md5加密
	private transient String userPassWord;
	
	@Column
	//用户名字
	private String userName;
	
	@Column
	//用户班级信息
	private String userMsg;
	
	@Column
	//用户学号
	private String userStudentId;
	
	@Column
	//用户头像路径
	private String userAvatar;

	@Column
	@ColDefine(customType = "DECIMAL(9,2)")
	//用户账户余额
	private float userMoney;
	
	@Column
	@Default("false")
	private boolean isLocked;
	
	
	public Tb_user() {
		// TODO Auto-generated constructor stub
	}
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public float getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(float userMoney) {
		this.userMoney = userMoney;
	}
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassWord() {
		return userPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		this.userPassWord = userPassWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMsg() {
		return userMsg;
	}

	public void setUserMsg(String userMsg) {
		this.userMsg = userMsg;
	}

	public String getUserStudentId() {
		return userStudentId;
	}

	public void setUserStudentId(String userStudentId) {
		this.userStudentId = userStudentId;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	
	public String toString() {
		return "user信息打印：学号:"+getUserId()+"邮箱："+getUserEmail()+
		"姓名"+getUserName()+"班级："+getUserMsg()+"学号："+getUserStudentId()+"头像路径"+getUserAvatar();
	}
}









