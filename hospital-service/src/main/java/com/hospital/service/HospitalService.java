package com.hospital.service;

import com.hospital.domain.Hospital;
import java.util.List;


public interface HospitalService {
    Hospital getHospitalByID(Hospital hospital);
    List<Hospital> getAllHospitals();
    boolean addHospital(Hospital hospital);
    boolean deleteHospital(Hospital hospital);
}
