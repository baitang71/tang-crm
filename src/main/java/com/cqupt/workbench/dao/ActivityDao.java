package com.cqupt.workbench.dao;

import com.cqupt.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> pageList(Map<String, Object> data);

    int pageListCount(Map<String, Object> data);

    int deleteByIds(String[] ids);

    Activity selectById(String id);

    int update(Activity activity);

    Activity detailById(String id);

    List<Activity> unRelate(@Param("sname") String sname,@Param("id") String id);

    List<Activity> selectByName(String aname);
}
