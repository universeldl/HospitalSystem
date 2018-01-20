package com.hospital.service;

import com.hospital.domain.Choice;
import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import net.sf.json.JSONObject;

public interface ChoiceService {

    Choice getChoice(Choice choice);

    Choice updateChoiceInfo(Choice choice);

    boolean addChoice(Choice choice);

    PageBean<Choice> findChoiceByPage(int pageCode, int pageSize);

    Choice getChoiceById(Choice choice);

    int deleteChoice(Choice choice);

    PageBean<Choice> queryChoice(Choice choice, int pageCode, int pageSize);

    Choice getChoiceByopenID(Choice choice);

    Choice getChoiceByOpenID(Choice choice);

    JSONObject batchAddChoice(String fileName, Doctor doctor);

    String exportChoice();

}
