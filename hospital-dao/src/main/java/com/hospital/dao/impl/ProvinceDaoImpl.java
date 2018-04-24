package com.hospital.dao.impl;

import com.hospital.dao.CityDao;
import com.hospital.dao.ProvinceDao;
import com.hospital.domain.City;
import com.hospital.domain.Province;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;


public class ProvinceDaoImpl extends HibernateDaoSupport implements ProvinceDao {

    @Override
    public Province getProvinceByID(Province province) {
        String hql = "from Province a where a.provinceId=?";
        List list = this.getHibernateTemplate().find(hql, province.getProvinceId());
        if (list != null && list.size() > 0) {
            return (Province) list.get(0);
        }
        return null;
    }

    @Override
    public Province getProvinceByName(Province province) {
        String hql = "from Province a where a.name=?";
        List list = this.getHibernateTemplate().find(hql, province.getName());
        if (list != null && list.size() > 0) {
            return (Province) list.get(0);
        }
        return null;
    }

    @Override
    public List<Province> getAllProvinces() {
        String hql = "from Province";
        List<Province> list = null;
        try {
            list = (List<Province>) this.getHibernateTemplate().find(hql);
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return list;
    }


    @Override
    public boolean addProvince(Province province) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(province);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }

    @Override
    public Province updateProvince(Province province) {
        Province newProvince = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            newProvince = (Province) this.getHibernateTemplate().merge(province);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return newProvince;
    }

    @Override
    public boolean deleteProvince(Province province) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(province);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }
}
