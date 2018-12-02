package com.hospital.dao.impl;

import com.hospital.dao.RetrieveDao;
import com.hospital.domain.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RetrieveDaoImpl extends HibernateDaoSupport implements RetrieveDao {


    /**
     * @param hql
     * @param pageCode
     * @param pageSize
     * @return
     */
    public List doSplitPage(final String hql, final int pageCode, final int pageSize) {
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException {
                //创建query对象
                Query query = session.createQuery(hql);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode - 1) * pageSize).setMaxResults(pageSize).list();

            }

        });

    }


    @Override
    public int addRetrieve(RetrieveInfo retrieveInfo) {
        int b = 1;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(retrieveInfo);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = 0;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }

    @Override
    public PageBean<RetrieveInfo> findRetrieveInfoByPage(int pageCode, int pageSize) {
        PageBean<RetrieveInfo> pb = new PageBean<RetrieveInfo>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List retrieveInfoList = null;
        try {
            String sql = "SELECT count(*) FROM RetrieveInfo";
            List list = this.getSessionFactory().getCurrentSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            String hql = "from RetrieveInfo";
            //分页查询
            retrieveInfoList = doSplitPage(hql, pageCode, pageSize);
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        if (retrieveInfoList != null && retrieveInfoList.size() > 0) {
            pb.setBeanList(retrieveInfoList);
            return pb;
        }
        return null;
    }


    @Override
    public RetrieveInfo getRetrieveInfoById(RetrieveInfo retrieveInfo) {
        String hql = "from RetrieveInfo b where b.deliveryId=?";
        List list = this.getHibernateTemplate().find(hql, retrieveInfo.getDeliveryId());
        if (list != null && list.size() > 0) {
            return (RetrieveInfo) list.get(0);
        }
        return null;
    }

    @Override
    public Set<Answer> getAnswerByDeliveryId(RetrieveInfo retrieveInfo) {
        String hql = "from RetrieveInfo b where b.deliveryId=?";
        List list = this.getHibernateTemplate().find(hql, retrieveInfo.getDeliveryId());
        if (list != null && list.size() > 0) {
            RetrieveInfo r = (RetrieveInfo) list.get(0);
            Set<Answer> answers = r.getAnswers();
            return answers;
        }
        return null;
    }


    @Override
    public PageBean<Integer> getDeliveryIdList(String openID, int deliveryId, int pageCode, int pageSize) {
        PageBean<Integer> pb = new PageBean<Integer>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数

        List<Integer> integers = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "select count(*) from RetrieveInfo ba ,deliveryInfo bo,survey bk,Patient r "
                + "where ba.deliveryId=bo.deliveryId and bk.surveyId=bo.surveyId and bo.patientId=r.patientId ";
        //不支持limit分页
        String hql = "select ba.deliveryId from RetrieveInfo ba ,deliveryInfo bo,survey bk,Patient r "
                + "where ba.deliveryId=bo.deliveryId and bk.surveyId=bo.surveyId and bo.patientId=r.patientId ";
        sb.append(hql);
        sb_sql.append(sql);
        if (!"".equals(openID.trim())) {
            sb.append(" and r.openID like '%" + openID + "%'");
            sb_sql.append(" and r.openID like '%" + openID + "%'");
        }
        if (deliveryId != 0) {
            sb.append(" and bo.deliveryId like '%" + deliveryId + "%'");
            sb_sql.append(" and bo.deliveryId like '%" + deliveryId + "%'");
        }

        try {
            NativeQuery createSQLQuery1 = this.getSessionFactory().getCurrentSession().createNativeQuery(sb_sql.toString());
            List list = createSQLQuery1.list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            //分页查询
            List list2 = doLimitRetrieveInfo(sb.toString(), pageCode, pageSize);
            for (Object object : list2) {
                Integer i = new Integer(object.toString());
                integers.add(i);
            }
            pb.setBeanList(integers);
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }


        return pb;
    }


    public List doLimitRetrieveInfo(final String hql, final int pageCode, final int pageSize) {
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException {
                //创建query对象
                NativeQuery query = session.createNativeQuery(hql);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode - 1) * pageSize).setMaxResults(pageSize).list();

            }

        });

    }


    @Override
    public RetrieveInfo updateRetrieveInfo(RetrieveInfo retrieveInfoById) {
        RetrieveInfo retrieveInfo = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            retrieveInfo = (RetrieveInfo) this.getHibernateTemplate().merge(retrieveInfoById);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return retrieveInfo;
    }


    @Override
    public boolean deleteRetrieveInfo(RetrieveInfo retrieveInfo) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(retrieveInfo);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }

    @Override
    public Integer getSurveyCurNum(Survey survey) {
        String sql = "select count(*) from RetrieveInfo d where d.survey.surveyId=?";
        Integer totalRecord = 0;
        try {
            List list = this.getHibernateTemplate().find(sql, survey.getSurveyId());
            if (list != null && list.size() > 0) {
                totalRecord = ((Long) list.get(0)).intValue();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return totalRecord;
    }

    @Override
    public List<RetrieveInfo> getRetrieveBySurveyId(Integer patient, Integer id) {
        String sql = "from RetrieveInfo d where d.survey.surveyId=? and d.patient.patientId=?";
        try {
            List list = this.getHibernateTemplate().find(sql, id, patient);
            return list;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public RetrieveInfo getRetrieveInfoByDeliveryID(Integer deliveryID) {
        String hql = "from RetrieveInfo b where b.deliveryInfo.deliveryId=?";
        List list = this.getHibernateTemplate().find(hql, deliveryID);
        if (list != null && list.size() > 0) {
            RetrieveInfo r = (RetrieveInfo) list.get(0);
            return r;
        }
        return null;
    }

}
