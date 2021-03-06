package com.hospital.service.impl;


import com.hospital.dao.HospitalDao;
import com.hospital.domain.Hospital;
import com.hospital.domain.PageBean;
import com.hospital.service.HospitalService;
import net.sf.json.JSON;

import java.util.List;

public class HospitalServiceImpl implements HospitalService {

    private HospitalDao hospitalDao;

    public void setHospitalDao(HospitalDao hospitalDao) {
        this.hospitalDao = hospitalDao;
    }

    @Override
    public Hospital getHospitalByID(Hospital hospital) {
        return hospitalDao.getHospitalByID(hospital);
    }

    @Override
    public Hospital getHospitalByName(Hospital hospital) {
        return hospitalDao.getHospitalByName(hospital);
    }
    @Override
    public Hospital updateHospital(Hospital hospital) {
        return hospitalDao.updateHospital(hospital);
    }
    @Override
    public List<Hospital> getAllHospitals() {
        return hospitalDao.getAllHospitals();
    }
    @Override

    public List<Hospital> getAllVisibleHospitals() {
        return hospitalDao.getAllVisibleHospitals();
    }
    @Override
    public boolean addHospital(Hospital hospital) {
        return hospitalDao.addHospital(hospital);
    }

    @Override
    public boolean deleteHospital(Hospital hospital) {
        return hospitalDao.deleteHospital(hospital);
    }
}
