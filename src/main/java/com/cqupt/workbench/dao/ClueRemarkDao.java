package com.cqupt.workbench.dao;

import com.cqupt.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> selectById(String clueId);

    int deleteById(String id);
}
