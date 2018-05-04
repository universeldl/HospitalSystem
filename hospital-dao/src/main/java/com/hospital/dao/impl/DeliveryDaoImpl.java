package com.hospital.dao.impl;

import com.hospital.dao.DeliveryDao;
import com.hospital.domain.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import com.hospital.util.CalendarUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDaoImpl extends HibernateDaoSupport implements DeliveryDao {


    /**
     * @param hql传入的hql语句
     * @param pageCode当前页
     * @param pageSize每页显示大小
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
    public PageBean<Integer> getDeliveryIdList(int surveyId, int pageCode, int pageSize, Patient patient) {
        PageBean<Integer> pb = new PageBean<Integer>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数

        List<Integer> integers = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "select count(*) from deliveryInfo di,survey bk where bk.surveyId=di.surveyId and di.patientId="+patient.getPatientId();
        //不支持limit分页
        String hql = "select di.deliveryId from deliveryInfo di,survey bk where bk.surveyId=di.surveyId and di.patientId="+patient.getPatientId();
        sb.append(hql);
        sb_sql.append(sql);
        if (surveyId > 0) {
            sb.append(" and bk.surveyId=" + surveyId);
            sb_sql.append(" and bk.surveyId=" + surveyId);
        }

        try {
            NativeQuery createSQLQuery1 = this.getSessionFactory().getCurrentSession().createNativeQuery(sb_sql.toString());
            List list = createSQLQuery1.list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            //分页查询
            List list2 = doLimitDeliveryInfo(sb.toString(), pageCode, pageSize);
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


    @Override
    public PageBean<Integer> getDeliveryIdList(String name, int deliveryId, int pageCode, int pageSize, Doctor doctor) {
        PageBean<Integer> pb = new PageBean<Integer>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数

        List<Integer> integers = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "select count(*) from deliveryInfo bo,survey bk,Patient r "
                + "where bk.surveyId=bo.surveyId and bo.patientId=r.patientId ";
        //不支持limit分页
        String hql = "select bo.deliveryId from deliveryInfo bo,survey bk,Patient r "
                + "where bk.surveyId=bo.surveyId and bo.patientId=r.patientId ";
        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.aid=" + doctor.getAid() + " or r.addnDoctorId=" + doctor.getAid() + ")";
            sql = sql + " and (r.aid=" + doctor.getAid() + " or r.addnDoctorId=" + doctor.getAid() + ")";
        }
        sb.append(hql);
        sb_sql.append(sql);
        if (!"".equals(name.trim())) {
            sb.append(" and r.name like '%" + name + "%'");
            sb_sql.append(" and r.name like '%" + name + "%'");
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
            List list2 = doLimitDeliveryInfo(sb.toString(), pageCode, pageSize);
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


    @Override
    public PageBean<Integer> getDeliveryIdList(int queryType, int pageCode, int pageSize, Doctor doctor) {
        PageBean<Integer> pb = new PageBean<Integer>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数

        List<Integer> integers = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "select count(*) from deliveryInfo bo,survey bk,Patient r "
                + "where bk.surveyId=bo.surveyId and bo.patientId=r.patientId and " +
                "(bo.deliveryId not in (select deliveryId from RetrieveInfo))";
        //不支持limit分页
        String hql = "select bo.deliveryId from deliveryInfo bo,survey bk,Patient r "
                + "where bk.surveyId=bo.surveyId and bo.patientId=r.patientId and " +
                "(bo.deliveryId not in (select deliveryId from RetrieveInfo))";
        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.aid=" + doctor.getAid() + " or r.addnDoctorId=" + doctor.getAid() + ")";
            sql = sql + " and (r.aid=" + doctor.getAid() + " or r.addnDoctorId=" + doctor.getAid() + ")";
        }
        if (queryType == 1) {//本月未答
            String firstDayOfMonth = CalendarUtils.getFirstDayOfMonth();
            String currentDay = CalendarUtils.getNowTime();
            hql = hql + "and  bo.deliveryDate between \"" + firstDayOfMonth + "\" and \"" + currentDay + "\"";
            sql = sql + "and  bo.deliveryDate between \"" + firstDayOfMonth + "\" and \"" + currentDay + "\"";
        }
        else if (queryType == 2) {//近30天未答
            String daysAgo = CalendarUtils.getModifyNumDaysAgo("", 30);//date of 30 days ago
            String currentDay = CalendarUtils.getNowTime();
            hql = hql + "and  bo.deliveryDate between \"" + daysAgo + "\" and \"" + currentDay + "\"";
            sql = sql + "and  bo.deliveryDate between \"" + daysAgo + "\" and \"" + currentDay + "\"";
        }
        hql = hql + " ORDER BY bo.deliveryId";
        sb.append(hql);
        sb_sql.append(sql);

        try {
            NativeQuery createSQLQuery1 = this.getSessionFactory().getCurrentSession().createNativeQuery(sb_sql.toString());
            List list = createSQLQuery1.list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            //分页查询
            List list2 = doLimitDeliveryInfo(sb.toString(), pageCode, pageSize);
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


    public List doLimitDeliveryInfo(final String hql, final int pageCode, final int pageSize) {
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


    public List doSplitPage(final String hql, final int pageCode, final int pageSize, final int aid1, final int aid2) {
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException {
                //创建query对象
                Query query = session.createQuery(hql);
                query.setParameter("aid1", aid1);
                query.setParameter("aid2", aid2);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode - 1) * pageSize).setMaxResults(pageSize).list();

            }

        });

    }


    @Override
    public PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Doctor doctor) {
        PageBean<DeliveryInfo> pb = new PageBean<DeliveryInfo>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List deliveryInfoList = null;
        try {
            String sql = "SELECT count(deliveryId) from DeliveryInfo";
            int totalRecord = 0;
            //如果是super，全选，否则做判断
            if ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() == 1)) {
                List list = this.getHibernateTemplate().find(sql);
                if (list != null && list.size() > 0) {
                    totalRecord = ((Long)list.get(0)).intValue();
                }
                pb.setTotalRecord(totalRecord);    //设置总记录数
                //this.getSessionFactory().getCurrentSession().close();

                //不支持limit分页
                String hql = "from DeliveryInfo";
                //分页查询
                deliveryInfoList = doSplitPage(hql, pageCode, pageSize);
            } else {
                //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
                String addSql = " r where (r.patient.doctor.aid=? or r.patient.addnDoctor.aid=?)";
                sql += addSql;
                List list = this.getHibernateTemplate().find(sql, doctor.getAid(), doctor.getAid());
                if (list != null && list.size() > 0) {
                    totalRecord = ((Long)list.get(0)).intValue();
                }
                pb.setTotalRecord(totalRecord);    //设置总记录数
                //this.getSessionFactory().getCurrentSession().close();

                //不支持limit分页
                String hql = "from DeliveryInfo r where (r.patient.doctor.aid=:aid1 or r.patient.addnDoctor.aid=:aid2)";
                //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人;把当前医生传进来，如果是super，全选，否则做前面的判断
                //分页查询
                deliveryInfoList = doSplitPage(hql, pageCode, pageSize, doctor.getAid(), doctor.getAid());
            }
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        if (deliveryInfoList != null && deliveryInfoList.size() > 0) {
            pb.setBeanList(deliveryInfoList);
            return pb;
        }
        return null;
    }


    @Override
    public PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Patient patient) {
        PageBean<DeliveryInfo> pb = new PageBean<DeliveryInfo>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List deliveryInfoList = null;
        try {
            String sql = "from DeliveryInfo d where d.patient.patientId=?";
            int totalRecord = 0;
            List list = this.getHibernateTemplate().find(sql, patient.getPatientId());
            if (list != null && list.size() > 0) {
                totalRecord = list.size();
            }
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            String hql = "from DeliveryInfo d where d.patient.patientId=" + patient.getPatientId();
            //分页查询
            deliveryInfoList = doSplitPage(hql, pageCode, pageSize);
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        if (deliveryInfoList != null && deliveryInfoList.size() > 0) {
            pb.setBeanList(deliveryInfoList);
            return pb;
        }
        return null;
    }


    @Override
    public List<DeliveryInfo> getUnansweredDeliveryInfos(Integer patientId) {
        String hql = "from DeliveryInfo b where b.state>-1 and b.patient.patientId=?";
        List list = this.getHibernateTemplate().find(hql, patientId);
        return list;
    }


    @Override
    public DeliveryInfo getDeliveryInfoById(DeliveryInfo info) {
        String hql = "from DeliveryInfo b where b.deliveryId=?";
        List list = this.getHibernateTemplate().find(hql, info.getDeliveryId());
        if (list != null && list.size() > 0) {
            return (DeliveryInfo) list.get(0);
        }
        return null;
    }


    @Override
    public List<DeliveryInfo> getDeliveryInfosByPatientId(Patient patient) {
        String hql = "from DeliveryInfo b where b.patient.patientId=?";
        List list = this.getHibernateTemplate().find(hql, patient.getPatientId());
        return list;
    }


    @Override
    public int addDelivery(DeliveryInfo info) {
        Integer integer = 0;
        try {
            this.getHibernateTemplate().clear();
            //save方法返回的是Serializable接口，该结果的值就是你插入到数据库后新记录的主键值
            Serializable save = this.getHibernateTemplate().save(info);
            this.getHibernateTemplate().flush();
            integer = (Integer) save;
        } catch (Throwable e1) {
            integer = 0;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return integer;
    }


    @Override
    public List<DeliveryInfo> getNoRetrieveDeliveryInfoByPatient(Patient patient) {
        String hql = "from DeliveryInfo b where b.state>=0 and b.patient.patientId=? ";
        List list = null;
        try {
            list = this.getHibernateTemplate().find(hql, patient.getPatientId());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    @Override
    public DeliveryInfo updateDeliveryInfo(DeliveryInfo deliveryInfoById) {
        DeliveryInfo deliveryInfo = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            deliveryInfo = (DeliveryInfo) this.getHibernateTemplate().merge(deliveryInfoById);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return deliveryInfo;
    }


    @Override
    public List<DeliveryInfo> getDeliveryInfoByNoRetrieveState() {
        String hql = "from DeliveryInfo b where b.state>=0";
        List<DeliveryInfo> list = null;
        try {
            list = (List<DeliveryInfo>) this.getHibernateTemplate().find(hql);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    @Override
    public List<DeliveryInfo> getDeliveryInfoBySurvey(Survey survey) {
        String hql = "from DeliveryInfo b where b.survey.surveyId=?";
        List<DeliveryInfo> list = null;
        try {
            list = (List<DeliveryInfo>) this.getHibernateTemplate().find(hql, survey.getSurveyId());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


}
