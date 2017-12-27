package com.hospital.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.hospital.dao.PatientTypeDao;

import com.hospital.domain.PatientType;

public class PatientTypeDaoImpl extends HibernateDaoSupport implements PatientTypeDao{

	@Override
	public List<PatientType> getAllPatientType() {

		String hql= "from PatientType";
		List list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
		
	}

	@Override
	public PatientType getTypeById(PatientType patientType) {
		String hql= "from PatientType r where r.patientTypeId=?";
		List list = this.getHibernateTemplate().find(hql, patientType.getPatientTypeId());
		if(list!=null && list.size()>0){
			return (PatientType) list.get(0);
		}
		return null;
	}

	@Override
	public PatientType updatePatientType(PatientType updatePatientType) {
		PatientType newPatientType = null;
		try{
			this.getHibernateTemplate().clear();
			//将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
			newPatientType = (PatientType) this.getHibernateTemplate().merge(updatePatientType);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return newPatientType;
	}

	@Override
	public boolean addPatientType(PatientType patientType) {
		boolean b = true;
		try{
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().save(patientType);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}

	@Override
	public PatientType getTypeByName(PatientType type) {
		String hql= "from PatientType r where r.patientTypeName=?";
		List list = this.getHibernateTemplate().find(hql, type.getPatientTypeName());
		if(list!=null && list.size()>0){
			return (PatientType) list.get(0);
		}
		return null;
	}

}
