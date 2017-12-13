package com.hospital.service.impl;


import com.hospital.dao.DoctorDao;
import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.service.DoctorService;

import java.util.List;

public class DoctorServiceImpl implements DoctorService{

	private DoctorDao doctorDao;

	public void setDoctorDao(DoctorDao doctorDao) {
		this.doctorDao = doctorDao;
	}

	@Override
	public Doctor getDoctorByUserName(Doctor doctor) {
		return doctorDao.getDoctorByUserName(doctor);
	}

	@Override
	public Doctor updateDoctorInfo(Doctor doctor) {
		return doctorDao.updateDoctorInfo(doctor);
	}

	@Override
	public List<Doctor> getAllDoctors() {
		return doctorDao.getAllDoctors();
	}

	@Override
	public boolean addDoctor(Doctor doctor) {
		return doctorDao.addDoctor(doctor);
	}

	@Override
	public Doctor getDoctorById(Doctor doctor) {
		return doctorDao.getDoctorById(doctor);
	}

	@Override
	public PageBean<Doctor> findDoctorByPage(int pageCode, int pageSize) {
		return doctorDao.findDoctorByPage(pageCode,pageSize);
	}

	@Override
	public PageBean<Doctor> queryDoctor(Doctor doctor,int pageCode,int pageSize) {
		return doctorDao.queryDoctor(doctor,pageCode,pageSize);
	}

	@Override
	public boolean deleteDoctor(Doctor doctor) {
		Doctor deleteDoctor = getDoctorById(doctor);
		return doctorDao.deleteDoctor(deleteDoctor);

	}

}
