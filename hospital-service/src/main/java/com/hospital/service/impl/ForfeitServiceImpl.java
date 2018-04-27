package com.hospital.service.impl;

import com.hospital.dao.DeliveryDao;
import com.hospital.dao.ForfeitDao;
import com.hospital.dao.RetrieveDao;
import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.ForfeitInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.service.ForfeitService;

import java.util.ArrayList;
import java.util.List;

public class ForfeitServiceImpl implements ForfeitService {

    private ForfeitDao forfeitDao;
    private RetrieveDao retrieveDao;
    private DeliveryDao deliveryDao;


    public void setDeliveryDao(DeliveryDao deliveryDao) {
        this.deliveryDao = deliveryDao;
    }

    public void setRetrieveDao(RetrieveDao retrieveDao) {
        this.retrieveDao = retrieveDao;
    }

    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
    }

    @Override
    public PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode,
                                                       int pageSize) {
        // TODO Auto-generated method stub
        return forfeitDao.findForfeitInfoByPage(pageCode, pageSize);
    }

    @Override
    public PageBean<ForfeitInfo> queryForfeitInfo(String openID,
                                                  int deliveryId, int pageCode, int pageSize) {
        PageBean<ForfeitInfo> pageBean = new PageBean<ForfeitInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        PageBean<Integer> list = retrieveDao.getDeliveryIdList(openID, deliveryId, pageCode, pageSize);
        pageBean.setTotalRecord(list.getTotalRecord());
        List<Integer> beanList = list.getBeanList();
        if (beanList.size() == 0) {
            return null;
        }
        List<ForfeitInfo> forfeitInfos = new ArrayList<ForfeitInfo>();
        for (Integer i : beanList) {
            ForfeitInfo forfeitInfo = new ForfeitInfo();
            forfeitInfo.setDeliveryId(i);
            ForfeitInfo info = forfeitDao.getForfeitInfoById(forfeitInfo);
            if (info != null) {//防止当逾期记录还不存在时候，病人点击查看个人逾期的时候报空指针的问题
                forfeitInfos.add(info);
            }
        }
        if (forfeitInfos.size() == 0) {
            return null;
        }
        pageBean.setBeanList(forfeitInfos);
        return pageBean;
    }

    @Override
    public ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo) {
        // TODO Auto-generated method stub
        return forfeitDao.getForfeitInfoById(forfeitInfo);
    }

    @Override
    public int payForfeit(ForfeitInfo forfeitInfo) {
        //支付提醒步骤
        /*
         * 1. 得到分发记录
         *
         * 2. 查看当前的分发状态
         * 		2.1 如果当前状态为未答卷(逾期未答卷,分发逾期未答卷),则提示病人先去答卷再来设置提醒,返回-1
         * 		2.2 如果当前状态为答卷,则继续下一步
         *
         * 3. 获得当前的医生
         *
         * 4. 为当前提醒记录进行设置为已支付并设置医生
         *
         * 5. 修改提醒记录
         */
        //得到分发记录
        DeliveryInfo info = new DeliveryInfo();
        info.setDeliveryId(forfeitInfo.getDeliveryId());
        DeliveryInfo deliveryInfoById = deliveryDao.getDeliveryInfoById(info);
        //查看当前的分发状态
/*        int state = deliveryInfoById.getState();
        if (state == 1 || state == 4) {*/
        if (deliveryInfoById.getRetrieveInfo() == null) {
            //如果当前状态为未答卷(逾期未答卷,分发逾期未答卷),则提示病人先去答卷再来设置提醒,返回-1
            return -1;
        }
        //得到当前提醒
        ForfeitInfo forfeitInfoById = forfeitDao.getForfeitInfoById(forfeitInfo);
        //如果当前提醒状态为已支付
        if (forfeitInfoById.getIsPay() == 1) {
            //提示已经设置提醒了
            return -2;
        }
        //为当前提醒记录进行设置为已支付并设置医生
        forfeitInfoById.setDoctor(forfeitInfo.getDoctor());
        forfeitInfoById.setIsPay(1);
        //修改提醒记录
        ForfeitInfo updateForfeitInfo = forfeitDao.updateForfeitInfo(forfeitInfoById);
        if (updateForfeitInfo != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public PageBean<ForfeitInfo> findMyForfeitInfoByPage(Patient patient,
                                                         int pageCode, int pageSize) {
        // TODO Auto-generated method stub
        int deliveryId = 0;
        String openID = patient.getOpenID();
        return queryForfeitInfo(openID, deliveryId, pageCode, pageSize);
    }


}
