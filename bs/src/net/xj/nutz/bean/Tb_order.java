package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 
orderId	Long	id	
orderOutTradeNo	String	外部订单(我的网站创建的订单)no	





orderTradeNo	String	支付宝方订单no	
orderTotalAmount	Float  DECIMAL(9,2)	支付宝返回实付的金额	
userId	long	创建订单的用户	
orderStatus	int	状态	
orderCountMoney	Float  DECIMAL(9,2)	订单总价值	
orderTime	string	创建时间	
addressPhone	String	收货人手机号	
addressName	String	收货人姓名	
addressMsg	String	收货人地址				
orderDeliveryName	String	快递名	
orderDeliveryNo	String	快递单号	

 * @author Administrator
 *
 */
@Table("tb_order")//订单表
public class Tb_order {
	@Id
	private long orderId;
	
	@Column//外部订单(我的网站创建的订单)no	
	private String orderOutTradeNo;
	
	@Column//支付宝方订单no	
	private String orderTradeNo;
	
	@Column//支付宝返回实付的金额	
	@ColDefine(customType = "DECIMAL(9,2)")
	private float orderTotalAmount;
	
	@Column//创建订单的用户	
	private long userId;
	
	@Column//状态
	private int orderStatus;
	
	@Column//订单总价值	
	@ColDefine(customType = "DECIMAL(9,2)")
	private float orderCountMoney;

	@Column//创建时间
	private String orderTime;
	
	@Column//收货人手机号
	private String addressPhone;
	
	@Column//收货人姓名
	private String addressName;
	
	@Column//收货人地址	
	private String addressMsg;

	public Tb_order() {
		// TODO Auto-generated constructor stub
	}
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderOutTradeNo() {
		return orderOutTradeNo;
	}

	public void setOrderOutTradeNo(String orderOutTradeNo) {
		this.orderOutTradeNo = orderOutTradeNo;
	}

	public String getOrderTradeNo() {
		return orderTradeNo;
	}

	public void setOrderTradeNo(String orderTradeNo) {
		this.orderTradeNo = orderTradeNo;
	}

	public float getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(float orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public float getOrderCountMoney() {
		return orderCountMoney;
	}

	public void setOrderCountMoney(float orderCountMoney) {
		this.orderCountMoney = orderCountMoney;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
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

	public String getAddressMsg() {
		return addressMsg;
	}

	public void setAddressMsg(String addressMsg) {
		this.addressMsg = addressMsg;
	}

	
	
}
