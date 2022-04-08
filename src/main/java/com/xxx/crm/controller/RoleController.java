package com.xxx.crm.controller;


import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.query.RoleQuery;
import com.xxx.crm.service.RoleService;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("roleAll")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer id){
        return roleService.queryRole(id);
    }

    @RequestMapping("role")
    public String role(){
        return "role/role";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queyList(RoleQuery roleQuery){
        return roleService.selectByTable(roleQuery);
    }

    @PostMapping("save")
    @ResponseBody
    public ResultInfo insertRole(Role role){
        roleService.saveRole(role);
        return success("添加角色成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.updateRole(role);
        return success("修改成功");
    }

    @RequestMapping("addAndUpdate")
    public String addRole(Integer id,HttpServletRequest request){
        if (id!=null){
           Role role = roleService.selectByPrimaryKey(id);
           request.setAttribute("role",role);
        }
        return "role/add_update";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("删除角色成功");
    }

    /*  @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer rId, HttpServletRequest request){
        AssertUtil.isTrue(rId == null,"角色不存在");
        request.setAttribute("roleId",rId);
        return "role/grant";
    }*/

    @RequestMapping("addGrant")
    public String addGrant(Integer rId,HttpServletRequest request){
        AssertUtil.isTrue(rId==null,"角色不存在");
        request.setAttribute("roleId",rId);
        return "role/grant";
    }

    /*    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return success();
    }*/

    @PostMapping("addRoleGrant")
    @ResponseBody
    public ResultInfo addRoleGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return success("授权成功");
    }
}
