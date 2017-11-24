package com.hospital.dao;

import java.util.List;

import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.domain.PatientType;

public interface PatientDao {

	
	Patient getPatient(Patient patient);

	Patient updatePatientInfo(Patient patient);

	boolean addPatient(Patient patient);

	PageBean<Patient> findPatientByPage(int pageCode, int pageSize);

	Patient getPatientById(Patient patient);

	boolean deletePatient(Patient patient);

	PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize);

	Patient getPatientByopenID(Patient patient);

	int batchAddPatient(List<Patient> patients, List<Patient> failPatients);

	List<Patient> findAllPatients();

}
