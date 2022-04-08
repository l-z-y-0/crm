package com.xxx.crm.dao;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.vo.SaleChance;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {

    //分页查询所有
    public List<SaleChance> selectByParam(SaleChanceQuery saleChanceQuery);

    //查询所有经理的数据
    public List<Map<String,Object>> selectAllSales();




    SaleChance selectByPrimaryKey(Integer id);


}