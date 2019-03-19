package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tb_classCollege")//学院类别表
public class Tb_classCollege {
	@Id
	private long classCollegeId;
	
	@Column
	private String classCollegeName;

	public long getClassCollegeId() {
		return classCollegeId;
	}
	public Tb_classCollege() {
		// TODO Auto-generated constructor stub
	}
	
	public void setClassCollegeId(long classCollegeId) {
		this.classCollegeId = classCollegeId;
	}

	public String getClassCollegeName() {
		return classCollegeName;
	}

	public void setClassCollegeName(String classCollegeName) {
		this.classCollegeName = classCollegeName;
	}
	
	
	
}
