package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("Tb_buyingimg")
public class Tb_buyingImg {
	@Id
	private long buyingImgId;
	
	@Column
	private String buyingImgPath;
	
	@Column
	private long buyingId;
	
	public Tb_buyingImg() {
		// TODO Auto-generated constructor stub
	}

	public long getBuyingImgId() {
		return buyingImgId;
	}

	public void setBuyingImgId(long buyingImgId) {
		this.buyingImgId = buyingImgId;
	}

	public String getBuyingImgPath() {
		return buyingImgPath;
	}

	public void setBuyingImgPath(String buyingImgPath) {
		this.buyingImgPath = buyingImgPath;
	}

	public long getBuyingId() {
		return buyingId;
	}

	public void setBuyingId(long buyingId) {
		this.buyingId = buyingId;
	}
	
}
