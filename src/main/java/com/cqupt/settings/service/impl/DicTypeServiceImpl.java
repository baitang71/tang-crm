package com.cqupt.settings.service.impl;

import com.cqupt.settings.dao.DicTypeDao;
import com.cqupt.settings.dao.DicValueDao;
import com.cqupt.settings.domain.DicType;
import com.cqupt.settings.domain.DicValue;
import com.cqupt.settings.service.DicTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicTypeServiceImpl implements DicTypeService {

    @Resource
    private DicTypeDao dicTypeDao;
    @Resource
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> selectAllValues() {
        Map<String, List<DicValue>> map=new HashMap<>();
        List<DicType> dicTypes=dicTypeDao.selectAll();
        for (DicType dicType:dicTypes){
            List<DicValue> dicValues=dicValueDao.selectByCode(dicType.getCode());
            map.put(dicType.getCode()+"List",dicValues);
        }
        return map;
    }
}
