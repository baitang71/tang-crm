package com.cqupt.workbench.service.impl;

import com.cqupt.utils.DateTimeUtil;
import com.cqupt.utils.UUIDUtil;
import com.cqupt.vo.PageListVo;
import com.cqupt.workbench.dao.ActivityDao;
import com.cqupt.workbench.dao.ActivityRemarkDao;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.exception.ActivityDeleteException;
import com.cqupt.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
}
