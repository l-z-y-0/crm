package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.query.CusDevPlanQuery;
import com.xxx.crm.service.CusDevPlanService;
import com.xxx.crm.service.SaleChanceService;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.vo.CusDevPlan;
import com.xxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("cus")
    public String cus(){
        return "cusDevPlan/cus_dev_plan";
    }


    @RequestMapping("plan_data")
    public String planDta(Integer sId,HttpServletRequest request){
        AssertUtil.isTrue(sId==null,"Sid数据异常");
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sId);
        if (saleChance!=null){
            request.setAttribute("saleChance",saleChance);
        }

        return "cusDevPlan/cus_dev_plan_data";
    }

    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> queryAll(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCus(cusDevPlanQuery);
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo add(CusDevPlan cusDevPlan){
        cusDevPlanService.addPlan(cusDevPlan);
        return success("数据添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCus(cusDevPlan);
        return success("数据修改成功");
    }

    @RequestMapping("addAndUpdate")
    public String addAndUpdate(Integer id,Integer sId,HttpServletRequest request){
        if (id!=null){
            //修改操作
            System.out.println(id);
            CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
            AssertUtil.isTrue(cusDevPlan==null,"修改数据为空");
            request.setAttribute("cusDevPlan",cusDevPlan);
        }
        request.setAttribute("sId",sId);

        return "cusDevPlan/add_update";

    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCus(Integer id){
        cusDevPlanService.delete(id);
        return success("删除成功");
    }

}
