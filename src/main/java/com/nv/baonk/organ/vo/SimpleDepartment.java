package com.nv.baonk.organ.vo;

import java.io.Serializable;
import java.util.List;

public class SimpleDepartment implements Serializable{	
	private static final long serialVersionUID = 12342578678567L;
	private String departmentid;
	private String departmentname;
	private String companyid;
	private String companyname;
	private List<SimpleDepartment> subDept;	
	private int hasSubDept;
	
	public String getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public List<SimpleDepartment> getSubDept() {
		return subDept;
	}
	public void setSubDept(List<SimpleDepartment> subDept) {
		this.subDept = subDept;
	}
	public int getHasSubDept() {
		return hasSubDept;
	}
	public void setHasSubDept(int hasSubDept) {
		this.hasSubDept = hasSubDept;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
}