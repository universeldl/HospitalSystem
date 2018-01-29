package com.hospital.dao.impl;

import com.hospital.dao.PlanDao;
import com.hospital.domain.Plan;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

public class PlanDaoImpl extends HibernateDaoSupport implements PlanDao {

    @Override
    public List<Plan> getAllPlan() {

        String hql = "from Plan";
        List list = this.getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;

    }

    @Override
    public Plan getPlanById(Plan plan) {
        String hql = "from Plan r where r.planId=?";
        List list = this.getHibernateTemplate().find(hql, plan.getPlanId());
        if (list != null && list.size() > 0) {
            return (Plan) list.get(0);
        }
        return null;
    }

    @Override
    public Plan getPlan(Plan plan) {
        String hql = "from Plan r where r.beginAge<=? and r.endAge>? and r.sex=? and r.patientType.patientTypeId=?";
        List list = this.getHibernateTemplate().find(hql, plan.getBeginAge(), plan.getEndAge(), plan.getSex(), plan.getPatientType().getPatientTypeId());
        if (list != null && list.size() > 0) {
            return (Plan) list.get(0);
        }
        return null;
    }

    @Override
    public Plan updatePlan(Plan updatePlan) {
        Plan newPlan = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            newPlan = (Plan) this.getHibernateTemplate().merge(updatePlan);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return newPlan;
    }

    @Override
    public boolean addPlan(Plan plan) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(plan);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }

    @Override
    public boolean deletePlan(Plan plan) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(plan);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }
}
