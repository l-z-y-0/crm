package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.dao.SaleChanceMapper;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.utils.PhoneUtil;
import com.xxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Autowired
    private SaleChanceMapper saleChanceMapper;


    public Map<String,Object> queryByParam(SaleChanceQuery query){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(query.getPage(),query.getLimit());
        //按照分页查询
        List<SaleChance> list = saleChanceMapper.selectByParam(query);
        //格式化数据
        PageInfo<SaleChance> pageInfo = new PageInfo<>(list);

        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 添加数据
     *      1.校验参数
     *          customerName   客户名称 非空
     *          linkMan       联系人   非空
     *          linkPhone      手机号码 非空  手机号11位正则校验
     *      2.设置默认值
     *          is_valid     数据有效   0无效 1有效
     *          create_date  数据创建时间
     *          update_date  数据修改时间
     *          create_man   数据的创建人  当前登录用户（交给controller层从cookie获取）直接设置到 salechance对象中
     *
     *          判断用户是否设置了分配人
     *              如果分配了
     *                  assign_man   分配人
     *                  assign_time  分配时间
     *                  state        已分配 分配状态  0未分配 1已分配
     *                  dev_result   开发中 开发状态  0-未开发 1-开发中 2-开发成功 3-开发失败
     *              如果未分配
     *                  state        未分配 分配状态  0未分配 1已分配
     *                  dev_result   未开发 开发状态  0-未开发 1-开发中 2-开发成功 3-开发失败
     *       3.执行添加操作，判断是否添加成功
     * @return
     */


    public void addSaleChance(SaleChance saleChance){
        //参数校验
        checkParm(saleChance.getCreateMan(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置默认值
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());

        if (StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
        }else {
            saleChance.setAssignTime(new Date());
            saleChance.setState(1);
            saleChance.setDevResult(1);
        }

        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"添加失败");

    }
    /**
     * 修改数据
     *      1.校验参数
     *          id属性是必须存在的，查询数据库校验
     *          customerName   客户名称 非空
     *          linkMan       联系人   非空
     *          linkPhone      手机号码 非空  手机号11位正则校验
     *      2.默认值
     *          update_date  修改时间
     *
     *          判断是否指派了工作人员
     *              1.修改前没有分配人
     *                  修改后没有分配人
     *                      不做任何操作
     *                  修改后有分配人
     *                      dev_result  开发状态
     *                      assign_time 分配时间
     *                      state       分配状态
     *
     *              2.修改前有分配人
     *                  修改后没有分配人
     *                      assign_time 分配时间 null
     *                      dev_result  开发状态
     *                      state       分配状态 0
     *                  修改后有分配人
     *                      判断更改后的人员和更改前的人员有没有变动
     *                          没有变动不做操作
     *                          有变动，assign_time最新的时间
     *     3.执行修改操作，判断是否修改成功
     *
     * @param
     */


    public void update(SaleChance saleChance){
        AssertUtil.isTrue(saleChance.getId()==null,"用户名不得为空");
        checkParm(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置修改时间
        saleChance.setUpdateDate(new Date());
        //通过现有id查询修改之前的信息
        SaleChance dbchance = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(dbchance==null,"用户数据不存在");

        //判断修改之前是否有分配人
        if (StringUtils.isBlank(dbchance.getAssignMan())){
            //修改后没有分配人不做任何事

            //修改后有分配人
            if (!StringUtils.isBlank(saleChance.getAssignMan())){
                saleChance.setDevResult(1);
                saleChance.setAssignTime(new Date());
                saleChance.setState(1);
            }

            //修改之后没有分配人,不做任何事
        }else {
            //修改之前有分配人

            //修改后无分配人
            if (StringUtils.isBlank(saleChance.getAssignMan())){
                saleChance.setAssignTime(null);
                saleChance.setDevResult(0);
                saleChance.setState(0);
                //修改后有分配人
            }else {
                if (dbchance.getAssignMan().equals(saleChance.getAssignMan())){
                    saleChance.setAssignTime(new Date());
                }else {
                    saleChance.setAssignTime(new Date());
                }
            }


        }

        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"修改失败");

    }

    //查询所有客户经理

    public List<Map<String,Object>> querySales(){
        return saleChanceMapper.selectAllSales();
    }



    private void checkParm(String customerName , String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"分配人不得为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不得为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"用户人手机号不得为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机不符合规范");
    }


}
