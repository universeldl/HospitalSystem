package com.hospital.service;

import java.util.List;

import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

public interface DeliveryService {

    PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize);

    DeliveryInfo getDeliveryInfoById(DeliveryInfo info);

    /**
     * 返回分发状态码
     *
     * @param info
     * @return
     */
    int addDelivery(DeliveryInfo info);

    boolean checkDeliveryInfo();

    int resendSurvey(DeliveryInfo deliveryInfo);


}
