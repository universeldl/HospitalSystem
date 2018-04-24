package com.hospital.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * 病人类
 *
 * @author c
 */
public class Hospital implements Serializable {
    private Integer hospitalId;
    private String name;
    private boolean visible;
    private Set<Doctor> doctors;
    private City city;

    public Integer getHospitalId() { return hospitalId; }
    public void setHospitalId(Integer hospitalId) { this.hospitalId = hospitalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
