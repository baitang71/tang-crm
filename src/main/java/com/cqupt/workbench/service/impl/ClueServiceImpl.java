package com.cqupt.workbench.service.impl;

import com.cqupt.utils.DateTimeUtil;
import com.cqupt.utils.UUIDUtil;
import com.cqupt.workbench.dao.*;
import com.cqupt.workbench.domain.*;
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
    private ActivityDao activityDao;
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;

    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;

    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;


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

    @Override
    public boolean convert(Tran tran, String clueId, String createBy) {
        /*
        (1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        (3) 通过线索对象提取联系人信息，保存联系人
        (4) 线索备注转换到客户备注以及联系人备注
        (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        (6) 如果有创建交易需求，创建一条交易
        (7) 如果创建了交易，则创建一条该交易下的交易历史
        (8) 删除线索备注
        (9) 删除线索和市场活动的关系
        (10) 删除线索  */
        boolean result=true;
        String createTime= DateTimeUtil.getSysTime();
        Clue clue=clueDao.selectById(clueId);

        String company=clue.getCompany();
        Customer customer=customerDao.selectByName(company);
        if (customer==null){
            customer=new Customer();
            customer.setWebsite(clue.getWebsite());
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setDescription(clue.getDescription());
            customer.setId(UUIDUtil.getUUID());
            customer.setName(company);
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setOwner(clue.getOwner());
            customer.setPhone(clue.getPhone());
            int count=customerDao.save(customer);
            if (count!=1){
                result=false;
            }
        }

        Contacts contacts=new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        int count1=contactsDao.save(contacts);
        if (count1!=1){
            result=false;
        }

        List<ClueRemark> clueRemarkList=clueRemarkDao.selectById(clueId);
        for (ClueRemark clueRemark:clueRemarkList){
            CustomerRemark customerRemark=new CustomerRemark();
            String noteContent=clueRemark.getNoteContent();
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            int count2=customerRemarkDao.save(customerRemark);
            if (count2!=1){
                result=false;
            }
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            int count3=contactsRemarkDao.save(contactsRemark);
            if (count3!=1){
                result=false;
            }
        }

        List<ClueActivityRelation> clueActivityRelationList=clueActivityRelationDao.selectByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            String activityId=clueActivityRelation.getActivityId();
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            int count4=contactsActivityRelationDao.save(contactsActivityRelation);
            if (count4!=1){
                result=false;
            }
        }

        if (tran.getId()!=null){
            tran.setCreateTime(createTime);
            tran.setCustomerId(customer.getId());
            tran.setDescription(clue.getDescription());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setOwner(clue.getOwner());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setContactSummary(clue.getContactSummary());
            int count5=tranDao.save(tran);
            if (count5!=1){
                result=false;
            }
            TranHistory tranHistory=new TranHistory();
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            tranHistory.setTranId(tran.getId());
            int count6=tranHistoryDao.save(tranHistory);
            if (count6!=1){
                result=false;
            }
        }

        for (ClueRemark clueRemark:clueRemarkList){
            int count7=clueRemarkDao.deleteById(clueRemark.getId());
            if (count7!=1){
                result=false;
            }
        }

        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            int count8=clueActivityRelationDao.deleteById(clueActivityRelation.getId());
            if (count8!=1){
                result=false;
            }
        }

        int count9=clueDao.deleteById(clueId);
        if (count9!=1){
            result=false;
        }














        return result;
    }
}



