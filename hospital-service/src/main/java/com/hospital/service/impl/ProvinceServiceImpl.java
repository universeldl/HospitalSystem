package com.hospital.service.impl;


import com.hospital.dao.ProvinceDao;
import com.hospital.domain.Province;
import com.hospital.service.ProvinceService;

import java.util.List;

public class ProvinceServiceImpl implements ProvinceService {

    private ProvinceDao provinceDao;

    public void setProvinceDao(ProvinceDao provinceDao) {
        this.provinceDao = provinceDao;
    }

    @Override
    public Province getProvinceByID(Province province) {
        return provinceDao.getProvinceByID(province);
    }

    @Override
    public Province getProvinceByName(Province province) {
        return provinceDao.getProvinceByName(province);
    }

    @Override
    public List<Province> getAllProvinces() {
        return provinceDao.getAllProvinces();
    }

    @Override
    public boolean addProvince(Province province) {
        return provinceDao.addProvince(province);
    }

    @Override
    public Province updateProvince(Province province) {
        return provinceDao.updateProvince(province);
    }
    @Override
    public boolean deleteProvince(Province province) {
        return provinceDao.deleteProvince(province);
    }
}
