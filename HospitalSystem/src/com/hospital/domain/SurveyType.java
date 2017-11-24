package com.hospital.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * 问卷类型类
 * @author c
 *
 */
public class SurveyType implements Serializable{

	private Integer typeId; //问卷类型编号
	private String typeName;	//问卷类型名称
	
	
	
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	
	public SurveyType() {
	
	}

	
	
	
	
	
	
	
}
