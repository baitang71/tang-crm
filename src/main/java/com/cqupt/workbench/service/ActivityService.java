package com.cqupt.workbench.service;

import com.cqupt.vo.PageListVo;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.domain.ActivityRemark;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PageListVo<Activity> pageList(Map<String, Object> data);

    void deleteByIds(String[] ids);

    Activity selectById(String id);

    Boolean update(Activity activity);

    ModelAndView detailById(String id);

    List<ActivityRemark> getRemarkListByAid(String id);
}
