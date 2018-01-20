package com.hospital.service;

import java.util.List;

import com.hospital.dao.PatientTypeDao;
import com.hospital.domain.PatientType;

public interface PatientTypeService {

    List<PatientType> getAllPatientType();

    PatientType getTypeById(PatientType patientType);

    PatientType updatePatientType(PatientType updatePatientType);

    boolean addPatientType(PatientType patientType);


}
