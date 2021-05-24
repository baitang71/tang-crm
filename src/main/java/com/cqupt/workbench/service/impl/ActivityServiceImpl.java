package com.cqupt.workbench.service.impl;

import com.cqupt.utils.DateTimeUtil;
import com.cqupt.utils.UUIDUtil;
import com.cqupt.vo.PageListVo;
import com.cqupt.workbench.dao.ActivityDao;
import com.cqupt.workbench.dao.ActivityRemarkDao;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.domain.ActivityRemark;
import com.cqupt.workbench.exception.ActivityDeleteException;
import com.cqupt.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ActivityRemarkDao activityRemarkDao;

    @Override
    public boolean save(Activity activity) {
        boolean flag=true;
        String id= UUIDUtil.getUUID();
        String createTime= DateTimeUtil.getSysTime();
        activity.setId(id);
        activity.setCreateTime(createTime);
        int n=activityDao.save(activity);
        if (n!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public PageListVo<Activity> pageList(Map<String, Object> data) {
        PageListVo<Activity> activityVo=new PageListVo<>();
        int total=activityDao.pageListCount(data);
        List<Activity> activityList=activityDao.pageList(data);
        activityVo.setTotal(total);
        activityVo.setList(activityList);
        return activityVo;
    }

    @Transactional(
            rollbackFor = ActivityDeleteException.class
    )
    @Override
    public void deleteByIds(String[] ids) {
        int remarkCount=activityRemarkDao.selectCountByAids(ids);
        int dRemarkCount=activityRemarkDao.deleteByAids(ids);
        int dActivityCount=activityDao.deleteByIds(ids);
        if(remarkCount!=dRemarkCount){
            throw new ActivityDeleteException("删除与市场活动相关的备注失败");
        }
        if(dActivityCount!=ids.length){
            throw new ActivityDeleteException("删除市场活动失败");
        }
    }

    @Override
    public Activity selectById(String id) {
        Activity activity=activityDao.selectById(id);
        return activity;
    }

    @Override
    public Boolean update(Activity activity) {
        boolean flag=true;
        String editTime= DateTimeUtil.getSysTime();
        activity.setEditTime(editTime);
        int n=activityDao.update(activity);
        if (n!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public ModelAndView detailById(String id) {
        ModelAndView mv=new ModelAndView();
        Activity activity=activityDao.detailById(id);
        mv.addObject("activity",activity);
        return mv;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String id) {
        List<ActivityRemark> remarkList=activityRemarkDao.selectByAid(id);
        return remarkList;
    }

    @Override
    public boolean removeRemarkById(String id) {
        boolean flag=true;
        int n=activityRemarkDao.removeRemarkById(id);
        if (n!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String,Object> saveRemark(@RequestParam("activityId") String aid, String noteContent, String createBy) {
        Map<String,Object> data=new HashMap<>();
        ActivityRemark activityRemark=new ActivityRemark();
        activityRemark.setActivityId(aid);
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");
        int n=activityRemarkDao.saveRemark(activityRemark);
        if (n==1){
            data.put("activityRemark",activityRemark);
            data.put("success",true);
        }else{
            data.put("success",false);
        }
        return data;
    }

}
