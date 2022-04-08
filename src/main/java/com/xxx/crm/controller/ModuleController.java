package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.model.TreeModel;
import com.xxx.crm.service.ModuleeService;
import com.xxx.crm.vo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Autowired
    private ModuleeService moduleeService;

    @RequestMapping("allModule")
    @ResponseBody
    public List<TreeModel> queryMod(Integer rId){
       return moduleeService.queryAllmodule(rId);
    }

    @RequestMapping("module")
    public String module(){
        return "module/module";
    }
    @RequestMapping("addPage")
    public String addPage(Integer grade, Integer parentId, HttpServletRequest request){
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }
    @RequestMapping("updatePage")
    public String updatePage(Integer id,HttpServletRequest request){
        Module module = moduleeService.selectByPrimaryKey(id);
        request.setAttribute("module",module);
        return "module/update";
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleeService.queryAllModules();
    }

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Module module){
        moduleeService.moduleAdd(module);
        return success("资源添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Module module){
        moduleeService.updateModule(module);
        return success("资源修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer mId){
        moduleeService.deleteModule(mId);
        return success("资源删除成功");
    }



}
