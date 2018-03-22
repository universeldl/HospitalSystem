package com.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hospital.dao.*;
import com.hospital.domain.*;
import com.hospital.service.DeliveryService;
import com.hospital.util.AccessTokenMgr;
import com.hospital.util.Md5Utils;
import com.hospital.util.TemplateMessageMgr;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DeliveryServiceImpl implements DeliveryService {

    private DeliveryDao deliveryDao;
    private SurveyDao surveyDao;
    private PatientDao patientDao;
    private ForfeitDao forfeitDao;
    private RetrieveDao retrieveDao;
    final static String[] colors = {"#FF0000", "#E066FF", "#BC8F8F", "#698B22", "#173177"};
    static int colorNum = 0;


    public void setRetrieveDao(RetrieveDao retrieveDao) {
        this.retrieveDao = retrieveDao;
    }

    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
    }

    public void setSurveyDao(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    public void setPatientDao(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    public void setDeliveryDao(DeliveryDao deliveryDao) {
        this.deliveryDao = deliveryDao;
    }

    @Override
    public PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Doctor doctor) {
        // TODO Auto-generated method stub
        return deliveryDao.findDeliveryInfoByPage(pageCode, pageSize, doctor);
    }

    @Override
    public PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize, Patient patient) {
        // TODO Auto-generated method stub
        return deliveryDao.findDeliveryInfoByPage(pageCode, pageSize, patient);
    }

    @Override
    public List<DeliveryInfo> getUnansweredDeliveryInfos(Integer patientId) {
        // TODO Auto-generated method stub
        return deliveryDao.getUnansweredDeliveryInfos(patientId);
    }

    @Override
    public DeliveryInfo getDeliveryInfoById(DeliveryInfo info) {
        // TODO Auto-generated method stub
        return deliveryDao.getDeliveryInfoById(info);
    }

    @Override
    public PageBean<DeliveryInfo> queryDeliveryInfo(String name, int deliveryId, int pageCode, int pageSize, Doctor doctor) {
        PageBean<DeliveryInfo> pageBean = new PageBean<DeliveryInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        PageBean<Integer> list = deliveryDao.getDeliveryIdList(name, deliveryId, pageCode, pageSize, doctor);
        pageBean.setTotalRecord(list.getTotalRecord());
        List<Integer> beanList = list.getBeanList();
        if (beanList.size() == 0) {
            return null;
        }
        List<DeliveryInfo> deliveryInfos = new ArrayList<DeliveryInfo>();
        for (Integer i : beanList) {
            DeliveryInfo deliveryInfo = new DeliveryInfo();
            deliveryInfo.setDeliveryId(i);
            DeliveryInfo info = deliveryDao.getDeliveryInfoById(deliveryInfo);
            deliveryInfos.add(info);
        }
        pageBean.setBeanList(deliveryInfos);
        return pageBean;
    }

    @Override
    public PageBean<DeliveryInfo> queryDeliveryInfo(String name, int pageCode, int pageSize, Patient patient) {
        PageBean<DeliveryInfo> pageBean = new PageBean<DeliveryInfo>();
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);
        PageBean<Integer> list = deliveryDao.getDeliveryIdList(name, pageCode, pageSize, patient);
        pageBean.setTotalRecord(list.getTotalRecord());
        List<Integer> beanList = list.getBeanList();
        if (beanList.size() == 0) {
            return null;
        }
        List<DeliveryInfo> deliveryInfos = new ArrayList<DeliveryInfo>();
        for (Integer i : beanList) {
            DeliveryInfo deliveryInfo = new DeliveryInfo();
            deliveryInfo.setDeliveryId(i);
            DeliveryInfo info = deliveryDao.getDeliveryInfoById(deliveryInfo);
            deliveryInfos.add(info);
        }
        pageBean.setBeanList(deliveryInfos);
        return pageBean;
    }

    @Override
    public int addDelivery(DeliveryInfo info) {
        Patient patient = patientDao.getPatientByopenID(info.getPatient());

        //先检查病人的用户名是否正确存在
        if (patient == null) {
            return 2;//病人用户名有误
        }

        //得到该病人的最长答卷天数，过期将不再接受该次答卷
        int maxDay = info.getSurvey().getBday();//允许填卷天数

        //获得当前时间
        Date deliveryDate = new Date(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deliveryDate);
        calendar.add(Calendar.DAY_OF_MONTH, +maxDay);//+maxDay今天的时间加maxDay天

        // 根据最大分发天数,计算出截止日期
        Date endDate = calendar.getTime();

        //获得该病人的信息,为分发信息设置病人信息
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setPatient(patient);
        deliveryInfo.setDoctor(info.getDoctor());
        deliveryInfo.setState(info.getState());
        deliveryInfo.setSurvey(info.getSurvey());
        deliveryInfo.setDeliveryDate(deliveryDate);
        deliveryInfo.setEndDate(endDate);

        int id = deliveryDao.addDelivery(deliveryInfo);//返回1成功添加,返回0添加失败

        return id;
    }

    @Override
    public DeliveryInfo updateDeliveryInfo(DeliveryInfo deliveryInfo) {
        return deliveryDao.updateDeliveryInfo(deliveryInfo);
    }

    @Override
    public boolean checkAndDoDelivery() {
        List<Patient> patients = patientDao.findAllPatients();
        for (Patient patient : patients) {
            //get sorted surveys, sort to make sure that the survey we are going to send is always in a order
            Set<Survey> surveys = patient.getPlan().getSurveys();

            //get all deliveryInfos of this patient
            Set<DeliveryInfo> deliveryInfos = patient.getDeliveryInfos();

            //do check and send
            for (Survey survey : surveys) {
                int num = 0;
                for (DeliveryInfo deliveryInfo : deliveryInfos) {
                    if (deliveryInfo.getSurvey().getSurveyId() == survey.getSurveyId()
                            && deliveryInfo.getPatient().getPatientId() == patient.getPatientId()) {
                        if (deliveryInfo.getState() > num) {//get the biggest state in one survey, that's the current state.
                            num = deliveryInfo.getState();
                        }
                    }
                }
                if (num >= 0 && num < survey.getTimes()) {//if not answered AND not reach the send times limit
                    //check if it's the right day, if so, do the delivery
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(patient.getCreateTime());
                        calendar.add(Calendar.MONTH, num);
                        Date sendDate = calendar.getTime();
                        calendar.add(Calendar.DAY_OF_MONTH, survey.getBday());
                        Date endDate = calendar.getTime();
                        if (System.currentTimeMillis() > sendDate.getTime() && System.currentTimeMillis() < endDate.getTime()) {//Now it's the time to deliver the survey~~

                            //TODO 大神，加入微信发送code并删除此行

                            //add new DeliveryInfo after sending survey to Patient
                            DeliveryInfo deliveryInfo = new DeliveryInfo();
                            deliveryInfo.setSurvey(survey);
                            deliveryInfo.setPatient(patient);
                            deliveryInfo.setState(num + 1);
                            int addDelivery = addDelivery(deliveryInfo);//返回1成功添加,返回0添加失败
                            //if (addDelivery == 1) {// added successfully
                            deliveryInfo.setDeliveryId(addDelivery);
                            DeliveryInfo newDeliveryInfo = getDeliveryInfoById(deliveryInfo);
                            if (newDeliveryInfo != null) {
                                sendTemplateMessage(newDeliveryInfo);
                                survey.setNum(survey.getNum() + 1);
                                surveyDao.updateSurveyInfo(survey);// 问卷的总发送数增加
                                break;//each patient only send once, or patients will receive all the remaining surveys
                            } else {
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean sendTemplateMessage(DeliveryInfo deliveryInfo) {
        JSONObject d1 = new JSONObject();
        JSONObject d2 = new JSONObject();
        JSONObject d3 = new JSONObject();
        JSONObject d4 = new JSONObject();
        JSONObject d5 = new JSONObject();
        JSONObject data = new JSONObject();

        d1.put("value","上海儿童医学中心呼吸科随访问卷\n");
        d1.put("color", colors[colorNum]);

        Patient patient = deliveryInfo.getPatient();
        d2.put("value",patient.getName());
        d2.put("color", colors[colorNum]);

        String date = DateFormat.getDateTimeInstance().format(deliveryInfo.getDeliveryDate());
        d3.put("value", date);
        d3.put("color", colors[colorNum]);

        Survey survey = deliveryInfo.getSurvey();
        d4.put("value", survey.getSurveyName());
        d4.put("color", colors[colorNum]);

        d5.put("value","\n请点击\"详情\"开始随访。");
        d5.put("color", colors[colorNum]);

        colorNum = (colorNum>=4)?0:(colorNum+1);

        data.put("first",    d1);
        data.put("keyword1", d2);
        data.put("keyword2", d3);
        data.put("keyword3", d4);
        data.put("remark", d5);

        AccessTokenMgr mgr = AccessTokenMgr.getInstance();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);

        StringBuffer url1 = request.getRequestURL();
        String tempContextUrl1 = url1.delete(url1.length() - request.getRequestURI().length(), url1.length()).append("/").toString();

        String url = tempContextUrl1 + "/hospital-wechat/doSurvey.jsp?deliveryID=" + deliveryInfo.getDeliveryId();

        return TemplateMessageMgr.sendSurveyTemplate(data, patient.getOpenID(), url, mgr);
    }

    @Override
    public boolean checkDeliveryInfo() {//TODO: not used for now, use checkAndDoDelivery instead, DO NOT use this function
        //得到所有的未答卷的分发记录(包括未答卷,逾期为答卷,重发为答卷,重发逾期不答卷)
        List<DeliveryInfo> deliveryInfos = deliveryDao.getDeliveryInfoByNoRetrieveState();
        if (deliveryInfos != null) {
            for (DeliveryInfo deliveryInfo : deliveryInfos) {
                long time1 = deliveryInfo.getEndDate().getTime();//截止时间
                long time2 = System.currentTimeMillis();//当前时间
                if (time2 > time1) {
                    //当前时间大于截止时间,已经逾期
                    Double days = Math.floor((time2 - time1) / (24 * 60 * 60 * 1000));
                    //用当前时间减去截止时间得到逾期的天数
                    int day = days.intValue();
                    //为当前分发记录设置逾期天数,并进行数据库修改
                    deliveryInfo.setOverday(day);
                    //设置当前分发记录的状态
                    if (deliveryInfo.getState() == 0) {
                        //如果是未答卷，则改为逾期未答卷
                        deliveryInfo.setState(1);
                    } else if (deliveryInfo.getState() == 3) {
                        deliveryInfo.setState(4);
                    }
                    //进行数据库修改
                    deliveryDao.updateDeliveryInfo(deliveryInfo);
                }
            }
        }
        return true;

    }

    @Override
    public int resendSurvey(DeliveryInfo deliveryInfo) {//TODO: not used for now, DO NOT use this function
        //得到分发的记录
        DeliveryInfo deliveryInfoById = deliveryDao.getDeliveryInfoById(deliveryInfo);
        //得到分发的记录的状态
        Integer state = deliveryInfoById.getState();
        if (state == 3 || state == 4) {
            //如果已经是重发状态(包括重发未答卷,重发逾期未答卷),则返回已经重发过了,返回-2
            return -2;
        }
        if (state == 2 || state == 5) {
            //如果是答卷状态(包括答卷,重发答卷),则返回已经答卷,无法重发,返回-1
            return -1;
        }
        //得到可重发的天数
        Integer resendDays = deliveryInfoById.getSurvey().getBday();
        //在当前记录的截止日期上叠加可重发天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deliveryInfoById.getEndDate());
        calendar.add(Calendar.DAY_OF_MONTH, +resendDays);//+resendDays ,今天的时间加resendDays天
        // 根据可重发天数,计算出截止日期
        Date endDate = calendar.getTime();
        //设置截止时间
        deliveryInfoById.setEndDate(endDate);
        if (System.currentTimeMillis() > endDate.getTime()) {
            //如果叠加后的时候比当前时间小,则返回你已超重发期了,无法进行重发,提示病人快去答卷和设置提醒  返回-3
            return -3;
        }

        //得到当前分发记录的状态
        //如果当前记录为逾期未答卷,则需要取消当前分发记录的提醒记录,并将该记录的状态设置为重发未答卷
        if (state == 1) {
            RetrieveInfo retrieveInfo = new RetrieveInfo();
            retrieveInfo.setDeliveryId(deliveryInfoById.getDeliveryId());
            //删除该提醒记录
            boolean deleteRetrieveInfo = retrieveDao.deleteRetrieveInfo(retrieveInfo);
            if (!deleteRetrieveInfo) {
                return 0;
            }
        }
        //将当前状态设置为重发未答卷
        deliveryInfoById.setState(3);
        //为当前分发记录进行设置,设置之后重新update该记录 返回1
        DeliveryInfo updateDeliveryInfo = deliveryDao.updateDeliveryInfo(deliveryInfoById);
        if (deliveryInfo != null) {
            return 1;
        }

        return 0;
    }


}
