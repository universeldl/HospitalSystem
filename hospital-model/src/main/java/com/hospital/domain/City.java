package com.hospital.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * 病人类
 *
 * @author c
 */
public class City implements Serializable {
    private Integer cityId;
    private String name;
    private Set<Hospital> hospitals;
    private Province province;

    public Integer getCityId() { return cityId; }
    public void setCityId(Integer cityId) { this.cityId = cityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setHospitals(Set<Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    public Set<Hospital> getHospitals() {
        return hospitals;
    }


    public void setProvince(Province province) {
        this.province = province;
    }

    public Province getProvince() {
        return province;
    }

}
