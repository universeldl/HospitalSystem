package com.hospital.dao;

import com.hospital.domain.PageBean;
import com.hospital.domain.Answer;

import java.util.List;

public interface AnswerDao {

	
	Answer getAnswer(Answer answer);

	Answer updateAnswerInfo(Answer answer);

	boolean addAnswer(Answer answer);

	PageBean<Answer> findAnswerByPage(int pageCode, int pageSize);

	Answer getAnswerById(Answer answer);

	boolean deleteAnswer(Answer answer);

	PageBean<Answer> queryAnswer(Answer answer, int pageCode, int pageSize);

	Answer getAnswerByopenID(Answer answer);

	int batchAddAnswer(List<Answer> answers, List<Answer> failAnswers);

	List<Answer> findAllAnswers();

}
