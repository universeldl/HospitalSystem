package com.hospital.service;

import com.hospital.domain.Answer;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.domain.RetrieveInfo;

import java.util.Set;

public interface RetrieveService {

    PageBean<RetrieveInfo> findRetrieveInfoByPage(int pageCode, int pageSize);

    RetrieveInfo getRetrieveInfoById(RetrieveInfo retrieveInfo);

    Set<Answer> getAnswerByDeliveryId(RetrieveInfo retrieveInfo);

    RetrieveInfo updateRetrieveInfo(RetrieveInfo retrieveInfo);

    boolean deleteRetrieveInfo(RetrieveInfo retrieveInfo);

    PageBean<RetrieveInfo> queryRetrieveInfo(String openID, int deliveryId, int pageCode, int pageSize);

    int addRetrieveInfo(RetrieveInfo retrieveInfo);

    PageBean<RetrieveInfo> findMyDeliveryInfoByPage(Patient patient,
                                                    int pageCode, int pageSize);

    RetrieveInfo getRetrieveInfoByDeliveryID(Integer deliveryID);
}
