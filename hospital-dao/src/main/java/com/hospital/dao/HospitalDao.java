package com.hospital.dao;

import com.hospital.domain.Hospital;
import com.hospital.domain.PageBean;

import java.util.List;

public interface HospitalDao {

    Hospital getHospitalByID(Hospital hospital);

    List<Hospital> getAllHospitals();

    boolean addHospital(Hospital hospital);

    boolean deleteHospital(Hospital hospital);

}
