package com.hospital.service;

import com.hospital.domain.Hospital;
import net.sf.json.JSON;

import java.util.List;


public interface HospitalService {
    Hospital getHospitalByID(Hospital hospital);
    Hospital getHospitalByName(Hospital hospital);
    Hospital updateHospital(Hospital hospital);
    List<Hospital> getAllHospitals();
    List<Hospital> getAllVisibleHospitals();
    boolean addHospital(Hospital hospital);
    boolean deleteHospital(Hospital hospital);
}
