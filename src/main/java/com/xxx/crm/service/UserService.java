package com.xxx.crm.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.dao.UserMapper;
import com.xxx.crm.dao.UserRoleMapper;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.query.UserQuery;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.utils.Md5Util;
import com.xxx.crm.utils.PhoneUtil;
import com.xxx.crm.utils.UserIDBase64;
import com.xxx.crm.vo.User;
import com.xxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;



//测试登录
    public UserModel login(String userName,String userPwd){
        //1.验证参数
        checkParams(userName,userPwd);
        //2.根据用户名查询用户信息
        User user = userMapper.queryUserByName(userName);
        //3.判断用户是否存在
        AssertUtil.isTrue(user==null,"用户不存在");
        //4.校验密码
        checkPwd(userPwd,user.getUserPwd());
        //5.密码正确,返回用户相关信息
        return buildUser(user);
    }


    //修改密码
    public void updatePassword(Integer userID,String oldPassword,String newPassword,String confirmPassword){
        //通过用户ID获取对象
        AssertUtil.isTrue(userID==null,"用户id不能为空");
        User user = userMapper.selectByPrimaryKey(userID);
        //校验参数
        checkPasswordParm(user,oldPassword,newPassword,confirmPassword);
        //设置用户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //修改
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败");

    }

    private void checkPasswordParm(User user, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(user==null,"用户不存在");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"原始密码不得为空");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)),"原始密码不正确");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码不能和原始密码一致");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请确认密码");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"两次密码不一致");
    }

    private UserModel buildUser(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserId(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    public Map<String,Object> queryUser(UserQuery userQuery){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        List<User> list = userMapper.queryUser(userQuery);
        //格式化数据
        PageInfo pageInfo = new PageInfo(list);

        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        return map;

    }

    /**
     * 添加用户
     *  1.校验参数
     *      用户名  非空 | 唯一
     *      邮箱    非空
     *      手机号  非空 | 格式正确
     *  2.设置默认值
     *      is_valid
     *      update_date
     *      create_date
     *      user_password  设置用户默认密码 123456(加密MD5)
     *  3.执行添加操作
     *
     */

    public void insertUser(User user){
        checkPP(user.getUserName(),user.getEmail(),user.getPhone());
        AssertUtil.isTrue(userMapper.queryUserByName(user.getUserName())!=null,"用户名已存在");
        user.setIsValid(1);
        user.setUpdateDate(new Date());
        user.setCreateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"用户添加失败");

        //绑定角色给用户
        relationRole(user.getId(),user.getRoleIds());
    }

    /**
     * 修改用户
     *  1.校验参数
     *      id     非空|存在
     *      用户名  非空 | 唯一
     *      邮箱    非空
     *      手机号  非空 | 格式正确
     *  2.设置默认值
     *      update_date
     *  3.执行修改操作
     *
     */

    public void updateUser(User user){
        checkPP(user.getUserName(),user.getEmail(),user.getPhone());
        AssertUtil.isTrue(user.getId()==null||userMapper.selectByPrimaryKey(user.getId())==null,"用户id已被注册");
        AssertUtil.isTrue(user.getUserName()==null,"用户名不为空");
        //名称唯一
        User db = userMapper.queryUserByName(user.getUserName());
        AssertUtil.isTrue(db!=null&&db.getId()!=user.getId(),"不可修改");
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"数据修改失败");
        //修改角色
        relationRole(user.getId(),user.getRoleIds());

    }

    private void checkPP(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不得为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不得为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }

    public void deleteUser(Integer[] ids){
        AssertUtil.isTrue(ids==null||ids.length==0,"无用户信息");
        AssertUtil.isTrue(userMapper.deleteUser(ids)!=ids.length,"删除用户失败");
    }

    public void relationRole(Integer userId,String roleIds){
        //修改角色操作,查询是否有角色,如果有直接删除再绑定
        Integer count = userRoleMapper.countUserRole(userId);
        System.out.println(count);
        System.out.println("id"+userId);
        if(count > 0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRole(userId) != count,"原有角色删除失败");
        }
        AssertUtil.isTrue(roleIds==null,"角色不存在");
        //准备一个容器接受遍历出来的新对象/新数据
        List<UserRole> list = new ArrayList<>();
        //切割获得到的每个id
        String[] ss = roleIds.split(",");
        for (String s:ss){
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.parseInt(s));
            userRole.setUpdateDate(new Date());
            userRole.setCreateDate(new Date());
            //将数据添加到集合中
            list.add(userRole);
        }

        AssertUtil.isTrue(userRoleMapper.insertBatch(list) !=ss.length,"角色绑定失败");

    }
    /*    private void relationUserRole(Integer id, String roleIds) {
        //修改角色操作：查询是否原来就有角色，如果有那么直接删除再绑定新角色
        Integer count = userRoleMapper.countUserRole(id);
        if(count > 0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUid(id) != count,"原有角色删除失败");
        }
        AssertUtil.isTrue(roleIds == null,"角色不存在");
        //准备一个容器接收遍历出来的新对象/新数据
        List<UserRole> urs = new ArrayList<>();
        //切割获取到每个id
        String[] splits = roleIds.split(",");
        for(String idStr:splits){
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(Integer.parseInt(idStr));
            userRole.setUpdateDate(new Date());
            userRole.setCreateDate(new Date());

            //将数据添加到集合中
            urs.add(userRole);
        }
        //执行批量添加操作
        AssertUtil.isTrue(userRoleMapper.insertBatch(urs) != splits.length,"角色绑定失败");

    }*/


    private void checkPwd(String upwd, String userPwd) {
        //将前台输入的密码加密后与数据库中的进行校验
        upwd = Md5Util.encode(upwd);
        AssertUtil.isTrue(!upwd.equals(userPwd),"密码不正确");
    }

    private void checkParams(String userName, String userPwd) {
        //1.判断姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //判断密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");

    }







}
