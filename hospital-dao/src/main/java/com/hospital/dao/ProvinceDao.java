package com.hospital.dao;

import com.hospital.domain.Province;

import java.util.List;

public interface ProvinceDao {

    Province getProvinceByID(Province province);

    Province getProvinceByName(Province province);

    List<Province> getAllProvinces();

    boolean addProvince(Province province);

    Province updateProvince(Province province);

    boolean deleteProvince(Province province);
}
