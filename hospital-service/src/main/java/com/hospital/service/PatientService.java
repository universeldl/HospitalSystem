package com.hospital.service;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import net.sf.json.JSONObject;

import java.util.List;

public interface PatientService {

    Patient getPatient(Patient patient);

    Integer[] getAdditionsForLast12Months(Doctor doctor);

/*
    List<Patient> findAllPatients();
*/
    List<Integer> getPatientSexByDoctor(Doctor doctor);

    List<Patient> getPatientsByDoctor(Doctor doctor);

    Patient updatePatientInfo(Patient patient);

    boolean addPatient(Patient patient);

    PageBean<Patient> findPatientByPage(int pageCode, int pageSize, Doctor doctor);

    PageBean<Patient> findRecyclePatientByPage(int pageCode, int pageSize, Doctor doctor);

    Patient getPatientById(Patient patient);

    int deletePatient(Patient patient);

    PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize, Doctor doctor,
                                   Integer hospitalId, Integer cityId, Integer provinceId);

    PageBean<Patient> queryRecyclePatient(Patient patient, int pageCode, int pageSize, Doctor doctor);

    Patient getPatientByOpenId(Patient patient);

    JSONObject batchAddPatient(String fileName, Doctor doctor);

    String exportPatient(Doctor doctor);

    boolean updatePlan();

    String exportSinglePatient(Patient patient);

}
