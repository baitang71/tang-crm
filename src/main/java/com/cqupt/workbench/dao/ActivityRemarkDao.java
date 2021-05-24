package com.cqupt.workbench.dao;

import com.cqupt.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int selectCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> selectByAid(String id);

    int removeRemarkById(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);
}
