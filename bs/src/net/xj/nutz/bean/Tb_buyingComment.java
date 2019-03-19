package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tb_buyingcomment")
public class Tb_buyingComment {
	@Id
	private long buyingCommentId;
	
	@Column
	@ColDefine(type=ColType.TEXT,width=1000)
	private String buyingCommentMsg;
	
	@Column
	private String userName;
	
	@Column
	private long userId;
	
	@Column
	private String buyingCommentTime;
	
	@Column
	private long buyingId;
	
	@Column//评论的评论
	private boolean isDoubleComment;
	
	@Column//是否是回复别人的回复
	private boolean isHaveReplyUser;
	
	@Column//评论 某回复 该回复的id
	private long flowCommentId;
	
	@Column
	private long flowUserId;
	
	@Column
	private String flowUserName;

	@Column
	@Default("false")
	private boolean isDelete;
	
	public long getBuyingCommentId() {
		return buyingCommentId;
	}

	public void setBuyingCommentId(long buyingCommentId) {
		this.buyingCommentId = buyingCommentId;
	}

	public String getBuyingCommentMsg() {
		return buyingCommentMsg;
	}

	public void setBuyingCommentMsg(String buyingCommentMsg) {
		this.buyingCommentMsg = buyingCommentMsg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getBuyingCommentTime() {
		return buyingCommentTime;
	}

	public void setBuyingCommentTime(String buyingCommentTime) {
		this.buyingCommentTime = buyingCommentTime;
	}

	public long getBuyingId() {
		return buyingId;
	}

	public void setBuyingId(long buyingId) {
		this.buyingId = buyingId;
	}

	public boolean isDoubleComment() {
		return isDoubleComment;
	}

	public void setDoubleComment(boolean isDoubleComment) {
		this.isDoubleComment = isDoubleComment;
	}

	public boolean isHaveReplyUser() {
		return isHaveReplyUser;
	}

	public void setHaveReplyUser(boolean isHaveReplyUser) {
		this.isHaveReplyUser = isHaveReplyUser;
	}

	public long getFlowCommentId() {
		return flowCommentId;
	}

	public void setFlowCommentId(long flowCommentId) {
		this.flowCommentId = flowCommentId;
	}

	public long getFlowUserId() {
		return flowUserId;
	}

	public void setFlowUserId(long flowUserId) {
		this.flowUserId = flowUserId;
	}

	public String getFlowUserName() {
		return flowUserName;
	}

	public void setFlowUserName(String flowUserName) {
		this.flowUserName = flowUserName;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	
	
}
