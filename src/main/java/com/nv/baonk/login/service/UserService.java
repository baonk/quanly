package com.nv.baonk.login.service;

import java.util.List;
import com.nv.baonk.login.vo.Role;
import com.nv.baonk.login.vo.User;

public interface UserService {
	public User findUserByUseridAndTenantid(String userId, int tenantId);
	public void saveUser(User user);
	public void updateUser(User user);
	public int getTenantId(String serverName);
	public List<Integer> getRoleId(String userID, int tenantID); 
	public Role findByRoleid(int roleId);	
	public Role findByRolename(String roleName);
	public List<User> findUsersInAdminMode(String deptID, int tenantId);
	public void deleteUser(User user);	
	public List<User> findAllCompanyEmployees(String companyId, int tenantId);
	public List<User> getAllUsersOfDepartment(String deptID, int tenantId);
	public List<User> findUsersWithSearchOption(String deptID, String sStr, String field, int tenantId);
	public void updateUserActive(String userId, int activeStatus, String companyId, int tenantId);
}