package com.hospital.dao;

import java.util.List;

import com.hospital.domain.PatientType;

public interface PatientTypeDao {

    List<PatientType> getAllPatientType();

    PatientType getTypeById(PatientType patientType);

    PatientType updatePatientType(PatientType updatePatientType);

    boolean addPatientType(PatientType patientType);

    PatientType getTypeByName(PatientType type);

}
