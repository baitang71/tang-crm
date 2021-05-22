package com.cqupt.workbench.dao;

import com.cqupt.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> pageList(Map<String, Object> data);

    int pageListCount(Map<String, Object> data);
}
