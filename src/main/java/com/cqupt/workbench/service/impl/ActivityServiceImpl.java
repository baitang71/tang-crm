package com.cqupt.workbench.service.impl;

import com.cqupt.utils.DateTimeUtil;
import com.cqupt.utils.UUIDUtil;
import com.cqupt.vo.PageListVo;
import com.cqupt.workbench.dao.ActivityDao;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;

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
}
