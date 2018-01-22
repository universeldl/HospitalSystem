package com.hospital.service.impl;

import com.hospital.dao.SurveyTypeDao;
import com.hospital.domain.PageBean;
import com.hospital.domain.SurveyType;
import com.hospital.service.SurveyTypeService;

import java.util.List;

public class SurveyTypeServiceImpl implements SurveyTypeService {

    private SurveyTypeDao surveyTypeDao;

    public void setSurveyTypeDao(SurveyTypeDao surveyTypeDao) {
        this.surveyTypeDao = surveyTypeDao;
    }

    @Override
    public PageBean<SurveyType> findSurveyTypeByPage(int pageCode, int pageSize) {
        return surveyTypeDao.findSurveyTypeByPage(pageCode, pageSize);
    }

    @Override
    public SurveyType getSurveyTypeByName(SurveyType surveyType) {
        return surveyTypeDao.getSurveyTypeByName(surveyType);
    }

    @Override
    public boolean addSurveyType(SurveyType surveyType) {
        return surveyTypeDao.addSurveyType(surveyType);
    }

    @Override
    public SurveyType getSurveyTypeById(SurveyType surveyType) {
        return surveyTypeDao.getSurveyTypeById(surveyType);
    }

    @Override
    public SurveyType updateSurveyTypeInfo(SurveyType surveyType) {
        // TODO Auto-generated method stub
        return surveyTypeDao.updateSurveyTypeInfo(surveyType);
    }

    @Override
    public boolean deleteSurveyType(SurveyType surveyType) {
        return surveyTypeDao.deleteSurveyType(surveyType);
    }

    @Override
    public PageBean<SurveyType> querySurveyType(SurveyType surveyType, int pageCode,
                                                int pageSize) {
        // TODO Auto-generated method stub
        return surveyTypeDao.querySurveyType(surveyType, pageCode, pageSize);
    }

    @Override
    public SurveyType getSurveyType(SurveyType surveyType) {
        // TODO Auto-generated method stub
        return surveyTypeDao.getSurveyType(surveyType);
    }

    @Override
    public List<SurveyType> getAllSurveyTypes() {
        // TODO Auto-generated method stub
        return surveyTypeDao.getAllSurveyTypes();
    }


}
