package net.xj.nutz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("tb_classGrade")
public class Tb_classGrade {
	@Id
	private long classGradeId;
	
	@Column
	private long classCollegeId;
	
	@Column
	private String classGradeName;

	public Tb_classGrade() {
		// TODO Auto-generated constructor stub
	}
	public long getClassGradeId() {
		return classGradeId;
	}

	public void setClassGradeId(long classGradeId) {
		this.classGradeId = classGradeId;
	}

	public long getClassCollegeId() {
		return classCollegeId;
	}

	public void setClassCollegeId(long classCollegeId) {
		this.classCollegeId = classCollegeId;
	}

	public String getClassGradeName() {
		return classGradeName;
	}

	public void setClassGradeName(String classGradeName) {
		this.classGradeName = classGradeName;
	}
	
}
