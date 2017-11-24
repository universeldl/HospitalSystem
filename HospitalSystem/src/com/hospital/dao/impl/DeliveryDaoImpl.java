package com.hospital.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.hospital.dao.DeliveryDao;
import com.hospital.domain.RetrieveInfo;
import com.hospital.domain.Survey;
import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;

public class DeliveryDaoImpl extends HibernateDaoSupport implements DeliveryDao{

	
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
	public PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize) {
		PageBean<DeliveryInfo> pb = new PageBean<DeliveryInfo>();	//pageBean对象，用于分页
		//根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
		pb.setPageCode(pageCode);//设置当前页码
		pb.setPageSize(pageSize);//设置页面记录数
		List deliveryInfoList = null;
		try {
			String sql = "SELECT count(*) FROM DeliveryInfo";
			List list = this.getSession().createQuery(sql).list();
			int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
			pb.setTotalRecord(totalRecord);	//设置总记录数
			this.getSession().close();
			
			//不支持limit分页
			String hql= "from DeliveryInfo";
			//分页查询
			deliveryInfoList = doSplitPage(hql,pageCode,pageSize);
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		if(deliveryInfoList!=null && deliveryInfoList.size()>0){
			pb.setBeanList(deliveryInfoList);
			return pb;
		}
		return null;
	}




	@Override
	public DeliveryInfo getDeliveryInfoById(DeliveryInfo info) {
		String hql= "from DeliveryInfo b where b.deliveryId=?";
		List list = this.getHibernateTemplate().find(hql, info.getDeliveryId());
		if(list!=null && list.size()>0){
			return (DeliveryInfo) list.get(0);
		}
		return null;
	}




	@Override
	public int addDelivery(DeliveryInfo info) {
		Integer integer = 0;
		try{
			this.getHibernateTemplate().clear();
			//save方法返回的是Serializable接口，该结果的值就是你插入到数据库后新记录的主键值
			Serializable save = this.getHibernateTemplate().save(info);
			this.getHibernateTemplate().flush();
			integer = (Integer)save;
		}catch (Throwable e1) {
			integer = 0;
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return integer;
	}




	@Override
	public List<DeliveryInfo> getNoRetrieveDeliveryInfoByPatient(Patient patient) {
		String hql= "from DeliveryInfo b where b.state=0 or b.state=1 or b.state=3 or b.state=4 and b.patient.patientId=? ";
		List list = null;
		try {
			list = this.getHibernateTemplate().find(hql, patient.getPatientId());
		}catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return list;
	}




	@Override
	public DeliveryInfo updateDeliveryInfo(DeliveryInfo deliveryInfoById) {
		DeliveryInfo deliveryInfo = null;
		try{
			this.getHibernateTemplate().clear();
			//将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
			deliveryInfo = (DeliveryInfo) this.getHibernateTemplate().merge(deliveryInfoById);
			this.getHibernateTemplate().flush();
		}catch (Throwable e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		return deliveryInfo;
	}








	@Override
	public List<DeliveryInfo> getDeliveryInfoByNoRetrieveState() {
		String hql= "from DeliveryInfo b where b.state=0 or b.state=1 or b.state=3 or b.state=4";
		List<DeliveryInfo> list = null;
		try {
			list = this.getHibernateTemplate().find(hql);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return list;
	}




	@Override
	public List<DeliveryInfo> getDeliveryInfoBySurvey(Survey survey) {
		String hql= "from DeliveryInfo b where b.survey.surveyId=?";
		List<DeliveryInfo> list = null;
		try{
		list = this.getHibernateTemplate().find(hql, survey.getSurveyId());
		}catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return list;
	}




	
	
	
	
	

}
