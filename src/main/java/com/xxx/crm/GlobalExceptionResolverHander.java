package com.xxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.exceptions.NoLoginException;
import com.xxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolverHander implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object hander, Exception e) {

        if (e instanceof NoLoginException){
            ModelAndView modelAndView = new ModelAndView("redirect:/index");
            return modelAndView;
        }



        //设置默认异常处理
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("");
        modelAndView.addObject("code",400);
        modelAndView.addObject("msg","系统异常,请重试");

        /*
        * * 如何判断⽅法的返回类型：
 * 如果⽅法级别配置了 @ResponseBody 注解，表示⽅法返回的是JSON；反之，返回的是视
图⻚⾯*/
        //返回视图
        if (hander instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) hander;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //返回的是视图
            if (responseBody==null){
                if (e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    modelAndView.addObject("code",pe.getCode());
                    modelAndView.addObject("msg",pe.getMsg());
                }

                return modelAndView;
            }else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常");
                if(e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    modelAndView.addObject("code",pe.getCode());
                    modelAndView.addObject("msg",pe.getMsg());
                }
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();

                    out.write(JSON.toJSONString(resultInfo));

                }catch (Exception ee){
                    ee.printStackTrace();

                }finally {
                    if (out!=null){
                        out.close();
                    }

                }
                return null;
            }

        }

        return modelAndView;
    }
}
