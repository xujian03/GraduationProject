package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tb_bigClass")//商品大类别
public class Tb_bigClass {
	@Id
	private long bigClassId;
	
	@Column
	private String bigClass;

	public Tb_bigClass() {
		// TODO Auto-generated constructor stub
	}
	public long getBigClassId() {
		return bigClassId;
	}

	public void setBigClassId(long bigClassId) {
		this.bigClassId = bigClassId;
	}

	public String getBigClass() {
		return bigClass;
	}

	public void setBigClass(String bigClass) {
		this.bigClass = bigClass;
	}
}
