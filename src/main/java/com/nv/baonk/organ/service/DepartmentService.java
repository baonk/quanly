package com.nv.baonk.organ.service;

import java.util.List;
import com.nv.baonk.organ.vo.Department;
import com.nv.baonk.organ.vo.SimpleDepartment;

public interface DepartmentService {
	public List<Department> getAllDepartmentsOfCompany(String companyId, int tenantID);
	public List<Department> getAllSubDepts(String deptID, int tenantID);
	public SimpleDepartment getSimpleDeptList(String deptID, int tenantID);
	public List<SimpleDepartment> getAllSimpleSubDepts(String parentID, int tenantID);
	public Department findByDepartmentidAndTenantid(String deptID, int tenantID);
	public void saveDept(Department dept);
	public void updateDept(Department dept);
	public void deleteDept(Department dept);
	public List<Department> findDeptsWithSearchOption(String deptID, String sStr, String field, int tenantId);
	public List<Department> findCompanyWithSearchOption(String sStr, String field, int tenantId);
	public List<Department> getDeptsByDeptNameSearch(String sStr, int tenantId);
}
