package com.nv.baonk.organ.vo;

import java.io.Serializable;
import java.util.List;

public class Department implements Serializable {
	private static final long serialVersionUID = 12235432564336L;

	private String departmentid;
	
	private int tenantid;

	private String departmentname;

	private String departmentpath;

	private String parentdept;

	private String email;	

	private List<Department> subDept;

	private String companyName;	

	private String companyId;	
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public int getTenantid() {
		return tenantid;
	}

	public void setTenantid(int tenantid) {
		this.tenantid = tenantid;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getDepartmentpath() {
		return departmentpath;
	}

	public void setDepartmentpath(String departmentpath) {
		this.departmentpath = departmentpath;
	}

	public String getParentdept() {
		return parentdept;
	}

	public void setParentdept(String parentdept) {
		this.parentdept = parentdept;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	

	public List<Department> getSubDept() {
		return subDept;
	}

	public void setSubDept(List<Department> subDept) {
		this.subDept = subDept;
	}
}