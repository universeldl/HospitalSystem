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

import java.util.Date;
import java.util.List;

public class PatientDaoImpl extends HibernateDaoSupport implements PatientDao {

    @Override
    public List<Patient> getPatientsByDoctor(Doctor doctor) {

        String hql = "from Patient";
        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " r where r.state>0 and r.doctor.aid=" + doctor.getAid();
        }
        List list = this.getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;

    }

    @Override
    public List<Integer> getPatientSexByDoctor(Doctor doctor) {

        String hql = "";
        if(doctor.getAccessibleHospitals().size() != 0) {
            hql = hql + "select r.sex from Patient r, Doctor d inner join d.accessibleHospitals as ah where r.state>0";
        }
        else {
            hql = hql + "select r.sex from Patient r where r.state>0";
        }
        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                hql = hql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + ")) GROUP BY r.patientId";
            }
            else {
                hql = hql + ")";
            }
        }
        List list = this.getHibernateTemplate().find(hql);
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
    public Integer[] getAdditionsForLast12Months(Doctor doctor) {
        Integer[] additions = new Integer[12];

        String hql = "";
        if(doctor.getAccessibleHospitals().size() != 0) {
            hql = hql + "select count(*) from Patient r, Doctor d inner join d.accessibleHospitals as ah where period_diff(date_format(now(), '%Y%m'), date_format(r.createTime, '%Y%m')) =? and r.state>0 ";
        }
        else {
            hql = hql + "select count(*) from Patient r where period_diff(date_format(now(), '%Y%m'), date_format(r.createTime, '%Y%m')) =? and r.state>0 ";
        }
        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                hql = hql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + "))";
            }
            else {
                hql = hql + ")";
            }
        }
        try {
            for (int i = 11; i >= 0; i--) {
                List list = this.getHibernateTemplate().find(hql, i);
                additions[11 - i] =  ((Long) list.get(0)).intValue();
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

    /**
     * @param hql传入的hql语句
     * @param pageCode当前页
     * @param pageSize每页显示大小
     * @return
     */
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


    /**
     * @param hql传入的hql语句
     * @param pageCode当前页
     * @param pageSize每页显示大小
     * @return
     */
    public List doSplitPage(final String hql, final int pageCode, final int pageSize, final int aid1, final int aid2, final int aid3) {
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback() {
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException {
                //创建query对象
                Query query = session.createQuery(hql);
                query.setParameter("aid1", aid1);
                query.setParameter("aid2", aid2);
                query.setParameter("aid3", aid3);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode - 1) * pageSize).setMaxResults(pageSize).list();

            }

        });

    }

    @Override
    public PageBean<Patient> findPatientByPage(int pageCode, int pageSize, Doctor doctor) {
        PageBean<Patient> pb = new PageBean<Patient>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List patientList = null;
        try {
            Long totalRecord = null;
            //如果是super，全选，否则做判断
            if ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() == 1)) {
                String sql = "select count(patientId) from Patient r where r.state>0";
                List list = this.getHibernateTemplate().find(sql);
                if (list != null && list.size() > 0) {
                    totalRecord = (Long) list.get(0);
                }
                pb.setTotalRecord(totalRecord.intValue());    //设置总记录数

                //不支持limit分页
                String hql = "from Patient r where r.state>0 ORDER BY createTime DESC";
                //分页查询
                patientList = doSplitPage(hql, pageCode, pageSize);
            } else {
                String sql = "";
                if(doctor.getAccessibleHospitals().size() != 0) {
                    sql = sql + "select count(DISTINCT patientId) from Patient p, Doctor d inner join d.accessibleHospitals as ah where p.state>0";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
                    String addSql = " and (p.doctor.aid=? or p.addnDoctor.aid=? or (ah.hospitalId=p.doctor.hospital.hospitalId and d.aid=?))";
                    sql += addSql;
                    List list = this.getHibernateTemplate().find(sql, doctor.getAid(), doctor.getAid(), doctor.getAid());
                    if (list != null && list.size() > 0) {
                        totalRecord = (Long) list.get(0);
                    }
                }
                else {
                    sql = sql + "select count(DISTINCT patientId) from Patient p where p.state>0";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
                    String addSql = " and (p.doctor.aid=? or p.addnDoctor.aid=?)";
                    sql += addSql;
                    List list = this.getHibernateTemplate().find(sql, doctor.getAid(), doctor.getAid());
                    if (list != null && list.size() > 0) {
                        totalRecord = (Long) list.get(0);
                    }
                }
                pb.setTotalRecord(totalRecord.intValue());    //设置总记录数

                //不支持limit分页
                String hql = "";
                if(doctor.getAccessibleHospitals().size() != 0) {
                    hql = hql + "select p from Patient p, Doctor d inner join d.accessibleHospitals as ah where (p.state>0 and (p.doctor.aid=:aid1 or p.addnDoctor.aid=:aid2 or (ah.hospitalId=p.doctor.hospital.hospitalId and d.aid=:aid3)))" + " GROUP BY p.patientId ORDER BY createTime DESC";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人;把当前医生传进来，如果是super，全选，否则做前面的判断
                    //分页查询
                    patientList = doSplitPage(hql, pageCode, pageSize, doctor.getAid(), doctor.getAid(), doctor.getAid());
                }
                else {
                    hql = hql + "select p from Patient p where (p.state>0 and (p.doctor.aid=:aid1 or p.addnDoctor.aid=:aid2))" + " ORDER BY createTime DESC";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人;把当前医生传进来，如果是super，全选，否则做前面的判断
                    //分页查询
                    patientList = doSplitPage(hql, pageCode, pageSize, doctor.getAid(), doctor.getAid());
                }
            }


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
    public PageBean<Patient> findRecyclePatientByPage(int pageCode, int pageSize, Doctor doctor) {
        PageBean<Patient> pb = new PageBean<Patient>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List patientList = null;
        try {
            Long totalRecord = null;
            //如果是super，全选，否则做判断
            if ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() == 1)) {
                String sql = "select count(patientId) from Patient r where r.state=-1";
                List list = this.getHibernateTemplate().find(sql);
                if (list != null && list.size() > 0) {
                    totalRecord = (Long) list.get(0);
                }
                pb.setTotalRecord(totalRecord.intValue());    //设置总记录数

                //不支持limit分页
                String hql = "from Patient r where r.state=-1 ORDER BY createTime DESC";
                //分页查询
                patientList = doSplitPage(hql, pageCode, pageSize);
            } else {
                String sql = "";
                if(doctor.getAccessibleHospitals().size() != 0) {
                    sql = sql + "select count(DISTINCT patientId) from Patient p, Doctor d inner join d.accessibleHospitals as ah where p.state=-1";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
                    String addSql = " and (p.doctor.aid=? or p.addnDoctor.aid=? or (ah.hospitalId=p.doctor.hospital.hospitalId and d.aid=?))";
                    sql += addSql;
                    List list = this.getHibernateTemplate().find(sql, doctor.getAid(), doctor.getAid(), doctor.getAid());
                    if (list != null && list.size() > 0) {
                        totalRecord = (Long) list.get(0);
                    }
                }
                else {
                    sql = sql + "select count(DISTINCT patientId) from Patient p where p.state=-1";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
                    String addSql = " and (p.doctor.aid=? or p.addnDoctor.aid=?)";
                    sql += addSql;
                    List list = this.getHibernateTemplate().find(sql, doctor.getAid(), doctor.getAid());
                    if (list != null && list.size() > 0) {
                        totalRecord = (Long) list.get(0);
                    }
                }
                pb.setTotalRecord(totalRecord.intValue());    //设置总记录数

                //不支持limit分页
                String hql = "";
                if(doctor.getAccessibleHospitals().size() != 0) {
                    hql = hql + "select p from Patient p, Doctor d inner join d.accessibleHospitals as ah where (p.state=-1 and (p.doctor.aid=:aid1 or p.addnDoctor.aid=:aid2 or (ah.hospitalId=p.doctor.hospital.hospitalId and d.aid=:aid3)))" + " GROUP BY p.patientId ORDER BY createTime DESC";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人;把当前医生传进来，如果是super，全选，否则做前面的判断
                    //分页查询
                    patientList = doSplitPage(hql, pageCode, pageSize, doctor.getAid(), doctor.getAid(), doctor.getAid());
                }
                else {
                    hql = hql + "select p from Patient p where (p.state=-1 and (p.doctor.aid=:aid1 or p.addnDoctor.aid=:aid2))" + " ORDER BY createTime DESC";
                    //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人;把当前医生传进来，如果是super，全选，否则做前面的判断
                    //分页查询
                    patientList = doSplitPage(hql, pageCode, pageSize, doctor.getAid(), doctor.getAid());
                }
            }


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


    //TODO auth of doctor
    @Override
    public PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize, Doctor doctor,
                                          Integer hospitalId, Integer cityId, Integer provinceId) {
        PageBean<Patient> pb = new PageBean<Patient>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数


        String sql = "";
        String hql = "";
        if(doctor.getAccessibleHospitals().size() != 0) {
            hql = hql + "select r from Patient r, Doctor d inner join d.accessibleHospitals as ah where 1=1 and r.state>0";
            sql = sql + "SELECT count(*) FROM Patient r, Doctor d inner join d.accessibleHospitals as ah where 1=1 and r.state>0";
        }
        else {
            hql = hql + "select r from Patient r where 1=1 and r.state>0";
            sql = sql + "SELECT count(*) FROM Patient r where 1=1 and r.state>0";
        }
        if (!"".equals(patient.getName().trim())) {
            hql += " and r.name like '%" + patient.getName() + "%'";
            sql += " and r.name like '%" + patient.getName() + "%'";
        }
        if (patient.getPatientType().getPatientTypeId() != -1) {
            hql += " and r.patientType=" + patient.getPatientType().getPatientTypeId();
            sql += " and r.patientType=" + patient.getPatientType().getPatientTypeId();
        }

        if (hospitalId != -1) {
            hql += " and r.doctor.hospital.hospitalId=" + hospitalId;
            sql += " and r.doctor.hospital.hospitalId=" + hospitalId;
        } else if (cityId != -1) {
            hql += " and r.doctor.hospital.city.cityId=" + cityId;
            sql += " and r.doctor.hospital.city.cityId=" + cityId;
        } else if (provinceId != -1) {
            hql += " and r.doctor.hospital.city.province.provinceId=" + provinceId;
            sql += " and r.doctor.hospital.city.province.provinceId=" + provinceId;
        }

        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                hql = hql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + ")) GROUP BY r.patientId";
            }
            else {
                hql = hql + ")";
            }
            sql = sql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                sql = sql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + "))";
            }
            else {
                sql = sql + ")";
            }
        }

        hql += " ORDER BY createTime DESC";
        try {
            int totalRecord = 0;
            List<Patient> patientList = null;
            List list = this.getHibernateTemplate().find(sql);
            Long total = (Long)list.get(0);
            totalRecord = total.intValue();
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();


            patientList = doSplitPage(hql, pageCode, pageSize);
            if (patientList != null && patientList.size() > 0) {
                pb.setBeanList(patientList);
                return pb;
            }
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return null;
    }

    @Override
    public PageBean<Patient> queryRecyclePatient(Patient patient, int pageCode, int pageSize, Doctor doctor) {
        PageBean<Patient> pb = new PageBean<Patient>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数


        String sql = "";
        String hql = "";
        if(doctor.getAccessibleHospitals().size() != 0) {
            hql = hql + "select r from Patient r, Doctor d inner join d.accessibleHospitals as ah where 1=1 and r.state=-1";
            sql = sql + "SELECT count(*) FROM Patient r, Doctor d inner join d.accessibleHospitals as ah where 1=1 and r.state=-1";
        }
        else {
            hql = hql + "select r from Patient r where 1=1 and r.state=-1";
            sql = sql + "SELECT count(*) FROM Patient r where 1=1 and r.state=-1";
        }
        if (!"".equals(patient.getOpenID().trim())) {
            hql += " and r.openID like '%" + patient.getOpenID() + "%'";
            sql += " and r.openID like '%" + patient.getOpenID() + "%'";
        }
        if (!"".equals(patient.getName().trim())) {
            hql += " and r.name like '%" + patient.getName() + "%'";
            sql += " and r.name like '%" + patient.getName() + "%'";
        }
        if (patient.getPatientType().getPatientTypeId() != -1) {
            hql += " and r.patientType=" + patient.getPatientType().getPatientTypeId();
            sql += " and r.patientType=" + patient.getPatientType().getPatientTypeId();
        }
        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                hql = hql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + ")) GROUP BY r.patientId";
            }
            else {
                hql = hql + ")";
            }
            sql = sql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                sql = sql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + "))";
            }
            else {
                sql = sql + ")";
            }
        }
        try {
            int totalRecord = 0;
            List<Patient> patientList = null;
            List list = this.getHibernateTemplate().find(sql);
            Long total = (Long)list.get(0);
            totalRecord = total.intValue();
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();


            patientList = doSplitPage(hql, pageCode, pageSize);
            if (patientList != null && patientList.size() > 0) {
                pb.setBeanList(patientList);
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

    @Override
    public List<Integer> findAllActivePatientIds() {
        String hql = "select patientId from Patient r where r.state>0";
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }


    @Override
    public List<Patient> findAllPatientsByDoctor(Doctor doctor) {
        String hql = "";
        if(doctor.getAccessibleHospitals().size() != 0) {
            hql = hql + "select r from Patient r, Doctor d inner join d.accessibleHospitals as ah where 1=1 and r.state>0";
        }
        else {
            hql = hql + "select r from Patient r where 1=1 and r.state>0";
        }
        //如果不是super
        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                hql = hql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + ")) GROUP BY r.patientId";
            }
            else {
                hql = hql + ")";
            }
        }
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }

    @Override
    public List<Integer> findPatientIdsByFilter(Doctor doctor, Integer sex, Integer oldPatient, Date start,
                                                Date end, Integer hospitalId, Integer cityId) {
        String hql = "";
        if(doctor.getAccessibleHospitals().size() != 0) {
            hql = hql + "select patientId from Patient r, Doctor d inner join d.accessibleHospitals as ah where r.state>0";
        }
        else {
            hql = hql + "select patientId from Patient r where r.state>0";
        }

        if ((doctor.getAuthorization().getSuperSet() == null) ||
                ((doctor.getAuthorization().getSuperSet() != null) && (doctor.getAuthorization().getSuperSet() != 1))) {
            //p.aid或addnDoctor.aid有任意一个匹配当前医生的aid就说明当前医生有权限查看该病人
            hql = hql + " and (r.doctor.aid=" + doctor.getAid() + " or r.addnDoctor.aid=" + doctor.getAid();
            if(doctor.getAccessibleHospitals().size() != 0) {
                hql = hql + " or (ah.hospitalId=r.doctor.hospital.hospitalId and d.aid=" + doctor.getAid() + ")) GROUP BY r.patientId";
            }
            else {
                hql = hql + ")";
            }
        }

        hql = hql + " and r.birthday >= :startTime and r.birthday <= :endTime";
        if (hospitalId != -1) {
            hql = hql + " and r.doctor.hospital.hospitalId = " + hospitalId;
        }
        if (cityId != -1) {
            hql = hql + " and r.doctor.hospital.city.cityId = " + cityId;
        }
        if (oldPatient != -1) {
            hql = hql + " and r.oldPatient = " + oldPatient;
        }
        String[] params = { "startTime", "endTime" };
        Object[] args = { start, end };

        List list = this.getHibernateTemplate().findByNamedParam(hql, params, args);

        return list;
    }

}
