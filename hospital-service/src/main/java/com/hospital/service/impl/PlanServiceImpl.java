package com.hospital.service.impl;

import com.hospital.dao.PlanDao;
import com.hospital.domain.Plan;
import com.hospital.service.PlanService;

import java.util.List;

public class PlanServiceImpl implements PlanService {

    public PlanDao planDao;

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    @Override
    public List<Plan> getAllPlan() {
        return planDao.getAllPlan();
    }

    @Override
    public Plan getPlanById(Plan plan) {
        return planDao.getPlanById(plan);
    }

    @Override
    public Plan getPlan(Plan plan) {
        return planDao.getPlan(plan);
    }

    @Override
    public Plan updatePlan(Plan updatePlan) {
        // TODO Auto-generated method stub
        return planDao.updatePlan(updatePlan);
    }

    @Override
    public boolean addPlan(Plan plan) {
        // TODO Auto-generated method stub
        return planDao.addPlan(plan);
    }

    @Override
    public boolean deletePlan(Plan plan) {
        return planDao.deletePlan(plan);
    }

}
