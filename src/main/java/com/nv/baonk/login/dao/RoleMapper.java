package com.nv.baonk.login.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.nv.baonk.login.vo.Role;

@Mapper
public interface RoleMapper {
	public List<Integer> getRoleId(@Param("userId") String userId, @Param("tenantId") int tenantId);
	public Role findByRoleid(@Param("roleId") int roleId);
	public Role findByRolename(@Param("roleName") String roleName);
	public void deleteUserRole(@Param("userId") String userId, @Param("tenantId") int tenantId);
}