package com.cqupt.settings.service;

import com.cqupt.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicTypeService {

    Map<String, List<DicValue>> selectAllValues();
}
