package com.hospital.dao;

import java.util.List;

import com.hospital.domain.Survey;
import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

public interface DeliveryDao {

	PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize);

	DeliveryInfo getDeliveryInfoById(DeliveryInfo info);

	int addDelivery(DeliveryInfo info);

	List<DeliveryInfo> getNoRetrieveDeliveryInfoByPatient(Patient patient);

	DeliveryInfo updateDeliveryInfo(DeliveryInfo deliveryInfoById);


	List<DeliveryInfo> getDeliveryInfoByNoRetrieveState();

	List<DeliveryInfo> getDeliveryInfoBySurvey(Survey survey);

}
