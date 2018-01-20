package com.hospital.dao;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;

import java.util.List;

public interface DoctorDao {

    Doctor getDoctorByUserName(Doctor doctor);

    Doctor updateDoctorInfo(Doctor doctor);

    List<Doctor> getAllDoctors();

    boolean addDoctor(Doctor doctor);

    Doctor getDoctorById(Doctor doctor);

    PageBean<Doctor> findDoctorByPage(int pageCode, int pageSize);

    PageBean<Doctor> queryDoctor(Doctor doctor, int pageCode, int pageSize);

    boolean deleteDoctor(Doctor doctor);
}
