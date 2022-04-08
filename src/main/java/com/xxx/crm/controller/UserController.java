package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.query.UserQuery;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import com.xxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("checkLogin")
    @ResponseBody
    public ResultInfo checkLogin(String userName,String userPwd){
            ResultInfo resultInfo = new ResultInfo();
            UserModel userModel = userService.login(userName, userPwd);
            resultInfo.setResult(userModel);
            return resultInfo;
    }


    @PostMapping("update")
    @ResponseBody
    public ResultInfo updatePassword(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePassword(id,oldPassword,newPassword,confirmPassword);
        return resultInfo;
    }


    @RequestMapping("updatePassword")
    public String updatePassword(){
        return "user/password";
    }

    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> queryUser(UserQuery userQuery){
        return userService.queryUser(userQuery);
    }

    @RequestMapping("quser")
    public String getUser(){
        return "user/user";
    }

    @PostMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.insertUser(user);
        return success("用户添加成功");
    }

    @RequestMapping("add_update")
    public String addAndUpdate(Integer id,HttpServletRequest request){
        User user = userService.selectByPrimaryKey(id);
        request.setAttribute("user",user);
        return "user/add_update";
    }

    @PostMapping("updateUser")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("修改成功");
    }

    @PostMapping("deleteUser")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success("删除用户成功");
    }

}
