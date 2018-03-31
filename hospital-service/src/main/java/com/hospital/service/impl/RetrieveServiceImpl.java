package com.hospital.service.impl;

import com.hospital.dao.DeliveryDao;
import com.hospital.dao.RetrieveDao;
import com.hospital.dao.SurveyDao;
import com.hospital.domain.*;
import com.hospital.service.RetrieveService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class RetrieveServiceImpl implements RetrieveService {

    private RetrieveDao retrieveDao;
    private SurveyDao surveyDao;
    private DeliveryDao deliveryDao;


    public void setSurveyDao(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    public void setDeliveryDao(DeliveryDao deliveryDao) {
        this.deliveryDao = deliveryDao;
    }

    public void setRetrieveDao(RetrieveDao retrieveDao) {
        this.retrieveDao = retrieveDao;
    }

    @Override
    public PageBean<RetrieveInfo> findRetrieveInfoByPage(int pageCode, int pageSize) {
        // TODO Auto-generated method stub
        return retrieveDao.findRetrieveInfoByPage(pageCode, pageSize);
    }

    @Override
    public RetrieveInfo getRetrieveInfoById(RetrieveInfo retrieveInfo) {
        // TODO Auto-generated method stub
        return retrieveDao.getRetrieveInfoById(retrieveInfo);
    }

    @Override
    public Set<Answer> getAnswerByDeliveryId(RetrieveInfo retrieveInfo) {
        // TODO Auto-generated method stub
        return retrieveDao.getAnswerByDeliveryId(retrieveInfo);
    }

    @Override
    public RetrieveInfo updateRetrieveInfo(RetrieveInfo retrieveInfo) {
        // TODO Auto-generated method stub
        return retrieveDao.updateRetrieveInfo(retrieveInfo);
    }

    @Override
    public boolean deleteRetrieveInfo(RetrieveInfo retrieveInfo) {
        return retrieveDao.deleteRetrieveInfo(retrieveInfo);
    }

    @Override
    public PageBean<RetrieveInfo> queryRetrieveInfo(String openID, int deliveryId, int pageCode, int pageSize) {
        PageBean<RetrieveInfo> pageBean = new PageBean<RetrieveInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        PageBean<Integer> list = retrieveDao.getDeliveryIdList(openID, deliveryId, pageCode, pageSize);
        pageBean.setTotalRecord(list.getTotalRecord());
        List<Integer> beanList = list.getBeanList();
        if (beanList.size() == 0) {
            return null;
        }
        List<RetrieveInfo> retrieveInfos = new ArrayList<RetrieveInfo>();
        for (Integer i : beanList) {
            RetrieveInfo retrieveInfo = new RetrieveInfo();
            retrieveInfo.setDeliveryId(i);
            RetrieveInfo info = retrieveDao.getRetrieveInfoById(retrieveInfo);
            retrieveInfos.add(info);
        }
        pageBean.setBeanList(retrieveInfos);
        return pageBean;
    }

    @Override
    public int addRetrieveInfo(RetrieveInfo retrieveInfo) {
        DeliveryInfo deliveryInfo = retrieveInfo.getDeliveryInfo();
        if (deliveryInfo.getState() == -1) {//如果已经答卷了。
            return -1;//已经答卷
        }
        Survey survey = deliveryInfo.getSurvey();
        survey.setCurrentNum(survey.getCurrentNum() + 1);
        Survey b = surveyDao.updateSurveyInfo(survey);// 问卷的总回收数增加
        Date retrieveDate = new Date(System.currentTimeMillis());//获取当前时间
        if(retrieveInfo.getRetrieveDate() != null) {
            retrieveDate = retrieveInfo.getRetrieveDate();
        }
        retrieveInfo.setRetrieveDate(retrieveDate);//设置答卷时间
        int ba = 0;
        if (b != null) {
            ba = retrieveDao.addRetrieve(retrieveInfo);
        }
        deliveryInfo.setState(-1);//设置分发的状态为已答卷
        DeliveryInfo bi = null;
        if (ba != -1) {
            bi = deliveryDao.updateDeliveryInfo(deliveryInfo);
        }
        if (bi != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public PageBean<RetrieveInfo> findMyDeliveryInfoByPage(Patient patient,
                                                           int pageCode, int pageSize) {
        // TODO Auto-generated method stub
        int deliveryId = 0;
        String openID = patient.getOpenID();
        return queryRetrieveInfo(openID, deliveryId, pageCode, pageSize);
    }

    @Override
    public RetrieveInfo getRetrieveInfoByDeliveryID(Integer deliveryID) {
        System.out.println("test1");
        return retrieveDao.getRetrieveInfoByDeliveryID(deliveryID);
    }


}
