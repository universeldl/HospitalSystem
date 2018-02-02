package com.hospital.dao.impl;

import com.hospital.dao.PatientDao;
import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

public class PatientDaoImpl extends HibernateDaoSupport implements PatientDao {

    @Override
    public List<Patient> getPatientsByDoctor(Doctor doctor) {

        String hql = "from Patient r where r.doctor.aid=?";
        List list = this.getHibernateTemplate().find(hql, doctor.getAid());
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;

    }

    @Override
    public Patient getPatient(Patient patient) {
        //this.getHibernateTemplate().find(hql, value)方法无法执行的问题
        //解决需要catch (Throwable e)
        String hql = "from Patient r where r.patientId=?";
        try {
            List list = this.getHibernateTemplate().find(hql, patient.getPatientId());
            if (list != null && list.size() > 0) {
                return (Patient) list.get(0);
            }
        } catch (Throwable e1) {
            throw new RuntimeException(e1.getMessage());
        }

        return null;

//		Patient newPatient = (Patient) this.getSession().get(Patient.class, patient.getPatientId());
//		this.getSession().close();
//		return newPatient;
    }

    @Override
    public Integer[] getAdditionsForLast12Months() {
        Integer[] additions = new Integer[12];

        String hql = "from Patient r where period_diff(date_format(now(), '%Y%m'), date_format(r.createTime, '%Y%m')) =?";
        try {
            for(int i=11; i>=0; i--) {
                List list = this.getHibernateTemplate().find(hql, i);
                additions[11-i] = list.size();
            }
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return additions;
    }

    @Override
    public Patient updatePatientInfo(Patient patient) {
        Patient newPatient = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            newPatient = (Patient) this.getHibernateTemplate().merge(patient);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return newPatient;
    }


    @Override
    public boolean addPatient(Patient patient) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(patient);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }


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
    public PageBean<Patient> findPatientByPage(int pageCode, int pageSize) {
        PageBean<Patient> pb = new PageBean<Patient>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List patientList = null;
        try {
            String sql = "SELECT count(*) FROM Patient";
            List list = this.getSessionFactory().getCurrentSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数

            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            String hql = "from Patient";
            //分页查询
            patientList = doSplitPage(hql, pageCode, pageSize);


        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        if (patientList != null && patientList.size() > 0) {
            pb.setBeanList(patientList);
            return pb;
        }
        return null;
    }


    @Override
    public Patient getPatientById(Patient patient) {
        String hql = "from Patient r where r.patientId=?";
        List list = this.getHibernateTemplate().find(hql, patient.getPatientId());
        if (list != null && list.size() > 0) {
            return (Patient) list.get(0);
        }
        return null;
    }


    @Override
    public boolean deletePatient(Patient patient) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(patient);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }


    @Override
    public PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize) {
        PageBean<Patient> pb = new PageBean<Patient>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数


        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "SELECT count(*) FROM Patient r where 1=1";
        String hql = "from Patient r where 1=1";
        sb.append(hql);
        sb_sql.append(sql);
        if (!"".equals(patient.getOpenID().trim())) {
            sb.append(" and r.openID like '%" + patient.getOpenID() + "%'");
            sb_sql.append(" and r.openID like '%" + patient.getOpenID() + "%'");
        }
        if (!"".equals(patient.getName().trim())) {
            sb.append(" and r.name like '%" + patient.getName() + "%'");
            sb_sql.append(" and r.name like '%" + patient.getName() + "%'");
        }
        if (patient.getPatientType().getPatientTypeId() != -1) {
            sb.append(" and r.patientType=" + patient.getPatientType().getPatientTypeId());
            sb_sql.append(" and r.patientType=" + patient.getPatientType().getPatientTypeId());
        }
        try {

            List list = this.getSessionFactory().getCurrentSession().createQuery(sb_sql.toString()).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();


            List<Patient> doctorList = doSplitPage(sb.toString(), pageCode, pageSize);
            if (doctorList != null && doctorList.size() > 0) {
                pb.setBeanList(doctorList);
                return pb;
            }
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return null;
    }


    @Override
    public Patient getPatientByopenID(Patient patient) {
        String hql = "from Patient r where r.openID=?";
        List list = this.getHibernateTemplate().find(hql, patient.getOpenID());
        if (list != null && list.size() > 0) {
            return (Patient) list.get(0);
        }
        return null;
    }


    @Override
    public int batchAddPatient(List<Patient> patients, List<Patient> failPatients) {
        int success = 0;
        for (int i = 0; i < patients.size(); i++) {
            try {
                this.getHibernateTemplate().clear();
                this.getHibernateTemplate().save(patients.get(i));
                this.getHibernateTemplate().flush();
                success++;
            } catch (Throwable e1) {
                this.getHibernateTemplate().clear();
                patients.get(i).setOpenID(patients.get(i).getOpenID() + "(可能已存在该病人)");
                failPatients.add(patients.get(i));
                continue;

            }
        }
        return success;
    }


    @Override
    public List<Patient> findAllPatients() {
        String hql = "from Patient ";
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }


}
