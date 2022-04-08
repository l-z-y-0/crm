package com.xxx.crm.interceptors;

import com.xxx.crm.exceptions.NoLoginException;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NologinInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        if (id==null||userService.selectByPrimaryKey(id)==null){
            throw  new NoLoginException();
        }
        return true;
    }
}
