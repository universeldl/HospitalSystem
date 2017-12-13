package com.hospital.service;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;

import java.util.List;


public interface DoctorService {
	/**
	 * 指定医生用户名得到指定医生
	 * @param doctor
	 * @return
	 */
    Doctor getDoctorByUserName(Doctor doctor);

	/**
	 * 修改医生个人资料
	 * @param doctor 传入修改的对象
	 * @return 修改后的对象
	 */
    Doctor updateDoctorInfo(Doctor doctor);

	List<Doctor> getAllDoctors();

	/**
	 * 添加医生
	 * @param doctor 传入添加的医生
	 * @return	是否添加成功
	 */
    boolean addDoctor(Doctor doctor);

	/**
	 * 指定医生id得到指定医生
	 * @param doctor
	 * @return
	 */
    Doctor getDoctorById(Doctor doctor);

	/**
	 * 分页查询医生
	 * @param pageCode
	 * @param pageSize
	 * @return
	 */
    PageBean<Doctor> findDoctorByPage(int pageCode, int pageSize);

	/**
	 * 条件查询医生
	 * @param doctor
	 */
    PageBean<Doctor> queryDoctor(Doctor doctor, int pageCode, int pageSize);

	/**
	 * 删除指定id的医生
	 * @param id
	 */
    boolean deleteDoctor(Doctor doctor);
	
}
