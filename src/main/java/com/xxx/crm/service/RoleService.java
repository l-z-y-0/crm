package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.dao.ModuleMapper;
import com.xxx.crm.dao.PermissionMapper;
import com.xxx.crm.dao.RoleMapper;
import com.xxx.crm.query.RoleQuery;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.vo.Permission;
import com.xxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    //查询对应角色分配给前台
    public List<Map<String,Object>> queryRole(Integer id){
        return roleMapper.queryRole(id);
    }


    /*    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp,"该角色已存在!");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(role)<1,"角色记录添加失败!");
    }*/
    public void saveRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        Role temp = roleMapper.queryRoleByName(role.getRoleName());
        AssertUtil.isTrue(null!=temp,"该角色已经存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(role)<1,"用户记录添加失败");
    }

    /*   @Transactional(propagation = Propagation.REQUIRED)
    public void  updateRole(Role role){
        AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待修改的记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(role.getId())),"该角色已存在!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色记录更新失败!");
    }*/

    public void updateRole(Role role){
        AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待修改的记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        Role temp = roleMapper.queryRoleByName(role.getRoleName());
        AssertUtil.isTrue(null!=temp && !(temp.getId()).equals(role.getId()),"该角色已经存在");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"角色更新失败");
    }

    public Map<String,Object> selectByTable(RoleQuery roleQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        List<Role> list = roleMapper.selectByParam(roleQuery);
        PageInfo pageInfo = new PageInfo(list);

        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;


    }

    //删除角色
    public void deleteRole(Integer id){
        AssertUtil.isTrue(id==null,"id不得为空");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(id)==null,"要删除的数据不存在");
        AssertUtil.isTrue(roleMapper.deleteByPrimaryKey(id)<1,"删除角色失败");
    }

    //给角色授权
    public void addGrant(Integer roleId,Integer[] mIds){
        //判断角色是否存在
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role==null,"角色不存在");
        //判断需要绑定的权限是否传输过来
        AssertUtil.isTrue(mIds==null||mIds.length<1,"需要绑定的模块不存在");
        //判断当前角色原来是否有资源
        Integer count = permissionMapper.countPermission(roleId);
        if (count>0){
            //将原有的资源全部删除
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId)!=count,"数据异常请重试");
        }
        //给角色绑定权限
        List<Permission> permissions = new ArrayList<>();
        for (Integer mid:mIds){
            Permission permission = new Permission();

            permission.setRoleId(roleId);
            permission.setModuleId(mid);
            permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());//设置权限码
            permission.setCreateDate(new Date());
            permission.setUpdateDate(new Date());

            permissions.add(permission);

        }
        //执行批量添加操作,绑定多个权限
        AssertUtil.isTrue(permissionMapper.insertBatch(permissions)!=permissions.size(),"权限绑定失败");
    }
    /* public void addGrant(Integer roleId, Integer[] mIds) {
        //判断角色是否存在
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role == null,"角色不存在");
        //判断需要绑定的权限是否传输过来
        // AssertUtil.isTrue(mIds == null || mIds.length < 1,"需要绑定的模块不存在");
        //判断当前角色原来是否有资源
        Integer count = permissionMapper.countPermission(roleId);
        if(count > 0){
            //将原有的资源全部删除
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId) != count,"数据异常请重试");
        }
        //给角色绑定权限
        List<Permission> permissions = new ArrayList<>();
        for(Integer mid:mIds){
            Permission permission = new Permission();

            permission.setRoleId(roleId);
            permission.setModuleId(mid);
            permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());//设置权限码  需要去module表中查询得到
            permission.setCreateDate(new Date());
            permission.setUpdateDate(new Date());

            permissions.add(permission);
        }

        //执行批量添加操作，绑定多个权限
        AssertUtil.isTrue(permissionMapper.insertBatch(permissions) != permissions.size(),"权限绑定失败");

    }*/
}
