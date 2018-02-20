package com.nv.baonk.organ.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.nv.baonk.organ.vo.Department;
import com.nv.baonk.organ.vo.SimpleDepartment;

@Mapper
public interface DepartmentMapper {
	Department findByDepartmentidAndTenantid(@Param("departId") String deptId, @Param("tenantId")int tenant_id);
	List<Department> findByParentdeptAndTenantid(@Param("parentId") String parentDept, @Param("tenantId") int tenant_id);
	List<Department> findByCompanyIdAndTenantid(@Param("companyId") String companyId, @Param("tenantId") int tenantId);
	List<Department> findDeptsWithSearchOption(@Param("departId")String deptId, @Param("searchStr") String sStr, @Param("columnName") String field, @Param("tenantId") int tenantId);
	List<Department> findCompanyWithSearchOption(@Param("searchStr") String sStr, @Param("columnName") String field, @Param("tenantId") int tenantId);
	List<Department> getDeptsBySearchingDeptName(@Param("departName") String deptName, @Param("tenantId") int tenantId);
	List<SimpleDepartment> getAllSimpleSubDepts(@Param("parentId") String parentId, @Param("tenantId") int tenantID);
	SimpleDepartment getSimpleDeptList(@Param("departId") String deptId, @Param("tenantId") int tenantId);
	void deleteDept(Department dept);
	void insertDept(Department dept);
	void updateDept(Department dept);
}