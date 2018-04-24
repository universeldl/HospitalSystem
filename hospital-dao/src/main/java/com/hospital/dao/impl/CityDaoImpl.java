package com.hospital.dao.impl;

import com.hospital.dao.CityDao;
import com.hospital.dao.HospitalDao;
import com.hospital.domain.City;
import com.hospital.domain.Hospital;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;


public class CityDaoImpl extends HibernateDaoSupport implements CityDao {

    @Override
    public City getCityByID(City city) {
        String hql = "from City a where a.cityId=?";
        List list = this.getHibernateTemplate().find(hql, city.getCityId());
        if (list != null && list.size() > 0) {
            return (City) list.get(0);
        }
        return null;
    }

    @Override
    public City getCityByName(City city) {
        String hql = "from City a where a.name=?";
        List list = this.getHibernateTemplate().find(hql, city.getName());
        if (list != null && list.size() > 0) {
            return (City) list.get(0);
        }
        return null;
    }

    @Override
    public List<City> getAllCities() {
        String hql = "from City";
        List<City> list = null;
        try {
            list = (List<City>) this.getHibernateTemplate().find(hql);
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return list;
    }


    @Override
    public boolean addCity(City city) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(city);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }

    @Override
    public City updateCity(City city) {
        City newCity = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            newCity = (City) this.getHibernateTemplate().merge(city);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return newCity;
    }

    @Override
    public boolean deleteCity(City city) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(city);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }
}
