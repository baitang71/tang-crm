package com.cqupt.workbench.service.impl;

import com.cqupt.utils.UUIDUtil;
import com.cqupt.workbench.dao.ActivityDao;
import com.cqupt.workbench.dao.ClueActivityRelationDao;
import com.cqupt.workbench.dao.ClueDao;
import com.cqupt.workbench.domain.Activity;
import com.cqupt.workbench.domain.Clue;
import com.cqupt.workbench.domain.ClueActivityRelation;
import com.cqupt.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ActivityDao activityDao;

    @Override
    public boolean save(Clue clue) {
        boolean flag=true;
        int n=clueDao.save(clue);
        if (n!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Clue> pageList(Clue clue,int skip, int size) {
        Map<String,Object> data=new HashMap<>();
        data.put("skip",skip);
        data.put("size",size);
        data.put("clue",clue);
        List<Clue> clueList=clueDao.pageList(data);
        return clueList;
    }

    @Override
    public int allCount() {
        int n =clueDao.allCount();
        return n;
    }

    @Override
    public ModelAndView getDetailById(String id) {
        ModelAndView mv=new ModelAndView();
        Clue clue=clueDao.getDetailById(id);
        mv.addObject("clue",clue);
        return mv;
    }

    @Override
    public List<Activity> getActivitiesByClueId(String id) {
        List<Activity> activities=clueActivityRelationDao.selectActivities(id);
        return activities;
    }

    @Override
    public boolean removeRelationById(String id) {
        boolean flag=true;
        int n=clueActivityRelationDao.deleteById(id);
        if (n!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Activity> selectActivities(String sname, String id) {
        List<Activity> activities=activityDao.unRelate(sname,id);
        return activities;
    }

    @Override
    public boolean makeRelation(String[] activityIds, String clueId) {
        boolean flag=true;
        ClueActivityRelation relation=new ClueActivityRelation();
        for(String aid:activityIds){
            relation.setId(UUIDUtil.getUUID());
            relation.setActivityId(aid);
            relation.setClueId(clueId);
            int n=clueActivityRelationDao.makeRelation(relation);
            if (n!=1){
                flag=false;
            }
        }
        return flag;
    }
}
