package com.xxx.crm.controller;

import com.xxx.crm.annotation.RequirePermission;
import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.service.SaleChanceService;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.utils.CookieUtil;
import com.xxx.crm.utils.LoginUserUtil;
import com.xxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController{
    @Autowired
    private SaleChanceService saleChanceService;

  /*  @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = 101001)
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){
        if(flag !=null && flag == 1){
            //获取cookie中的id
            int id = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(id);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    */


    @GetMapping("list")
    @ResponseBody
    /*@RequirePermission(code = 101001)*/
    public Map<String,Object> queryParams(SaleChanceQuery query,Integer flg,HttpServletRequest request){
        if (flg!=null&&flg==1){
            // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人参数
            Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(id);
        }
        return saleChanceService.queryByParam(query);
    }

    @PostMapping("save")
    @ResponseBody
    public ResultInfo saveSal(HttpServletRequest request, SaleChance saleChance){
        String assignName = CookieUtil.getCookieValue(request,"userName");
        saleChance.setCreateMan(assignName);
        saleChanceService.addSaleChance(saleChance);
        return success();
    }

    @RequestMapping("sale")
    public String sale(){
        return "saleChance/sale_chance";
    }

    @RequestMapping("ad_up")
    public String addAndUpdate(Integer id,HttpServletRequest request){
        if (id!=null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            System.out.println(saleChance);
            System.out.println(saleChance.getAssignMan()+"////");
            System.out.println(saleChance.getId());
            AssertUtil.isTrue(saleChance==null,"用户异常");
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSale(SaleChance saleChance){
        saleChanceService.update(saleChance);
        return success();
    }


    @PostMapping("allsales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return saleChanceService.querySales();
    }


}
