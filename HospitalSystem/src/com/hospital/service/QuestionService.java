package com.hospital.service;

import com.hospital.domain.Question;
import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import net.sf.json.JSONObject;

public interface QuestionService {

	Question getQuestion(Question question);

	Question updateQuestionInfo(Question question);

	boolean addQuestion(Question question);

	PageBean<Question> findQuestionByPage(int pageCode, int pageSize);

	Question getQuestionById(Question question);

	int deleteQuestion(Question question);

	PageBean<Question> queryQuestion(Question question, int pageCode, int pageSize);

	Question getQuestionByopenID(Question question);

	Question getQuestionByOpenID(Question question);

	JSONObject batchAddQuestion(String fileName, Doctor doctor);

	String exportQuestion();

}
