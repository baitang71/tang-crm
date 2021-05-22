package com.cqupt.workbench.service;

import com.cqupt.vo.PageListVo;
import com.cqupt.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PageListVo<Activity> pageList(Map<String, Object> data);
}
