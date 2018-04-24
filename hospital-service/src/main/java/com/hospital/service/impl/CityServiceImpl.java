package com.hospital.service.impl;


import com.hospital.dao.CityDao;
import com.hospital.dao.HospitalDao;
import com.hospital.domain.City;
import com.hospital.domain.Hospital;
import com.hospital.service.CityService;

import java.util.List;

public class CityServiceImpl implements CityService {

    private CityDao cityDao;

    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public City getCityByID(City city) {
        return cityDao.getCityByID(city);
    }

    @Override
    public City getCityByName(City city) {
        return cityDao.getCityByName(city);
    }

    @Override
    public List<City> getAllCities() {
        return cityDao.getAllCities();
    }

    @Override
    public boolean addCity(City city) {
        return cityDao.addCity(city);
    }

    @Override
    public City updateCity(City city) {
        return cityDao.updateCity(city);
    }

    @Override
    public boolean deleteCity(City city) {
        return cityDao.deleteCity(city);
    }
}
