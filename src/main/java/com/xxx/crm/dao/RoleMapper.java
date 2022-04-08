package com.xxx.crm.dao;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.query.RoleQuery;
import com.xxx.crm.vo.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper extends BaseMapper<Role,Integer> {

  /*  !--查询对应的角色名称和id反馈给前台使用--><*/
    public List<Map<String,Object>> queryRole(Integer id);

    public Role queryRoleByName(String roleName);

    public List<Role> selectByParam(RoleQuery roleQuery);
}