package com.hospital.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.hospital.domain.Question;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.hospital.dao.SurveyDao;
import com.hospital.domain.Survey;
import com.hospital.domain.SurveyType;
import com.hospital.domain.PageBean;

public class SurveyDaoImpl extends HibernateDaoSupport implements SurveyDao{

	
	
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
	public PageBean<Survey> findSurveyByPage(int pageCode, int pageSize) {
		PageBean<Survey> pb = new PageBean<Survey>();	//pageBean对象，用于分页
		//根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
		pb.setPageCode(pageCode);//设置当前页码
		pb.setPageSize(pageSize);//设置页面记录数
		List surveyList = null;
		try {
			String sql = "SELECT count(*) FROM Survey";
			List list = this.getSession().createQuery(sql).list();
			int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
			pb.setTotalRecord(totalRecord);	//设置总记录数
			this.getSession().close();
			//不支持limit分页
			String hql= "from Survey";
			//分页查询
			surveyList = doSplitPage(hql,pageCode,pageSize);
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		if(surveyList!=null && surveyList.size()>0){
			pb.setBeanList(surveyList);
			return pb;
		}
		return null;
	}



	@Override
	public boolean addSurvey(Survey survey) {
		boolean b = true;
		try{
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().save(survey);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}

	@Override
	public boolean addQuestion(Question question) {
		boolean b = true;
		try{
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().save(question);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}




	@Override
	public Survey getSurveyById(Survey survey) {
		String hql= "from Survey b where b.surveyId=? ";
		List list = this.getHibernateTemplate().find(hql, survey.getSurveyId());
		if(list!=null && list.size()>0){
			return (Survey) list.get(0);
		}
		return null;
	}



	@Override
	public Survey updateSurveyInfo(Survey updateSurvey) {
		Survey newSurvey = null;
		try{
			this.getHibernateTemplate().clear();
			//将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
			newSurvey = (Survey) this.getHibernateTemplate().merge(updateSurvey);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return newSurvey;
	}



	@Override
	public PageBean<Survey> querySurvey(Survey survey, int pageCode, int pageSize) {
		PageBean<Survey> pb = new PageBean<Survey>();	//pageBean对象，用于分页
		//根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
		pb.setPageCode(pageCode);//设置当前页码
		pb.setPageSize(pageSize);//设置页面记录数
		
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_sql = new StringBuilder();
		String sql = "SELECT count(*) FROM Survey b where 1=1";
		String hql= "from Survey b where 1=1";
		sb.append(hql);
		sb_sql.append(sql);
		if(!"".equals(survey.getSurveyName().trim())){
			sb.append(" and b.surveyName like '%" +survey.getSurveyName() +"%'");
			sb_sql.append(" and b.surveyName like '%" + survey.getSurveyName() +"%'");
		}
		if(!"".equals(survey.getDepartment())){
			sb.append(" and b.department like '%" +survey.getDepartment() +"%'");
			sb_sql.append(" and b.department like '%" + survey.getDepartment() +"%'");
		}
		if(!"".equals(survey.getAuthor().trim())){
			sb.append(" and b.author like '%" +survey.getAuthor() +"%'");
			sb_sql.append(" and b.author like '%" + survey.getAuthor() +"%'");
		}
		if(survey.getSurveyType().getTypeId()!=-1){
			sb.append(" and b.surveyType="+survey.getSurveyType().getTypeId());
			sb_sql.append(" and b.surveyType="+survey.getSurveyType().getTypeId());
		}
		try{
			
			List list = this.getSession().createQuery(sb_sql.toString()).list();
			int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
			pb.setTotalRecord(totalRecord);	//设置总记录数
			this.getSession().close();
			
			
			List<Survey> surveyList = doSplitPage(sb.toString(),pageCode,pageSize);
			if(surveyList!=null && surveyList.size()>0){
				pb.setBeanList(surveyList);
				return pb;
			}
		}catch (Throwable e1){
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return null;
	}



	@Override
	public boolean deleteSurvey(Survey survey) {
		boolean b = true;
		try{
			this.getHibernateTemplate().clear();
			this.getHibernateTemplate().delete(survey);
			this.getHibernateTemplate().flush();
		}catch  (Throwable e1){
			b = false;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return b;
	}


	@Override
	public int batchAddSurvey(List<Survey> surveys,List<Survey> failSurveys) {
		int success = 0;
		for (int i = 0; i < surveys.size(); i++) {
			try{
				this.getHibernateTemplate().clear();
				this.getHibernateTemplate().save(surveys.get(i));
				this.getHibernateTemplate().flush();
				success++;
			}catch (Throwable e1) {
				this.getHibernateTemplate().clear();
				failSurveys.add(surveys.get(i));
				continue ;
				
			}
		}
		return success;
	}



	@Override
	public List<Survey> findAllSurveys() {
		String hql= "from Survey ";
		List list = this.getHibernateTemplate().find(hql);
		return list;
	}



}
