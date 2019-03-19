package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 商品表
 * @author Administrator
 *
 */
@Table("tb_goods")
public class Tb_goods {

	@Id
	private long goodsId;
	
	@Column
	//状态
	private int goodsStatus;
	
	@Column
	//用户id外键
	private long userId;
	
	@Column
	//大类别
	private String bigClass;
	
	@Column
	//小类别
	private String smallClass;
	
	@Column//书籍名称
	private String goodsName;
	
	

	@Column
	//商品描述
	private String goodsDescribe;
	
	@Column
	//订单id外键
	private String orderOutTradeNo;
	
	@Column
	//订单状态
	private int orderStatus;
	
	@Column//价格
	@ColDefine(customType = "DECIMAL(7,2)")
	private float goodsPrice;
	
	@Column//浏览次数
	private int goodsHeat;

	@Column//上传时间
	private String goodsTime;
	
	@Column//快递名
	private String goodsDeliveryName;
	
	@Column//快递号
	private String goodsDeliveryNo;
	
	public String getGoodsDeliveryName() {
		return goodsDeliveryName;
	}

	public void setGoodsDeliveryName(String goodsDeliveryName) {
		this.goodsDeliveryName = goodsDeliveryName;
	}



	public String getGoodsDeliveryNo() {
		return goodsDeliveryNo;
	}

	public void setGoodsDeliveryNo(String goodsDeliveryNo) {
		this.goodsDeliveryNo = goodsDeliveryNo;
	}

	public Tb_goods() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(int goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public long getUserId() {
		return userId;
	}
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsTime() {
		return goodsTime;
	}

	public void setGoodsTime(String goodsTime) {
		this.goodsTime = goodsTime;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getBigClass() {
		return bigClass;
	}

	public void setBigClass(String bigClass) {
		this.bigClass = bigClass;
	}

	public String getSmallCalss() {
		return smallClass;
	}

	public void setSmallCalss(String smallClass) {
		this.smallClass = smallClass;
	}

	public String getGoodsDescribe() {
		return goodsDescribe;
	}

	public void setGoodsDescribe(String goodsDescribe) {
		this.goodsDescribe = goodsDescribe;
	}

	

	public String getSmallClass() {
		return smallClass;
	}

	public void setSmallClass(String smallClass) {
		this.smallClass = smallClass;
	}

	public String getOrderOutTradeNo() {
		return orderOutTradeNo;
	}

	public void setOrderOutTradeNo(String orderOutTradeNo) {
		this.orderOutTradeNo = orderOutTradeNo;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsHeat() {
		return goodsHeat;
	}

	public void setGoodsHeat(int goodsHeat) {
		this.goodsHeat = goodsHeat;
	}
	
	
}
