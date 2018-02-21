package com.nv.baonk.login.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.nv.baonk.login.vo.User;

@Mapper
public interface UserMapper {
	public int getTenantId(@Param("serverName") String serverName);
	public User findUserByUseridAndTenantid(@Param("userId") String userId, @Param("tenantId") int tenantId);
	public List<User> findUsersInAdminMode(@Param("deptId") String deptId, @Param("tenantId") int tenantId);
	public List<User> findAllCompanyEmployees(@Param("companyId") String companyId, @Param("tenantId") int tenantId);
	public List<User> getAllUsersOfDepartment(@Param("deptId") String deptId, @Param("tenantId") int tenantId);
	public List<User> findUsersWithSearchOption(@Param("departId") String deptId, @Param("searchStr") String sStr, @Param("columnName") String field, @Param("tenantId") int tenantId);
	public void updateUserActive(@Param("userId") String userId, @Param("status") int activeStatus, @Param("companyId") String companyId, @Param("tenantId") int tenantId);
	public void saveUser(User user);
	public void updateUser(User user);
	public void deleteUser(User user);
}