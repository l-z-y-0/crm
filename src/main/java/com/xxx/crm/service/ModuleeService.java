package com.xxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.util.StringUtil;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.dao.ModuleMapper;
import com.xxx.crm.dao.PermissionMapper;
import com.xxx.crm.dao.RoleMapper;
import com.xxx.crm.model.TreeModel;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleeService extends BaseService<Module,Integer>{
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public List<TreeModel> queryAllmodule(Integer rId){
        //角色非空且存在
        AssertUtil.isTrue(rId == null || roleMapper.selectByPrimaryKey(rId) == null,"角色不存在");
        //查询当前角色拥有的权限
        List<Integer> mIds = permissionMapper.selectPermissionByRid(rId);
        //查询所有的模块
        List<TreeModel> list = moduleMapper.queryAllModel();
        //遍历需要返回前台的所有资源
        for (TreeModel treeModel : list){
            //获取当前对象的模块id
            Integer id = treeModel.getId();
            if (mIds.contains(id)){
                treeModel.setChecked(true);
                treeModel.setOpen(true);

            }
        }
        return list;
    }


    /*  //查询所有资源
    public List<TreeModel> queryAllModules(Integer rId){
        //角色非空且存在
        AssertUtil.isTrue(rId == null || roleMapper.selectByPrimaryKey(rId) == null,"角色不存在");
        //查询当前角色拥有的权限
        List<Integer> mIds = permissionMapper.selectPermissionByRid(rId);
        //查询所有的模块
        List<TreeModel> treeModels = moduleMapper.queryAllModules();
        //遍历需要返回到前台的所有资源
        for(TreeModel treeModel:treeModels){
            //获取当前遍历对象的模块id
            Integer id = treeModel.getId();
            //判断当前角色拥有的权限中是否包含了 遍历对象的模块id
            if(mIds.contains(id)){  //当前方法判断某个数据是否存在于这个集合中
                treeModel.setChecked(true);
                treeModel.setOpen(true);
            }
        }
        return treeModels;
    }*/

    //查找所有模块资源
    public Map<String,Object> queryAllModules(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Module> modules = moduleMapper.queryAllModules();
        result.put("count",modules.size());
        result.put("data",modules);
        result.put("code",0);
        result.put("msg","");
        return result;
    }

    /**
     * 模块添加
     *   1.数据校验
     模块名称
     非空，同级唯一
     地址 URL
     二级菜单：非空，同级唯一
     父级菜单 parentId
     一级：null | -1
     二级|三级：非空 | 必须存在
     层级 grade
     非空  值必须为 0|1|2
     权限码
     非空  唯一
     2.默认值
     is_valid
     updateDate
     createDate
     3.执行添加操作  判断受影响行数
     *
     * @param module
     */

    //模块添加
    public void moduleAdd(Module module){
        //层级grade 非空 值必须为 0|1|2
        AssertUtil.isTrue(module.getGrade()==null,"层级不能为空");
        AssertUtil.isTrue(!(module.getGrade()==0||module.getGrade()==1||module.getGrade()==2),"层级格式有误");
        //模块名称非空 同级唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        Module dbmodule = moduleMapper.queryModulByGradeAName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(dbmodule!=null,"模块名称已存在");
        //二级菜单URL:非空,同级唯一
        if (module.getGrade()==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"模块地址不能为空");
            dbmodule = moduleMapper.queryModulByGradeAUrl(module.getGrade(),module.getUrl());
            AssertUtil.isTrue(dbmodule!=null,"地址已存在,请重新输入");
        }
        //父级菜单 二级|三级:非空|必须存在
        if (module.getGrade()==1||module.getGrade()==2){
            AssertUtil.isTrue(module.getParentId()==null,"父Id不能为空");
            dbmodule = moduleMapper.queryModulById(module.getParentId());
            AssertUtil.isTrue(dbmodule==null,"父Id不存在");
        }
        //权限码 非空 唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        dbmodule = moduleMapper.queryModulByOptValue(module.getOptValue());
        AssertUtil.isTrue(dbmodule!=null,"权限码已存在");

        //默认值
        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());

        //执行添加操作 判断受影响行数

        AssertUtil.isTrue(moduleMapper.insertSelective(module)<1,"模块添加失败");
    }

    /**
     * 修改模块
     1.数据校验
     id
     非空，并且资源存在
     模块名称
     非空，同级唯一
     地址 URL
     二级菜单：非空，同级唯一
     父级菜单 parentId
     一级：null | -1
     二级|三级：非空 | 必须存在
     层级 grade
     非空  值必须为 0|1|2
     权限码
     非空  唯一
     2.默认值
     is_valid
     updateDate
     createDate
     3.执行修改操作  判断受影响行数
     * @param module
     */

    //修改模块
    public void updateModule(Module module){
        //id非空,并且资源存在
        AssertUtil.isTrue(module.getId()==null,"待删除的资源不存在");
        Module dbmodule = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(dbmodule==null,"id系统异常");

        //层级grade 非空 值必须为 0|1|2
        AssertUtil.isTrue(module.getGrade()==null,"层级不能为空");
        AssertUtil.isTrue(!(module.getGrade()==0||module.getGrade()==1||module.getGrade()==2),"层级有误");

        //模块名称非空 同级唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        dbmodule = moduleMapper.queryModulByGradeAName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(dbmodule!=null&&!(module.getId()).equals(dbmodule.getId()),"模块名称已经存在");

        //二级菜单URl:非空同级唯一
        if (module.getGrade()==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"模块地址不能为空");
            dbmodule = moduleMapper.queryModulByGradeAUrl(module.getGrade(),module.getUrl());
            AssertUtil.isTrue(dbmodule!=null&&(module.getId().equals(dbmodule.getId())),"地址已存在");
        }

        //父级菜单 二级|三级:非空|必须存在
        if (module.getGrade()==1||module.getGrade()==2){
            AssertUtil.isTrue(module.getParentId()==null,"父id不能为空");
            dbmodule = moduleMapper.queryModulById(module.getParentId());
            AssertUtil.isTrue(dbmodule==null&&!(module.getId()).equals(dbmodule.getId()),"父Id不存在");
        }

        //权限码 非空 唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        dbmodule = moduleMapper.queryModulByOptValue(module.getOptValue());
        AssertUtil.isTrue(dbmodule!=null&&!(module.getId().equals(dbmodule.getId())),"权限码已经存在");

        //默认值
        module.setUpdateDate(new Date());

        //执行修改操作
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module)<1,"修改失败");

    }

    /**
     * 逻辑删除资源
     *      逻辑判断
     *          1.参数id 非空，删除的数据必须存在
     *          2.查询当前id下是否有子模块，如果有不能删除
     *          3.查询权限表中(角色和资源)是否包含当前模块的数据，有则删除
     *          4.删除资源
     * @param mId
     */

    public void deleteModule(Integer mId){
        //参数id 非空 ,删除的数据必须存在
        AssertUtil.isTrue(mId == null,"mid系统异常");
        AssertUtil.isTrue(selectByPrimaryKey(mId)==null,"删除数据不得为空");

        //查询当前id下是否有子模块,如果有不能删除
        Integer count = moduleMapper.queryCountModuleByParentId(mId);
        AssertUtil.isTrue(count>0,"该模块存在子模块,无法删除");

        //查询权限表(角色和资源)是否包含当前模块的数据,有则删除
        count = permissionMapper.queryCountByMoudleId(mId);
        if (count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByMoudleId(mId)!=count,"权限删除失败");
        }

        //删除资源
        AssertUtil.isTrue(moduleMapper.deleteModuleByMid(mId)<1,"资源删除失败");
    }



}
