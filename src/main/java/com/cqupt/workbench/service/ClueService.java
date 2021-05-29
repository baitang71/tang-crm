package com.cqupt.workbench.service;

import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.domain.Clue;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ClueService {
    boolean save(Clue clue);

    List<Clue> pageList(Clue clue,int skip, int size);

    int allCount();

    ModelAndView getDetailById(String id);

    List<Activity> getActivitiesByClueId(String id);

    boolean removeRelationById(String id);

    List<Activity> selectActivities(String sname, String id);

    boolean makeRelation(String[] activityIds, String clueId);
}
