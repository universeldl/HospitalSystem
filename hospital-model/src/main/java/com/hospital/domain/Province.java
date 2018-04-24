package com.hospital.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * 病人类
 *
 * @author c
 */
public class Province implements Serializable {
    private Integer provinceId;
    private String name;
    private Set<City> cities;

    public Integer getProvinceId() { return provinceId; }
    public void setProvinceId(Integer provinceId) { this.provinceId = provinceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public Set<City> getCities() {
        return cities;
    }


}
