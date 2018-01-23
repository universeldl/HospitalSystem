package com.hospital.domain;

import java.io.Serializable;

/**
 * 病人类
 *
 * @author c
 */
public class Hospital implements Serializable {
    private Integer aid;
    private String name;

    public Integer getAid() { return aid; }
    public void setAid(Integer aid) { this.aid = aid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Hospital() {

    }


}
