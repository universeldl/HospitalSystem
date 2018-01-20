package com.hospital.dao;

import com.hospital.domain.Question;
import com.hospital.domain.PageBean;

import java.util.List;

public interface QuestionDao {


    Question getQuestion(Question question);

    Question updateQuestionInfo(Question question);

    boolean addQuestion(Question question);

    PageBean<Question> findQuestionByPage(int pageCode, int pageSize);

    Question getQuestionById(Question question);

    boolean deleteQuestion(Question question);

    PageBean<Question> queryQuestion(Question question, int pageCode, int pageSize);

    Question getQuestionByopenID(Question question);

    int batchAddQuestion(List<Question> questions, List<Question> failQuestions);

    List<Question> findAllQuestions();

}
