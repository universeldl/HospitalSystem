package com.hospital.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.hospital.dao.SurveyTypeDao;
import com.hospital.domain.Survey;
import com.hospital.domain.SurveyType;
import com.hospital.domain.PageBean;

public class SurveyTypeDaoImpl extends HibernateDaoSupport implements SurveyTypeDao{
	
	
	@Override
	public List<SurveyType> getAllSurveyTypes() {
		String hql= "from SurveyType";
		List list = this.getHibernateTemplate().find(hql);
		return list;
	}

	@Override
	public SurveyType getSurveyType(SurveyType surveyType) {
		String hql= "from SurveyType b where b.typeId=?";
		List list = this.getHibernateTemplate().find(hql, surveyType.getTypeId());
		if(list!=null && list.size()>0){
			return (SurveyType) list.get(0);
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
                    throws HibernateException{
                //创建query对象
                Query query=session.createQuery(hql);
                //返回其执行了分布方法的list
                return query.setFirstResult((pageCode-1)*pageSize).setMaxResults(pageSize).list();
                 
            }
             
        });
         
    }
	

	@Override
	public PageBean<SurveyType> findSurveyTypeByPage(int pageCode, int pageSize) {
		PageBean<SurveyType> pb = new PageBean<SurveyType>();	//pageBean对象，用于分页
		//根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
		pb.setPageCode(pageCode);//设置当前页码
		pb.setPageSize(pageSize);//设置页面记录数
		List surveyTypeList = null;
		try {
			String sql = "SELECT count(*) FROM SurveyType";
			List list = this.getSessionFactory().getCurrentSession().createQuery(sql).list();
			int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
			pb.setTotalRecord(totalRecord);	//设置总记录数
			//this.getSessionFactory().getCurrentSession().close();
			
			//不支持limit分页
			String hql= "from SurveyType";
			//分页查询
			surveyTypeList = doSplitPage(hql,pageCode,pageSize);
			
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		if(surveyTypeList!=null && surveyTypeList.size()>0){
			pb.setBeanList(surveyTypeList);
			return pb;
		}
		return null;
	}


	@Override
	public SurveyType getSurveyTypeByName(SurveyType surveyType) {
		String hql= "from SurveyType b where b.typeName=?";
		List list = this.getHibernateTemplate().find(hql, surveyType.getTypeName());
		if(list!=null && list.size()>0){
			return (SurveyType) list.get(0);
		}
		return null;
	}


	@Override
	public boolean addSurveyType(SurveyType surveyType) {
		boolean b = true;
		try{
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().save(surveyType);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}


	@Override
	public SurveyType getSurveyTypeById(SurveyType surveyType) {
		String hql= "from SurveyType b where b.typeId=?";
		List list = this.getHibernateTemplate().find(hql, surveyType.getTypeId());
		if(list!=null && list.size()>0){
			return (SurveyType) list.get(0);
		}
		return null;
	}


	@Override
	public SurveyType updateSurveyTypeInfo(SurveyType surveyType) {
		SurveyType newSurveyType = null;
		try{
			this.getHibernateTemplate().clear();
			//将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
			newSurveyType = (SurveyType) this.getHibernateTemplate().merge(surveyType);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return newSurveyType;
	}


	@Override
	public boolean deleteSurveyType(SurveyType surveyType) {
		boolean b = true;
		try{
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().delete(surveyType);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}


	@Override
	public PageBean<SurveyType> querySurveyType(SurveyType surveyType, int pageCode,
			int pageSize) {
		PageBean<SurveyType> pb = new PageBean<SurveyType>();	//pageBean对象，用于分页
		//根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
		pb.setPageCode(pageCode);//设置当前页码
		pb.setPageSize(pageSize);//设置页面记录数
		
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_sql = new StringBuilder();
		String sql = "SELECT count(*) FROM SurveyType b where 1=1";
		String hql= "from SurveyType b where 1=1";
		sb.append(hql);
		sb_sql.append(sql);
		if(!"".equals(surveyType.getTypeName().trim())){
			sb.append(" and b.typeName like '%" + surveyType.getTypeName() +"%'");
			sb_sql.append(" and b.typeName like '%" + surveyType.getTypeName() +"%'");
		}
		
		try{
			
			List list = this.getSessionFactory().getCurrentSession().createQuery(sb_sql.toString()).list();
			int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
			pb.setTotalRecord(totalRecord);	//设置总记录数
			//this.getSessionFactory().getCurrentSession().close();
			
			
			List<SurveyType> surveyTypeList = doSplitPage(sb.toString(),pageCode,pageSize);
			if(surveyTypeList!=null && surveyTypeList.size()>0){
				pb.setBeanList(surveyTypeList);
				return pb;
			}
		}catch (Throwable e1){
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return null;
	}
	


	
	@Override
	public Survey getSurveyById(Survey survey) {
		String hql= "from Survey b where b.surveyId=?";
		List list = this.getHibernateTemplate().find(hql, survey.getSurveyId());
		if(list!=null && list.size()>0){
			return (Survey) list.get(0);
		}
		return null;
	}

}
