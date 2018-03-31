package com.hospital.dao;

import com.hospital.domain.*;

import java.util.List;

public interface DeliveryDao {

    PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Doctor doctor);

    PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Patient patient);

    DeliveryInfo getDeliveryInfoById(DeliveryInfo info);

    List<DeliveryInfo> getDeliveryInfosByPatientId(Patient patient);

    int addDelivery(DeliveryInfo info);

    List<DeliveryInfo> getUnansweredDeliveryInfos(Integer patientId);

    PageBean<Integer> getDeliveryIdList(int surveyId, int pageCode, int pageSize, Patient patient);

    PageBean<Integer> getDeliveryIdList(String name, int deliveryId, int pageCode, int pageSize, Doctor doctor);

    List<DeliveryInfo> getNoRetrieveDeliveryInfoByPatient(Patient patient);

    DeliveryInfo updateDeliveryInfo(DeliveryInfo deliveryInfoById);


    List<DeliveryInfo> getDeliveryInfoByNoRetrieveState();

    List<DeliveryInfo> getDeliveryInfoBySurvey(Survey survey);

}
