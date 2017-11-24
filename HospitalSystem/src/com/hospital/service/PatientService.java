package com.hospital.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.domain.PatientType;

public interface PatientService {

	Patient getPatient(Patient patient);

	Patient updatePatientInfo(Patient patient);

	boolean addPatient(Patient patient);

	PageBean<Patient> findPatientByPage(int pageCode, int pageSize);

	Patient getPatientById(Patient patient);

	int deletePatient(Patient patient);

	PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize);

	Patient getPatientByopenID(Patient patient);

	Patient getPatientByOpenID(Patient patient);

	JSONObject batchAddPatient(String fileName, Doctor doctor);

	String exportPatient();

}
