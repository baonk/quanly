package com.nv.baonk.login.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.nv.baonk.login.dao.RoleMapper;
import com.nv.baonk.login.dao.UserMapper;
import com.nv.baonk.login.vo.Role;
import com.nv.baonk.login.vo.User;

@Service
public class UserServiceImpl implements UserService {
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPass;
	
	@Override
	public User findUserByUseridAndTenantid(String userId, int tenantId) {
		return userMapper.findUserByUseridAndTenantid(userId, tenantId);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPass.encode(user.getPassword()));
		user.setActive(1);
		
		Role userRole = roleMapper.findByRolename("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userMapper.saveUser(user);
	}

	@Override
	public int getTenantId(String serverName) {
		return userMapper.getTenantId(serverName);
	}

	@Override
	public List<Integer> getRoleId(String userID, int tenantID) {
		return roleMapper.getRoleId(userID, tenantID);
	}

	@Override
	public Role findByRoleid(int roleId) {
		return roleMapper.findByRoleid(roleId);
	}

	@Override
	public Role findByRolename(String roleName) {
		return roleMapper.findByRolename(roleName);
	}

	@Override
	public List<User> findUsersInAdminMode(String deptID, int tenantId) {
		return userMapper.findUsersInAdminMode(deptID, tenantId);
	}

	@Override
	public void updateUser(User user) {
		userMapper.updateUser(user);
	}

	@Override
	public void deleteUser(User user) {	
		roleMapper.deleteUserRole(user.getUserid(), user.getTenantid());
		userMapper.deleteUser(user);
	}

	@Override
	public List<User> findAllCompanyEmployees(String companyId, int tenantId) {
		return userMapper.findAllCompanyEmployees(companyId, tenantId);
	}

	@Override
	public List<User> getAllUsersOfDepartment(String deptID, int tenantId) {
		return userMapper.getAllUsersOfDepartment(deptID, tenantId);
	}

	@Override
	public List<User> findUsersWithSearchOption(String deptID, String sStr, String field, int tenantId) {
		return userMapper.findUsersWithSearchOption(deptID, sStr, field, tenantId);
	}
}
