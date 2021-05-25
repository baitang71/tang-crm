package com.cqupt.settings.dao;

import com.cqupt.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> selectByCode(String code);
}
