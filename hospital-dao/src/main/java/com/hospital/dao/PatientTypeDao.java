package com.hospital.dao;

import com.hospital.domain.PatientType;

import java.util.List;

public interface PatientTypeDao {

    List<PatientType> getAllPatientType();

    PatientType getTypeById(PatientType patientType);

    PatientType updatePatientType(PatientType updatePatientType);

    boolean addPatientType(PatientType patientType);

    PatientType getTypeByName(PatientType type);

}
