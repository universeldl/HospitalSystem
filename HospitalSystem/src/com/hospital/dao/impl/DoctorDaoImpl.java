package com.hospital.dao.impl;

import com.hospital.dao.DoctorDao;
import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;


public class DoctorDaoImpl extends HibernateDaoSupport implements DoctorDao{

	@Override
	public Doctor getDoctorByUserName(Doctor doctor) {
		String hql= "from Doctor a where a.username=? and a.state=1";
		List list = this.getHibernateTemplate().find(hql, doctor.getUsername());
		if(list!=null && list.size()>0){
			return (Doctor) list.get(0);
		}
		return null;
	}

	
	@Override
	public Doctor updateDoctorInfo(Doctor doctor) {
		Doctor newDoctor = null;
		try{
			this.getHibernateTemplate().clear();
			//将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
			newDoctor = (Doctor) this.getHibernateTemplate().merge(doctor);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return newDoctor;
	}


	@Override
	public List<Doctor> getAllDoctors() {
		String hql= "from Doctor a where a.state=1";
		List<Doctor> list = null;
		try{
			list = this.getHibernateTemplate().find(hql);
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return list;
	}


	@Override
	public boolean addDoctor(Doctor doctor) {
		boolean b = true;
		try{
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().save(doctor);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}


	@Override
	public Doctor getDoctorById(Doctor doctor) {
		String hql= "from Doctor a where a.aid=? and a.state=1";
		List list = this.getHibernateTemplate().find(hql, doctor.getAid());
		if(list!=null && list.size()>0){
			return (Doctor) list.get(0);
		}
		return null;
	}


	@Override
	public PageBean<Doctor> findDoctorByPage(int pageCode, int pageSize) {
		PageBean<Doctor> pb = new PageBean<Doctor>();	//pageBean对象，用于分页
		//根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
		pb.setPageCode(pageCode);//设置当前页码
		pb.setPageSize(pageSize);//设置页面记录数
		List doctorList = null;
		try {
			String sql = "SELECT count(*) FROM Doctor a where a.state=1";
			List list = this.getSession().createQuery(sql).list();
			int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
			pb.setTotalRecord(totalRecord);	//设置总记录数
			this.getSession().close();
			
			//不支持limit分页
			String hql= "from Doctor a where a.state=1";
			//分页查询
			doctorList = doSplitPage(hql,pageCode,pageSize);
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		if(doctorList!=null && doctorList.size()>0){
			pb.setBeanList(doctorList);
			return pb;
		}
		return null;
	}

	
	
	/**
     * 
     * @param hql传入的hql语句
     * @param pageCode当前页
     * @param pageSize每页显示大小
     * @return
     */
    public List doSplitPage(final String hql,final int pageCode,final int pageSize){
        //调用模板的execute方法，参数是实现了HibernateCallback接口的匿名类，
        return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
            //重写其doInHibernate方法返回一个object对象，
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                //创建query对象
                Query query=session.createQuery(hql);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode-1)*pageSize).setMaxResults(pageSize).list();
                 
            }
             
        });
         
    }


    
	@Override
	public PageBean<Doctor> queryDoctor(Doctor doctor,int pageCode, int pageSize) {
		
		PageBean<Doctor> pb = new PageBean<Doctor>();	//pageBean对象，用于分页
		//根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
		pb.setPageCode(pageCode);//设置当前页码
		pb.setPageSize(pageSize);//设置页面记录数
		
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_sql = new StringBuilder();
		String sql = "SELECT count(*) FROM Doctor a where a.state=1 ";
		String hql= "from Doctor a where a.state=1 ";
		sb.append(hql);
		sb_sql.append(sql);
		if(!"".equals(doctor.getUsername().trim())){
			sb.append(" and a.username like '%" + doctor.getUsername() +"%'");
			sb_sql.append(" and a.username like '%" + doctor.getUsername() +"%'");
		}
		if(!"".equals(doctor.getName().trim())){
			sb.append(" and a.name like '%" + doctor.getName() +"%'");
			sb_sql.append(" and a.name like '%" + doctor.getName() +"%'");
		}
		try{
			
			List list = this.getSession().createQuery(sb_sql.toString()).list();
			int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
			pb.setTotalRecord(totalRecord);	//设置总记录数
			this.getSession().close();
			
			
			List<Doctor> doctorList = doSplitPage(sb.toString(),pageCode,pageSize);
			if(doctorList!=null && doctorList.size()>0){
				pb.setBeanList(doctorList);
				return pb;
			}
		}catch (Throwable e1){
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return null;
	}


	@Override
	public boolean deleteDoctor(Doctor doctor) {
		boolean b = true;
		try{
			doctor.setState(0);
			this.getHibernateTemplate().clear();
			//将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
			this.getHibernateTemplate().merge(doctor);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}
	
}
