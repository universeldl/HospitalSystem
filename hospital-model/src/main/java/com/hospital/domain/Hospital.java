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
    private boolean visible;

    public Integer getAid() { return aid; }
    public void setAid(Integer aid) { this.aid = aid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Hospital() {

    }


}
