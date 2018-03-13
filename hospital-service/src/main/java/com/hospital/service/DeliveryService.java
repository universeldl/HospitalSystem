package com.hospital.service;

import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.PageBean;

import java.util.List;

public interface DeliveryService {

    PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize);

    DeliveryInfo getDeliveryInfoById(DeliveryInfo info);

    List<DeliveryInfo> getUnansweredDeliveryInfos();

    /**
     * 返回分发状态码
     *
     * @param info
     * @return
     */
    int addDelivery(DeliveryInfo info);

    DeliveryInfo updateDeliveryInfo(DeliveryInfo deliveryInfo);

    boolean checkAndDoDelivery();

    boolean checkDeliveryInfo();

    int resendSurvey(DeliveryInfo deliveryInfo);

    boolean sendTemplateMessage(DeliveryInfo deliveryInfo);

}
