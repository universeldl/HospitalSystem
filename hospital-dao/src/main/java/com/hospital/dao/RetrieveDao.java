package com.hospital.dao;

import com.hospital.domain.Answer;
import com.hospital.domain.PageBean;
import com.hospital.domain.RetrieveInfo;
import com.hospital.domain.Survey;

import java.util.Set;


public interface RetrieveDao {

    int addRetrieve(RetrieveInfo retrieveInfo);

    PageBean<RetrieveInfo> findRetrieveInfoByPage(int pageCode, int pageSize);

    RetrieveInfo getRetrieveInfoById(RetrieveInfo retrieveInfo);

    Set<Answer> getAnswerByDeliveryId(RetrieveInfo retrieveInfo);

    PageBean<Integer> getDeliveryIdList(String openID, int deliveryId, int pageCode, int pageSize);

    RetrieveInfo updateRetrieveInfo(RetrieveInfo retrieveInfoById);

    boolean deleteRetrieveInfo(RetrieveInfo retrieveInfo);

    RetrieveInfo getRetrieveInfoByDeliveryID(Integer deliveryID);

    Integer getSurveyCurNum(Survey survey);
}
