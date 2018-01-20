package com.hospital.dao;

import java.util.List;

import com.hospital.domain.ForfeitInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

public interface ForfeitDao {

    List<ForfeitInfo> getForfeitByPatient(Patient patient);

    boolean addForfeitInfo(ForfeitInfo forfeitInfo);

    PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode,
                                                int pageSize);


    ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo);

    ForfeitInfo updateForfeitInfo(ForfeitInfo forfeitInfoById);

}
