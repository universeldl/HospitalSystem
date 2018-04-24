package com.hospital.dao;

import com.hospital.domain.City;
import com.hospital.domain.Hospital;

import java.util.List;

public interface CityDao {

    City getCityByID(City city);

    City getCityByName(City city);

    List<City> getAllCities();

    boolean addCity(City city);

    boolean deleteCity(City city);

    City updateCity(City city);
}
