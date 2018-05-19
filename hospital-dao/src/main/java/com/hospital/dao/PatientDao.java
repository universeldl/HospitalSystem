package com.hospital.dao;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

import java.util.List;

public interface PatientDao {

    List<Integer> getPatientSexByDoctor(Doctor doctor);

    List<Patient> getPatientsByDoctor(Doctor doctor);

    Integer[] getAdditionsForLast12Months(Doctor doctor);

    Patient getPatient(Patient patient);

    Patient updatePatientInfo(Patient patient);

    boolean addPatient(Patient patient);

    PageBean<Patient> findPatientByPage(int pageCode, int pageSize, Doctor doctor);

    PageBean<Patient> findRecyclePatientByPage(int pageCode, int pageSize, Doctor doctor);

    Patient getPatientById(Patient patient);

    boolean deletePatient(Patient patient);

    PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize, Doctor doctor,
                                   Integer hospitalId, Integer cityId, Integer provinceId);

    PageBean<Patient> queryRecyclePatient(Patient patient, int pageCode, int pageSize, Doctor doctor);

    Patient getPatientByopenID(Patient patient);

    int batchAddPatient(List<Patient> patients, List<Patient> failPatients);

    List<Patient> findAllPatients();

    List<Integer> findAllActivePatientIds();

    List<Patient> findAllPatientsByDoctor(Doctor doctor);

}
