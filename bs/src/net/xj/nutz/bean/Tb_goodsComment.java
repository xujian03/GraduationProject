package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("Tb_goodsComment")//商品评论表-长度为800包括html代码
public class Tb_goodsComment {
	
	@Id
	private long goodsCommentId;
	
	
	@Column
	@ColDefine(type=ColType.TEXT,width=800)
	private String goodsCommentMsg;
	
	@Column
	private long goodsCommentGoodsId;
	
	@Column
	private String goodsCommentTime;
	
	@Column
	private long goodsCommentUserId;
	
	@Column
	private String goodsCommentUserName;
	
	@Column
	@Default("false")
	private Boolean goodsCommentIsdelete;
	
	@Column
	private String goodsCommentAimUser;
	
	@Column
	@Default("0")
	private long goodsCommentAimUserId;
	
	public Tb_goodsComment() {
		// TODO Auto-generated constructor stub
	}

	public String getGoodsCommentAimUser() {
		return goodsCommentAimUser;
	}

	public void setGoodsCommentAimUser(String goodsCommentAimUser) {
		this.goodsCommentAimUser = goodsCommentAimUser;
	}

	public long getGoodsCommentAimUserId() {
		return goodsCommentAimUserId;
	}

	public void setGoodsCommentAimUserId(long goodsCommentAimUserId) {
		this.goodsCommentAimUserId = goodsCommentAimUserId;
	}

	public long getGoodsCommentId() {
		return goodsCommentId;
	}

	public void setGoodsCommentId(long goodsCommentId) {
		this.goodsCommentId = goodsCommentId;
	}

	public String getGoodsCommentMsg() {
		return goodsCommentMsg;
	}

	public void setGoodsCommentMsg(String goodsCommentMsg) {
		this.goodsCommentMsg = goodsCommentMsg;
	}

	public long getGoodsCommentGoodsId() {
		return goodsCommentGoodsId;
	}

	public void setGoodsCommentGoodsId(long goodsCommentGoodsId) {
		this.goodsCommentGoodsId = goodsCommentGoodsId;
	}

	public String getGoodsCommentTime() {
		return goodsCommentTime;
	}

	public void setGoodsCommentTime(String goodsCommentTime) {
		this.goodsCommentTime = goodsCommentTime;
	}

	public long getGoodsCommentUserId() {
		return goodsCommentUserId;
	}

	public void setGoodsCommentUserId(long goodsCommentUserId) {
		this.goodsCommentUserId = goodsCommentUserId;
	}

	public String getGoodsCommentUserName() {
		return goodsCommentUserName;
	}

	public void setGoodsCommentUserName(String goodsCommentUserName) {
		this.goodsCommentUserName = goodsCommentUserName;
	}

	public Boolean getGoodsCommentIsdelete() {
		return goodsCommentIsdelete;
	}

	public void setGoodsCommentIsdelete(Boolean goodsCommentIsdelete) {
		this.goodsCommentIsdelete = goodsCommentIsdelete;
	}
	
}
