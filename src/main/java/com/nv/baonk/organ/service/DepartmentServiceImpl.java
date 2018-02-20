package com.nv.baonk.organ.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nv.baonk.organ.dao.DepartmentMapper;
import com.nv.baonk.organ.vo.Department;
import com.nv.baonk.organ.vo.SimpleDepartment;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentMapper deptMapper;
	
	@Override
	public List<Department> getAllDepartmentsOfCompany(String companyId, int tenantID) {
		return deptMapper.findByCompanyIdAndTenantid(companyId, tenantID);
	}

	@Override
	public SimpleDepartment getSimpleDeptList(String deptId, int tenantId) {
		return deptMapper.getSimpleDeptList(deptId, tenantId);
	}

	@Override
	public List<SimpleDepartment> getAllSimpleSubDepts(String parentId, int tenantId) {
		return deptMapper.getAllSimpleSubDepts(parentId, tenantId);
	}

	@Override
	public Department findByDepartmentidAndTenantid(String deptID, int tenantID) {
		return deptMapper.findByDepartmentidAndTenantid(deptID, tenantID);
	}

	@Override
	public void saveDept(Department dept) {
		deptMapper.insertDept(dept);
	}

	@Override
	public void updateDept(Department dept) {
		deptMapper.updateDept(dept);
	}

	@Override
	public List<Department> getAllSubDepts(String deptID, int tenantID) {
		return deptMapper.findByParentdeptAndTenantid(deptID, tenantID);
	}

	@Override
	public void deleteDept(Department dept) {
		deptMapper.deleteDept(dept);
	}

	@Override
	public List<Department> findDeptsWithSearchOption(String deptId, String sStr, String field, int tenantId) {
		return deptMapper.findDeptsWithSearchOption(deptId, sStr, field, tenantId);
	}

	@Override
	public List<Department> findCompanyWithSearchOption(String sStr, String field, int tenantId) {
		return deptMapper.findCompanyWithSearchOption(sStr, field, tenantId);
	}

	@Override
	public List<Department> getDeptsByDeptNameSearch(String sStr, int tenantId) {
		return deptMapper.getDeptsBySearchingDeptName(sStr, tenantId);
	}
}