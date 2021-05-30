package com.cqupt.workbench.dao;


import com.cqupt.workbench.domain.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    List<Clue> pageList(Map<String,Object> data);

    int allCount();

    Clue getDetailById(String id);

    Clue selectById(String clueId);

    int deleteById(String clueId);
}
