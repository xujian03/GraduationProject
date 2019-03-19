package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 收货地址类
 * @author Administrator
 *
 */
@Table("Tb_address")
public class Tb_address {
	
	@Id
	//id
	private long  addressId;
	
	@Column
	//地址信息
	private String addressMsg;
	
	@Column
	//收货人手机
	private String addressPhone;
	
	@Column 
	//收货人姓名
	private String addressName;
	
	@Column
	//所属用户id
	private long userId;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "用户的id："+this.userId+"用户地址："+this.addressMsg+"用户姓名："+this.addressName+"用户手机："+this.addressPhone;
	}
	
	public Tb_address() {
		// TODO Auto-generated constructor stub
	}
	
	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public String getAddressMsg() {
		return addressMsg;
	}

	public void setAddressMsg(String addressMsg) {
		this.addressMsg = addressMsg;
	}

	public String getAddressPhone() {
		return addressPhone;
	}

	public void setAddressPhone(String addressPhone) {
		this.addressPhone = addressPhone;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
