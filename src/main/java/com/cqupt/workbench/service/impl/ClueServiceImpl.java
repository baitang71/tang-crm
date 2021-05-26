package com.cqupt.workbench.service.impl;

import com.cqupt.workbench.dao.ClueDao;
import com.cqupt.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueDao clueDao;
}
