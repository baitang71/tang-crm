package com.cqupt.workbench.web.controller;


import com.cqupt.settings.domain.User;
import com.cqupt.settings.service.UserService;
import com.cqupt.vo.PageListVo;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    @RequestMapping("/selectAll.do")
    @ResponseBody
    public List<User> doSelectAll(){
        return userService.selectAll();
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String ,Object> doSave(Activity activity, HttpServletRequest request){
        String createName=((User)request.getSession().getAttribute("user")).getName();
        activity.setCreateBy(createName);
        Boolean flag=activityService.save(activity);
        Map<String,Object> data=new HashMap<>();
        data.put("success",flag);
        return data;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public PageListVo<Activity> doPageList(@RequestParam("pageNo") String pageNoStr,
                                          @RequestParam("pageSize") String pageSizeStr,
                                           @RequestParam("name") String name,
                                           @RequestParam("owner") String owner,
                                           @RequestParam("startDate") String startDate,
                                           @RequestParam("endDate") String endDate
                                          ){

        int pageNo=Integer.valueOf(pageNoStr);
        int pageSize=Integer.valueOf(pageSizeStr);
        int skipNum=(pageNo-1)*pageSize;
        Map<String,Object> data=new HashMap<>();
        data.put("skipNum",skipNum);
        data.put("pageSize",pageSize);
        data.put("name",name);
        data.put("owner",owner);
        data.put("startDate",startDate);
        data.put("endDate",endDate);
        PageListVo<Activity> reData=activityService.pageList(data);
        return reData;
    }
}