package com.hospital.service;

import com.hospital.domain.City;

import java.util.List;


public interface CityService {
    City getCityByID(City city);
    List<City> getAllCities();
    boolean addCity(City city);
    boolean deleteCity(City city);
    City getCityByName(City city);
    City updateCity(City city);
}
