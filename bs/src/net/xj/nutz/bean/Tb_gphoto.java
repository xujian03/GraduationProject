package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
/**
 * 商品图片
 * @author Administrator
 *
 */
@Table("tb_gphoto")
public class Tb_gphoto {
	@Id
	private long gphotoId;
	
	@Column
	private String gphotoPath;
	
	@Column
	private long goodsId;
	
	@Column//第几张图片
	private int gphotoRank;
	
	@Column//该商品总共有几张图片
	private int gphotoCount;
	
	public long getGphotoId() {
		return gphotoId;
	}

	public void setGphotoId(long gphotoId) {
		this.gphotoId = gphotoId;
	}

	public String getGphotoPath() {
		return gphotoPath;
	}

	public void setGphotoPath(String gphotoPath) {
		this.gphotoPath = gphotoPath;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public int getGphotoRank() {
		return gphotoRank;
	}

	public void setGphotoRank(int gphotoRank) {
		this.gphotoRank = gphotoRank;
	}

	public int getGphotoCount() {
		return gphotoCount;
	}

	public void setGphotoCount(int gphotoCount) {
		this.gphotoCount = gphotoCount;
	}

	
}
