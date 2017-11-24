package com.hospital.dao;

import com.hospital.domain.Choice;
import com.hospital.domain.PageBean;

import java.util.List;

public interface ChoiceDao {

	
	Choice getChoice(Choice choice);

	Choice updateChoiceInfo(Choice choice);

	boolean addChoice(Choice choice);

	PageBean<Choice> findChoiceByPage(int pageCode, int pageSize);

	Choice getChoiceById(Choice choice);

	boolean deleteChoice(Choice choice);

	PageBean<Choice> queryChoice(Choice choice, int pageCode, int pageSize);

	Choice getChoiceByopenID(Choice choice);

	int batchAddChoice(List<Choice> choices, List<Choice> failChoices);

	List<Choice> findAllChoices();

}
