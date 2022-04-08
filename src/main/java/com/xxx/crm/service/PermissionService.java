package com.xxx.crm.service;

import com.xxx.crm.base.BaseService;
import com.xxx.crm.dao.PermissionMapper;
import com.xxx.crm.vo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Autowired
    private PermissionMapper permissionMapper;


 /*   public List<Integer> selectAclvalueByUserId(int id) {
        return permissionMapper.selectAclvalueByUserId(id);
    }*/

    public List<Integer> selectAclvalueByUserId(int id){
        return permissionMapper.selectAclvalueByUserId(id);
    }

}
