package com.cqupt.settings.web.controller;

import com.cqupt.settings.domain.User;
import com.cqupt.settings.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,Object> doLogin(String loginAct, String loginPwd, HttpServletRequest request){
        System.out.println("=====>"+loginAct);
        Map<String,Object> map= new HashMap<>();
        User user;
        try{
            String ip=request.getRemoteAddr();
            user=userService.login(loginAct,loginPwd,ip);
            HttpSession session=request.getSession();
            session.setAttribute("user",user);
            map.put("success",true);
        }catch (Exception e){
            String msg=e.getMessage();
            map.put("success",false);
            map.put("msg",msg);
        }
        return map;
    }

}
