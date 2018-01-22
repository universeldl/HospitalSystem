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
    public Set<Answer> getAnswerBySurveyId(RetrieveInfo retrieveInfo) {
        // TODO Auto-generated method stub
        return retrieveDao.getAnswerBySurveyId(retrieveInfo);
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
        //答卷的步骤
        /*
         * 1. 获得操作的分发编号
         *
         * 2. 获得当前的医生
         *
         * 3. 获得分发的问卷
         * 		3.1 问卷的总回收数增加
         *
         *
         * 4. 获取当前时间
         *
         * 5. 设置操作医生
         *
         * 6. 设置答卷时间
         *
         *
         * 7. 设置分发的状态
         * 		7.1 如果当前分发不属于重发，则设置为答卷
         * 		7.2 如果当前分发属于重发,则设置为重发答卷
         *
         * 8. 查看该分发记录有逾期提醒未设置的记录
         * 		8.1 如果有，返回状态码2,提示病人去答卷
         *		8.2 如果没有,则结束
         *
         *
         *
         */
        DeliveryInfo deliveryInfoById = deliveryDao.getDeliveryInfoById(retrieveInfo.getDeliveryInfo());//获得操作的分发编号
        if (deliveryInfoById.getState() == 2 || deliveryInfoById.getState() == 5) {//如果已经答卷了。
            return -1;//已经答卷
        }
        Survey survey = deliveryInfoById.getSurvey();
        Survey surveyById = surveyDao.getSurveyById(survey);//获得分发的问卷
        surveyById.setCurrentNum(surveyById.getCurrentNum() + 1);
        Survey b = surveyDao.updateSurveyInfo(surveyById);// 问卷的总回收数增加
        Date retrieveDate = new Date(System.currentTimeMillis());//获取当前时间
        RetrieveInfo retrieveInfoById = retrieveDao.getRetrieveInfoById(retrieveInfo);
        retrieveInfoById.setDoctor(retrieveInfo.getDoctor());//设置医生
        retrieveInfoById.setRetrieveDate(retrieveDate);//设置答卷时间
        int state = deliveryInfoById.getState();
        RetrieveInfo ba = null;
        if (b != null) {
            ba = retrieveDao.updateRetrieveInfo(retrieveInfoById);//修改答卷记录
        }
        if (deliveryInfoById.getState() == 0 || deliveryInfoById.getState() == 1) {
            deliveryInfoById.setState(2);//设置分发的状态
        }
        if (deliveryInfoById.getState() == 3 || deliveryInfoById.getState() == 4) {
            deliveryInfoById.setState(5);//设置分发的状态
        }
        DeliveryInfo bi = null;
        if (ba != null) {
            bi = deliveryDao.updateDeliveryInfo(deliveryInfoById);
        }
        if (bi != null) {
            if (state == 1 || state == 4) {
                return 2;//提示病人去答卷
            } else {
                return 1;
            }
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


}
