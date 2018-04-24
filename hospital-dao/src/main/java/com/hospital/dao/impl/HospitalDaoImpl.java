package com.hospital.dao.impl;

import com.hospital.dao.HospitalDao;
import com.hospital.domain.Hospital;
import net.sf.json.JSON;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;


public class HospitalDaoImpl extends HibernateDaoSupport implements HospitalDao {

    @Override
    public Hospital getHospitalByID(Hospital hospital) {
        String hql = "from Hospital a where a.hospitalId=?";
        List list = this.getHibernateTemplate().find(hql, hospital.getHospitalId());
        if (list != null && list.size() > 0) {
            return (Hospital) list.get(0);
        }
        return null;
    }

    @Override
    public Hospital getHospitalByName(Hospital hospital) {
        String hql = "from Hospital a where a.name=?";
        List list = this.getHibernateTemplate().find(hql, hospital.getName());
        if (list != null && list.size() > 0) {
            return (Hospital) list.get(0);
        }
        return null;
    }

    @Override
    public Hospital updateHospital(Hospital hospital) {
        Hospital newHospital = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            newHospital = (Hospital) this.getHibernateTemplate().merge(hospital);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return newHospital;
    }

    @Override
    public List<Hospital> getAllHospitals() {
        String hql = "from Hospital";
        List<Hospital> list = null;
        try {
            list = (List<Hospital>) this.getHibernateTemplate().find(hql);
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return list;
    }

    @Override
    public List<Hospital> getAllVisibleHospitals() {
        String hql = "from Hospital a where a.visible=?";
        List<Hospital> list = null;
        try {
            list = (List<Hospital>) this.getHibernateTemplate().find(hql, true);
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return list;
    }

    @Override
    public boolean addHospital(Hospital hospital) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(hospital);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }

    @Override
    public boolean deleteHospital(Hospital hospital) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(hospital);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }
}
