package com.hospital.domain;

import java.io.Serializable;

/**
 * 提醒信息类
 *
 * @author c
 */
public class ForfeitInfo implements Serializable {

    private Integer deliveryId;    //分发编号
    private DeliveryInfo deliveryInfo;
    private Doctor doctor;    //操作员
    private Double forfeit;    //提醒金额
    private Integer isPay;    //是否已经支付提醒

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Double getForfeit() {
        return forfeit;
    }

    public void setForfeit(Double forfeit) {
        this.forfeit = forfeit;
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public ForfeitInfo() {

    }


}
