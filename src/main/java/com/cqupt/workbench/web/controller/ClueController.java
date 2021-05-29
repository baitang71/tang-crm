package com.cqupt.workbench.web.controller;

import com.cqupt.settings.domain.User;
import com.cqupt.settings.service.UserService;
import com.cqupt.utils.DateTimeUtil;
import com.cqupt.utils.UUIDUtil;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.domain.Clue;
import com.cqupt.workbench.service.ActivityService;
import com.cqupt.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Resource
    private ClueService clueService;
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    @RequestMapping("/getUsers.do")
    @ResponseBody
    public List<User> doGetUsers(){
        List<User> users=userService.selectAll();
        return users;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String,Object> doSave(Clue clue,HttpServletRequest request){
        Map<String,Object> data=new HashMap<>();
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setId(UUIDUtil.getUUID());
        boolean flag=clueService.save(clue);
        data.put("success",flag);
        return data;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public Map<String,Object> doPageList(Clue clue,String pageNo,String pageSize){
        Map<String,Object> data=new HashMap<>();
        /*System.out.println(clue);
        System.out.println(pageNo+"====="+pageSize);*/
        int no=Integer.parseInt(pageNo);
        int size=Integer.parseInt(pageSize);
        int skip=(no-1)*size;
        List<Clue> clueList=clueService.pageList(clue,skip,size);
        int total=clueService.allCount();
        data.put("clueList",clueList);
        data.put("total",total);
        return data;
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ModelAndView doDetail(String id){
        ModelAndView mv=clueService.getDetailById(id);
        mv.setViewName("clue/detail");
        return mv;
    }
    @RequestMapping("/getActivitiesByClueId.do")
    @ResponseBody
    public List<Activity> doGetActivitiesByClueId(String id){
        List<Activity> activities=clueService.getActivitiesByClueId(id);
        return activities;
    }

    @RequestMapping("/removeRelation.do")
    @ResponseBody
    public Map<String,Object> doRemove(String id){
        Map<String,Object> data =new HashMap<>();
        boolean flag=clueService.removeRelationById(id);
        data.put("success",flag);
        return data;
    }

    @RequestMapping("/selectActivities.do")
    @ResponseBody
    public List<Activity> doSelectActivities(String sname,String id){
        List<Activity> activities=clueService.selectActivities(sname,id);
        return activities;
    }

    @RequestMapping("/selectActivityByName.do")
    @ResponseBody
    public List<Activity> doSelectByName(String aname){
        List<Activity> activities=activityService.selectByName(aname);
        return activities;
    }


   /* @RequestMapping("/tran.do")
    @ResponseBody
    public List<Activity> doTran(String data){
        List<Activity> activities=activityService.selectByName(aname);
        return activities;
    }*/
}
