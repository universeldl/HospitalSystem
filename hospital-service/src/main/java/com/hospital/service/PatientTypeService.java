package com.hospital.service;

import com.hospital.domain.PatientType;

import java.util.List;

public interface PatientTypeService {

    List<PatientType> getAllPatientType();

    PatientType getTypeById(PatientType patientType);

    PatientType updatePatientType(PatientType updatePatientType);

    boolean addPatientType(PatientType patientType);


}
