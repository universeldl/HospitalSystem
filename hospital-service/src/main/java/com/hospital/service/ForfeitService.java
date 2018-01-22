package com.hospital.service;

import com.hospital.domain.ForfeitInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

public interface ForfeitService {

    PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode, int pageSize);

    PageBean<ForfeitInfo> queryForfeitInfo(String openID, int deliveryId, int pageCode, int pageSize);

    ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo);

    int payForfeit(ForfeitInfo forfeitInfo);

    PageBean<ForfeitInfo> findMyForfeitInfoByPage(Patient patient, int pageCode, int pageSize);

}
 