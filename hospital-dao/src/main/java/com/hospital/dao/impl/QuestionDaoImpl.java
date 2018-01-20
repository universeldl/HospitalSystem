package com.hospital.dao.impl;

import com.hospital.dao.QuestionDao;
import com.hospital.domain.Question;
import com.hospital.domain.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class QuestionDaoImpl extends HibernateDaoSupport implements QuestionDao {

    @Override
    public Question getQuestion(Question question) {
        //this.getHibernateTemplate().find(hql, value)方法无法执行的问题
        //解决需要catch (Throwable e)
        String hql = "from Question r where r.questionId=?";
        try {
            List list = this.getHibernateTemplate().find(hql, question.getQuestionId());
            if (list != null && list.size() > 0) {
                return (Question) list.get(0);
            }
        } catch (Throwable e1) {
            throw new RuntimeException(e1.getMessage());
        }

        return null;

//		Question newQuestion = (Question) this.getSession().get(Question.class, question.getQuestionId());
//		this.getSession().close();
//		return newQuestion;
    }


    @Override
    public Question updateQuestionInfo(Question question) {
        Question newQuestion = null;
        try {
            this.getHibernateTemplate().clear();
            //将传入的detached(分离的)状态的对象的属性复制到持久化对象中，并返回该持久化对象
            newQuestion = (Question) this.getHibernateTemplate().merge(question);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return newQuestion;
    }


    @Override
    public boolean addQuestion(Question question) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().save(question);
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
    public PageBean<Question> findQuestionByPage(int pageCode, int pageSize) {
        PageBean<Question> pb = new PageBean<Question>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数
        List questionList = null;
        try {
            String sql = "SELECT count(*) FROM Question";
            List list = this.getSessionFactory().getCurrentSession().createQuery(sql).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数

            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();

            //不支持limit分页
            String hql = "from Question";
            //分页查询
            questionList = doSplitPage(hql, pageCode, pageSize);


        } catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        if (questionList != null && questionList.size() > 0) {
            pb.setBeanList(questionList);
            return pb;
        }
        return null;
    }


    @Override
    public Question getQuestionById(Question question) {
        String hql = "from Question r where r.questionId=?";
        List list = this.getHibernateTemplate().find(hql, question.getQuestionId());
        if (list != null && list.size() > 0) {
            return (Question) list.get(0);
        }
        return null;
    }


    @Override
    public boolean deleteQuestion(Question question) {
        boolean b = true;
        try {
            this.getHibernateTemplate().clear();
            this.getHibernateTemplate().delete(question);
            this.getHibernateTemplate().flush();
        } catch (Throwable e1) {
            b = false;
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
        return b;
    }


    @Override
    public PageBean<Question> queryQuestion(Question question, int pageCode, int pageSize) {
        PageBean<Question> pb = new PageBean<Question>();    //pageBean对象，用于分页
        //根据传入的pageCode当前页码和pageSize页面记录数来设置pb对象
        pb.setPageCode(pageCode);//设置当前页码
        pb.setPageSize(pageSize);//设置页面记录数


        StringBuilder sb = new StringBuilder();
        StringBuilder sb_sql = new StringBuilder();
        String sql = "SELECT count(*) FROM Question r where 1=1";
        String hql = "from Question r where 1=1";
        sb.append(hql);
        sb_sql.append(sql);

        try {

            List list = this.getSessionFactory().getCurrentSession().createQuery(sb_sql.toString()).list();
            int totalRecord = Integer.parseInt(list.get(0).toString()); //得到总记录数
            pb.setTotalRecord(totalRecord);    //设置总记录数
            //this.getSessionFactory().getCurrentSession().close();


            List<Question> doctorList = doSplitPage(sb.toString(), pageCode, pageSize);
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
    public Question getQuestionByopenID(Question question) {
        String hql = "from Question r where r.questionId=?";
        List list = this.getHibernateTemplate().find(hql, question.getAid());
        if (list != null && list.size() > 0) {
            return (Question) list.get(0);
        }
        return null;
    }


    @Override
    public int batchAddQuestion(List<Question> questions, List<Question> failQuestions) {
        int success = 0;
        for (int i = 0; i < questions.size(); i++) {
            try {
                this.getHibernateTemplate().clear();
                this.getHibernateTemplate().save(questions.get(i));
                this.getHibernateTemplate().flush();
                success++;
            } catch (Throwable e1) {
                this.getHibernateTemplate().clear();
                failQuestions.add(questions.get(i));
                continue;

            }
        }
        return success;
    }


    @Override
    public List<Question> findAllQuestions() {
        String hql = "from Question ";
        List list = this.getHibernateTemplate().find(hql);
        return list;
    }


}
