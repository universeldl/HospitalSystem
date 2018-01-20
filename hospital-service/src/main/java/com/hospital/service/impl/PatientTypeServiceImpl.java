package com.hospital.service.impl;

import java.util.List;

import com.hospital.dao.PatientTypeDao;
import com.hospital.domain.PatientType;
import com.hospital.service.PatientTypeService;

public class PatientTypeServiceImpl implements PatientTypeService {

    public PatientTypeDao patientTypeDao;

    public void setPatientTypeDao(PatientTypeDao patientTypeDao) {
        this.patientTypeDao = patientTypeDao;
    }

    @Override
    public List<PatientType> getAllPatientType() {
        return patientTypeDao.getAllPatientType();
    }

    @Override
    public PatientType getTypeById(PatientType patientType) {
        return patientTypeDao.getTypeById(patientType);
    }

    @Override
    public PatientType updatePatientType(PatientType updatePatientType) {
        // TODO Auto-generated method stub
        return patientTypeDao.updatePatientType(updatePatientType);
    }

    @Override
    public boolean addPatientType(PatientType patientType) {
        // TODO Auto-generated method stub
        return patientTypeDao.addPatientType(patientType);
    }


}
