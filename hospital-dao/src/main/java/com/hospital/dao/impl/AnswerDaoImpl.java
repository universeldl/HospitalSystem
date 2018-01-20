package com.hospital.dao.impl;

import com.hospital.dao.AnswerDao;
import com.hospital.domain.PageBean;
import com.hospital.domain.Answer;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class AnswerDaoImpl extends HibernateDaoSupport implements AnswerDao {

    @Override
    public Answer getAnswer(Answer answer) {
        //this.getHibernateTemplate().find(hql, value)方法无法执行的问题
        //解决需要catch (Throwable e)
        String hql = "from Answer r where r.answerId=?";
        try {
            List list = this.getHibernateTemplate().find(hql, answer.getAnswerId());
            if (list != null && list.size() > 0) {
                return (Answer) list.get(0);
            }
        } catch (Throwable e1) {
            throw new RuntimeException(e1.getMessage());
        }

        return null;

//		Answer newAnswer = (Answer) this.getSession().get(Answer.class, answer.getAnswerId());
//		this.getSession().close();
//		return newAnswer;
    }


    @Override
    public Answer updateAnswerInfo(Answer answer) {
        Answer newAnswer = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            newAnswer = (Answer) this.getHibernateTemplate().merge(answer);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return newAnswer;
    }


    @Override
    public boolean addAnswer(Answer answer) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(answer);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }


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
    public PageBean<Answer> findAnswerByPage(int pageCode, int pageSize) {
        PageBean<Answer> pb = new PageBean<Answer>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List answerList = null;
        try {
            String sql = "SELECT count(*) FROM Answer";
            List list = this.getSessionFactory().getCurrentSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数

            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            String hql = "from Answer";
            //分页查询
            answerList = doSplitPage(hql, pageCode, pageSize);


        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        if (answerList != null && answerList.size() > 0) {
            pb.setBeanList(answerList);
            return pb;
        }
        return null;
    }


    @Override
    public Answer getAnswerById(Answer answer) {
        String hql = "from Answer r where r.answerId=?";
        List list = this.getHibernateTemplate().find(hql, answer.getAnswerId());
        if (list != null && list.size() > 0) {
            return (Answer) list.get(0);
        }
        return null;
    }


    @Override
    public boolean deleteAnswer(Answer answer) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(answer);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }


    @Override
    public PageBean<Answer> queryAnswer(Answer answer, int pageCode, int pageSize) {
        PageBean<Answer> pb = new PageBean<Answer>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数


        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "SELECT count(*) FROM Answer r where 1=1";
        String hql = "from Answer r where 1=1";
        sb.append(hql);
        sb_sql.append(sql);

        try {

            List list = this.getSessionFactory().getCurrentSession().createQuery(sb_sql.toString()).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();


            List<Answer> doctorList = doSplitPage(sb.toString(), pageCode, pageSize);
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
    public Answer getAnswerByopenID(Answer answer) {
        String hql = "from Answer r where r.answerId=?";
        List list = this.getHibernateTemplate().find(hql, answer.getDoctor().getAid());
        if (list != null && list.size() > 0) {
            return (Answer) list.get(0);
        }
        return null;
    }


    @Override
    public int batchAddAnswer(List<Answer> answers, List<Answer> failAnswers) {
        int success = 0;
        for (int i = 0; i < answers.size(); i++) {
            try {
                this.getHibernateTemplate().clear();
                this.getHibernateTemplate().save(answers.get(i));
                this.getHibernateTemplate().flush();
                success++;
            } catch (Throwable e1) {
                this.getHibernateTemplate().clear();
                failAnswers.add(answers.get(i));
                continue;

            }
        }
        return success;
    }


    @Override
    public List<Answer> findAllAnswers() {
        String hql = "from Answer ";
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }


}
