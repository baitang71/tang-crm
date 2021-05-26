package com.cqupt.workbench.web.controller;

import com.cqupt.settings.domain.User;
import com.cqupt.settings.service.UserService;
import com.cqupt.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Resource
    private ClueService clueService;
    @Resource
    private UserService userService;

    @RequestMapping("/getUsers.do")
    @ResponseBody
    public List<User> doGetUsers(){
        List<User> users=userService.selectAll();
        return users;
    }
}
