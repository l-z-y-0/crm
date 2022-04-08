package com.xxx.crm.dao;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.model.TreeModel;
import com.xxx.crm.vo.Module;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleMapper extends BaseMapper<Module,Integer> {

    //查询所有的模块资源封装(TreeModel)
    public List<TreeModel> queryAllModel();

    //查询所有的模块资源
    public List<Module> queryAllModules();

    //同级下名称唯一
    Module queryModulByGradeAName(@Param("grade") Integer grade,@Param("moduleName") String moduleName);

    Module queryModulByGradeAUrl(@Param("grade") Integer grade,@Param("url") String url);

    Module queryModulById(Integer parentId);

    Module queryModulByOptValue(String optValue);

    //查询某个模块下是否存在子模块
    Integer queryCountModuleByParentId(Integer mId);

    //根据模块id 删除对应模块
    Integer deleteModuleByMid(Integer mId);


}