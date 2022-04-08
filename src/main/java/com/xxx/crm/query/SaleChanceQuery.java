package com.xxx.crm.query;

import com.xxx.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
    private String customerName;
    private String createMan;
    private String state;

    private Integer devResult; //开发状态
    private Integer assignMan; //分配人

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public SaleChanceQuery() {
    }

    public SaleChanceQuery(String custorMan, String createMan, String state) {
        this.customerName = custorMan;
        this.createMan = createMan;
        this.state = state;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
