package com.hospital.dao;

import com.hospital.domain.ForfeitInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

import java.util.List;

public interface ForfeitDao {

    List<ForfeitInfo> getForfeitByPatient(Patient patient);

    boolean addForfeitInfo(ForfeitInfo forfeitInfo);

    PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode,
                                                int pageSize);


    ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo);

    ForfeitInfo updateForfeitInfo(ForfeitInfo forfeitInfoById);

}
