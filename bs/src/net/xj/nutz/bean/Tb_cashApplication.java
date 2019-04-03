package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
/**
 * //提现申请
 * @author Administrator
 *
 */
@Table("tb_cashApplication")
public class Tb_cashApplication {
	@Id
	private long cashApplicationId;
	
	@Column
	private long userId;
	
	@Column
	@ColDefine(customType = "DECIMAL(9,2)")
	private float cashNumber;
	
	@Column//支付宝账号
	private String alipayCount;
	
	/**
	 * 状态 0为申请中 1为申请成功  -1为申请失败
	 */
	@Column
	private int cashApplicationStatus;
	
	
	@Column
	private String cashApplicationTime;


	


	


	/**
	 * @return the cashApplicationId
	 */
	public long getCashApplicationId() {
		return cashApplicationId;
	}


	/**
	 * @param cashApplicationId the cashApplicationId to set
	 */
	public void setCashApplicationId(long cashApplicationId) {
		this.cashApplicationId = cashApplicationId;
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
	 * @return the cashNumber
	 */
	public float getCashNumber() {
		return cashNumber;
	}


	/**
	 * @param cashNumber the cashNumber to set
	 */
	public void setCashNumber(float cashNumber) {
		this.cashNumber = cashNumber;
	}


	/**
	 * @return the alipayCount
	 */
	public String getAlipayCount() {
		return alipayCount;
	}


	/**
	 * @param alipayCount the alipayCount to set
	 */
	public void setAlipayCount(String alipayCount) {
		this.alipayCount = alipayCount;
	}


	/**
	 * @return the cashApplicationStatus
	 */
	public int getCashApplicationStatus() {
		return cashApplicationStatus;
	}


	/**
	 * @param cashApplicationStatus the cashApplicationStatus to set
	 */
	public void setCashApplicationStatus(int cashApplicationStatus) {
		this.cashApplicationStatus = cashApplicationStatus;
	}


	/**
	 * @return the cashApplicationTime
	 */
	public String getCashApplicationTime() {
		return cashApplicationTime;
	}


	/**
	 * @param cashApplicationTime the cashApplicationTime to set
	 */
	public void setCashApplicationTime(String cashApplicationTime) {
		this.cashApplicationTime = cashApplicationTime;
	}
	
	
}
