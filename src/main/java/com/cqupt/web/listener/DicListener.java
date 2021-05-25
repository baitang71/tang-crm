package com.cqupt.web.listener;

import com.cqupt.settings.domain.DicValue;
import com.cqupt.settings.service.DicTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DicListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("=====监听器====");
        ServletContext application=servletContextEvent.getServletContext();
        ApplicationContext applicationContext= (ApplicationContext) application.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        DicTypeService dicTypeService= (DicTypeService) applicationContext.getBean("dicTypeServiceImpl");
        Map<String,List<DicValue>> map=dicTypeService.selectAllValues();
        Set<String> set=map.keySet();
        for (String dicType:set){
            application.setAttribute(dicType,map.get(dicType));
            System.out.println("====>"+dicType);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
