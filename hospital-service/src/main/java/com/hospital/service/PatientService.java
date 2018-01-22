package com.hospital.service;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import net.sf.json.JSONObject;

import java.util.List;

public interface PatientService {

    Patient getPatient(Patient patient);

    List<Patient> getPatientsByDoctor(Doctor doctor);

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
