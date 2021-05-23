package com.cqupt.workbench.dao;

public interface ActivityRemarkDao {
    int selectCountByAids(String[] ids);

    int deleteByAids(String[] ids);
}
