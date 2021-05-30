package com.cqupt.workbench.dao;


import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {


    List<Activity> selectActivities(String id);

    int deleteById(String id);

    int makeRelation(ClueActivityRelation relation);

    List<ClueActivityRelation> selectByClueId(String clueId);
}
