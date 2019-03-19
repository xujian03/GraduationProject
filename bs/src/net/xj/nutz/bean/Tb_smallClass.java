package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tb_smallClass")//商品小类别
public class Tb_smallClass {

	@Id
	private long smallClassId;
	
	@Column//外键bigclass
	private long bigClassId;
	
	@Column
	private String smallClass;

	public Tb_smallClass() {
		// TODO Auto-generated constructor stub
	}
	public long getSmallClassId() {
		return smallClassId;
	}

	public void setSmallClassId(long smallClassId) {
		this.smallClassId = smallClassId;
	}

	public long getBigClassId() {
		return bigClassId;
	}

	public void setBigClassId(long bigClassId) {
		this.bigClassId = bigClassId;
	}

	public String getSmallClass() {
		return smallClass;
	}

	public void setSmallClass(String smallClass) {
		this.smallClass = smallClass;
	}
	
}
