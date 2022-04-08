package com.xxx.crm.dao;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.vo.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    //获取某个用户对应的角色数量
    public Integer countUserRole(Integer id);

    //删除用户角色
    public Integer deleteUserRole(Integer id);


    //批量添加



}