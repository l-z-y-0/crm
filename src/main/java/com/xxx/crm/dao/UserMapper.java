package com.xxx.crm.dao;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.query.UserQuery;
import com.xxx.crm.vo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User,Integer> {


    User selectByPrimaryKey(Integer id);

    //删除
    public Integer deleteUser(Integer[] ids);

    User queryUserByName(String name);

    //分页
    public List<User> queryUser(UserQuery userQuery);
}