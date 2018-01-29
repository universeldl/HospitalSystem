package com.hospital.dao;

import com.hospital.domain.Plan;

import java.util.List;

public interface PlanDao {

    List<Plan> getAllPlan();

    Plan getPlanById(Plan plan);

    Plan getPlan(Plan plan);

    Plan updatePlan(Plan updatePlan);

    boolean addPlan(Plan plan);

    boolean deletePlan(Plan plan);
}
