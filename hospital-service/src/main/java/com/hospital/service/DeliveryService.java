package com.hospital.service;

import com.hospital.domain.*;

import java.util.Calendar;
import java.util.List;

public interface DeliveryService {

    PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Doctor doctor);

    PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Patient patient);

    DeliveryInfo getDeliveryInfoById(DeliveryInfo info);

    List<DeliveryInfo> getDeliveryInfosByPatientId(Patient patient);

    List<DeliveryInfo> getUnansweredDeliveryInfos(Integer patientId);

    PageBean<DeliveryInfo> queryDeliveryInfo(String name, int deliveryId, int pageCode, int pageSize, Doctor doctor);

    PageBean<DeliveryInfo> queryDeliveryInfo(int queryType, int pageCode, int pageSize, Doctor doctor);

    PageBean<DeliveryInfo> queryDeliveryInfo(int surveyId, int pageCode, int pageSize, Patient patient);

    List<DeliveryInfo> getDelivryInfoBySurveyAndDate(DeliveryInfo deliveryInfo, Calendar start, Calendar end);
    /**
     * 返回分发状态码
     *
     * @param info
     * @return
     */
    int addDelivery(DeliveryInfo info);

    DeliveryInfo updateDeliveryInfo(DeliveryInfo deliveryInfo);

    boolean checkAndDoDelivery();

    boolean checkAndDoDeliveryNew();

    boolean checkAndDoDelivery2();

    boolean checkDeliveryInfo();

    int resendSurvey(DeliveryInfo deliveryInfo);

    boolean sendTemplateMessage(DeliveryInfo deliveryInfo);

}
