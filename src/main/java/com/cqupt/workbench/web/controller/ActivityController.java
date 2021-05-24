package com.cqupt.workbench.web.controller;


import com.cqupt.settings.domain.User;
import com.cqupt.settings.service.UserService;
import com.cqupt.vo.PageListVo;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.domain.ActivityRemark;
import com.cqupt.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/delete.do")
    @ResponseBody
    public Map<String,Object> doDelete(HttpServletRequest request){
        String[] ids=request.getParameterValues("id");
        //System.out.println(ids);
        Map<String,Object> data=new HashMap<>();
        try{
            activityService.deleteByIds(ids);
            data.put("success",true);
        }catch (Exception e){
            System.out.println("======>"+e.getMessage());
            data.put("success",false);
        }
        return data;
    }

    @RequestMapping("/getListAndActivity.do")
    @ResponseBody
    public Map<String,Object> doGet(String id){
        List<User> users=userService.selectAll();
        Activity activity=activityService.selectById(id);
        Map<String,Object> data=new HashMap<>();
        if (users!=null&&activity!=null){
            data.put("success",true);
            data.put("uList",users);
            data.put("activity",activity);
        }else{
            data.put("success",false);
        }
        return data;
    }

    @RequestMapping("/updateActivity.do")
    @ResponseBody
    public Map<String,Object> doGet(Activity activity,HttpServletRequest request){
        String editName=((User)request.getSession().getAttribute("user")).getName();
        activity.setEditBy(editName);
        Boolean flag=activityService.update(activity);
        Map<String,Object> data=new HashMap<>();
        data.put("success",flag);
        return data;
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ModelAndView doDetail(String id){
        ModelAndView mv=activityService.detailById(id);
        mv.setViewName("detail");
        return mv;
    }

    @RequestMapping("/getRemarkList.do")
    @ResponseBody
    public List<ActivityRemark> doRemarkList(String id){
        List<ActivityRemark> remarkList=activityService.getRemarkListByAid(id);
        System.out.println(remarkList);
        return remarkList;
    }

    @RequestMapping("/removeRemarkById.do")
    @ResponseBody
    public Map<String,Object> doRemoveById(String id){
        Map<String,Object> data=new HashMap<>();
        boolean flag=activityService.removeRemarkById(id);
        data.put("success",flag);
        return data;
    }

    @RequestMapping("/saveActivityRemark.do")
    @ResponseBody
    public Map<String,Object> doSaveRemark(@RequestParam("activityId") String aid,String noteContent,HttpServletRequest request){
        String createName=((User)request.getSession().getAttribute("user")).getName();
        Map<String,Object> data=activityService.saveRemark(aid,noteContent,createName);
        return data;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    public Map<String,Object> doUpdateRemark(String id,String noteContent,HttpServletRequest request){
        String editName=((User)request.getSession().getAttribute("user")).getName();
        Map<String,Object> data=activityService.updateRemark(id,noteContent,editName);
        return data;
    }
}
