package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.service.PermissionService;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import com.xxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

/*系统登录界面*/
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    /*欢迎界面*/
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /*后台管理主页面*/
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //通过工具获得cookieid
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        System.out.println(id);
        //调用service层,通过id查询用户
        User user = userService.selectByPrimaryKey(id);
        System.out.println(user);
        //将查询过来的数据设置到作用域中
        request.setAttribute("user",user);
        //当用户登录后进去主页面之前将当前用户具备所有的权限查询出来,放在session作用域中
        List<Integer> permission = permissionService.selectAclvalueByUserId(id);
        request.getSession().setAttribute("permission",permission);//权限码
        System.out.println(permission.toString());
        return "main";

    }

    /*    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //获取id
        int id = LoginUserUtil.releaseUserIdFromCookie(request);
        //tonggid查询
        User user = userService.selectByPrimaryKey(id);
        request.setAttribute("user",user);

        //当用户登录后进去主页面之前将当前用户具备所有的权限码查询出来，放在session作用域中
        List<Integer> permission =  permissionService.selectAclvalueByUserId(id);
        request.getSession().setAttribute("permission",permission);//权限码
        System.out.println(permission.toString());
        return "main";
    }*/
}
