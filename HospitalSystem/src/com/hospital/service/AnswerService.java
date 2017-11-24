package com.hospital.service;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.domain.Answer;
import net.sf.json.JSONObject;

public interface AnswerService {

	Answer getAnswer(Answer answer);

	Answer updateAnswerInfo(Answer answer);

	boolean addAnswer(Answer answer);

	PageBean<Answer> findAnswerByPage(int pageCode, int pageSize);

	Answer getAnswerById(Answer answer);

	int deleteAnswer(Answer answer);

	PageBean<Answer> queryAnswer(Answer answer, int pageCode, int pageSize);

	Answer getAnswerByopenID(Answer answer);

	Answer getAnswerByOpenID(Answer answer);

	JSONObject batchAddAnswer(String fileName, Doctor doctor);

	String exportAnswer();

}
