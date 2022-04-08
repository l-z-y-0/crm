package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.dao.CusDevPlanMapper;
import com.xxx.crm.query.CusDevPlanQuery;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.vo.CusDevPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Autowired
    private CusDevPlanMapper cusDevPlanMapper;

    public Map<String,Object> queryCus(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap();
        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        List<CusDevPlan> list = cusDevPlanMapper.queryCusQuery(cusDevPlanQuery);
        //格式化数据
        PageInfo pageInfo = new PageInfo(list);

        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }


    /**
     * 添加数据
     *      1.数据校验
     *          saleChanceId   营销机会的id  非空 / 数据存在
     *          计划内容         非空
     *          计划时间         非空
     *      2.设置默认值
     *          is_valid     数据是否有效
     *          create_date
     *          update_date
     *      3.执行添加操作，判断
     */
    public void addPlan(CusDevPlan cusDevPlan){
        //数据校验
        checkParam(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //设置默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        //执行操作
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)<1,"数据添加不成功");


    }

    /**
     * 修改数据
     *      1.数据校验
     *          计划项 id       非空/数据存在
     *          saleChanceId   营销机会的id  非空 / 数据存在
     *          计划内容         非空
     *          计划时间         非空
     *      2.设置默认值
     *          update_date
     *      3.执行添加操作，判断
     */

    public void updateCus(CusDevPlan cusDevPlan){
        //校验参数
        AssertUtil.isTrue(cusDevPlan.getId()==null,"客户数据不存在");
        checkParam(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        //设置默认值
        cusDevPlan.setUpdateDate(new Date());
        //执行操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"修改数据不成功");


    }

    public void delete(Integer id){
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(id==null||cusDevPlan==null,"删除数据不得为空");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"删除失败");
    }





    private void checkParam(Integer saleChanceId, String planItem, Date planDate) {
        AssertUtil.isTrue(saleChanceId==null ||cusDevPlanMapper.selectByPrimaryKey(saleChanceId)==null,"营销机会id不得为空");
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"计划内容不得空");
        AssertUtil.isTrue(planDate==null,"计划时间不得为空");
    }



}
