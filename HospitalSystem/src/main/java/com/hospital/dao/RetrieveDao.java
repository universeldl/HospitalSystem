package com.hospital.dao;

import java.util.List;
import java.util.Set;

import com.hospital.domain.RetrieveInfo;
import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.domain.Answer;


public interface RetrieveDao {

	int addRetrieve(RetrieveInfo retrieveInfo);

	PageBean<RetrieveInfo> findRetrieveInfoByPage(int pageCode, int pageSize);

	RetrieveInfo getRetrieveInfoById(RetrieveInfo retrieveInfo);

	Set<Answer> getAnswerBySurveyId(RetrieveInfo retrieveInfo);

	PageBean<Integer> getDeliveryIdList(String openID, int deliveryId, int pageCode, int pageSize);

	RetrieveInfo updateRetrieveInfo(RetrieveInfo retrieveInfoById);

	boolean deleteRetrieveInfo(RetrieveInfo retrieveInfo);

	

}
