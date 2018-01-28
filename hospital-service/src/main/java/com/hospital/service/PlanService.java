package com.hospital.service;

import com.hospital.domain.Plan;

import java.util.List;

public interface PlanService {

    List<Plan> getAllPlan();

    Plan getPlanById(Plan plan);

    Plan updatePlan(Plan updatePlan);

    boolean addPlan(Plan plan);

    boolean deletePlan(Plan plan);

}
