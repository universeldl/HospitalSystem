package com.hospital.service;

import com.hospital.domain.RetrieveInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

public interface RetrieveService {

	PageBean<RetrieveInfo> findRetrieveInfoByPage(int pageCode, int pageSize);

	RetrieveInfo getRetrieveInfoById(RetrieveInfo retrieveInfo);

	PageBean<RetrieveInfo> queryRetrieveInfo(String openID, int deliveryId, int pageCode, int pageSize);

	int addRetrieveInfo(RetrieveInfo retrieveInfo);

	PageBean<RetrieveInfo> findMyDeliveryInfoByPage(Patient patient,
                                              int pageCode, int pageSize);

}
