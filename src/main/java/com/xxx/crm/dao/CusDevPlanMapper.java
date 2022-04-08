package com.xxx.crm.dao;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.query.CusDevPlanQuery;
import com.xxx.crm.vo.CusDevPlan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CusDevPlanMapper extends BaseMapper<CusDevPlan,Integer> {
   public List<CusDevPlan> queryCusQuery(CusDevPlanQuery cusDevPlanQuery);
}