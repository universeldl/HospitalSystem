package com.hospital.service.impl;

import com.hospital.dao.*;
import com.hospital.domain.*;
import com.hospital.service.DeliveryService;
import com.hospital.util.Md5Utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class DeliveryServiceImpl implements DeliveryService {

    private DeliveryDao deliveryDao;
    private SurveyDao surveyDao;
    private PatientDao patientDao;
    private ForfeitDao forfeitDao;
    private RetrieveDao retrieveDao;


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
    public PageBean<DeliveryInfo> findDeliveryInfoByPage(int pageCode, int pageSize) {
        // TODO Auto-generated method stub
        return deliveryDao.findDeliveryInfoByPage(pageCode, pageSize);
    }

    @Override
    public DeliveryInfo getDeliveryInfoById(DeliveryInfo info) {
        // TODO Auto-generated method stub
        return deliveryDao.getDeliveryInfoById(info);
    }


    @Override
    public int addDelivery(DeliveryInfo info) {
        /*
         * 1. 得到分发的病人
         *
         * 2. 查看病人输入的密码是否匹配
         * 		2.1 如果不匹配提示 密码错误
         * 		2.2 如果匹配,执行第3步骤
         *
         * 3. 查看该病人的分发信息
         * 		3.1 查看分发信息的条数
         * 		3.2 查看该病人的类型得出该病人的最大分发数量
         * 		3.3 匹配分发的数量是否小于最大分发量
         * 			3.3.1 小于,则可以分发
         * 			3.3.2 大于,则不可以分发,直接返回分发数量已超过
         * 		3.4 查看病人的延期信息,是否有未设置的延期
         * 			3.4.1 如果没有,继续第3的操作步骤
         * 			3.4.2 如果有,直接返回有尚未设置的提醒
         *
         * 4. 查看分发的问卷
         * 		4.1 查看该问卷的总回收数是否大于1,,如果为1则不能分发,必须留在馆内浏览
         * 			4.1.1 如果大于1,进入第4操作步骤
         * 			4.1.2 如果小于等于1,提示该问卷为馆内最后一本,无法分发
         *
         * 5. 处理分发信息
         * 		5.1 得到该病人的最大分发天数,和几日提醒
         * 			5.1.1 获得当前时间
         * 			5.1.2 根据最大分发天数,计算出截止日期
         * 			5.1.3 为分发信息设置几日的提醒金额
         * 		5.2 获得该病人的信息,为分发信息设置病人信息
         * 		5.3 获得问卷信息,为分发信息设置问卷信息
         * 		5.4 获得医生信息,为分发信息设置操作的医生信息
         *
         * 6. 存储分发信息
         *
         *
         *
         * 7. 分发的问卷的总回收数需要减少
         *
         * 8. 生成答卷记录
         *
         */

        //得到密码
        String pwd = info.getPatient().getPwd();
        //1. 得到分发的病人
        Patient patient = patientDao.getPatientByopenID(info.getPatient());

        //先检查病人的用户名是否正确存在
        if (patient == null) {
            return 2;//病人用户名有误
        }

        //2. 查看病人输入的密码是否匹配
        if (!Md5Utils.md5(patient.getPwd()).equals(pwd)) {
            return -1;//返回-1表示密码错误
        }
        //3. 查看该病人的分发信息
        List<DeliveryInfo> patientInfos = deliveryDao.getNoRetrieveDeliveryInfoByPatient(patient);///查询的应该是未答卷的记录
        //查看分发信息的条数
        //查看该病人的类型得出该病人的最大分发数量
        // 匹配分发的数量是否小于最大分发量
        if (patientInfos != null && patientInfos.size() >= patient.getPatientType().getMaxNum()) {
            return -2;    //返回分发数量已超过
        }
        // 查看病人的延期信息,是否有未设置的延期
        List<ForfeitInfo> list = forfeitDao.getForfeitByPatient(patient);
        for (ForfeitInfo forfeitInfo : list) {
            if (forfeitInfo.getIsPay() == 0) {
                return -3;//尚未设置的提醒
            }
        }

        //4. 处理分发信息
        //得到该病人的最大分发天数,和几日提醒
        int maxDay = patient.getPatientType().getBday();//允许填卷天数
        double penalty = patient.getPatientType().getPenalty();//几日提醒

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
        deliveryInfo.setDeliveryDate(deliveryDate);
        deliveryInfo.setEndDate(endDate);
        deliveryInfo.setPenalty(penalty);

        int id = deliveryDao.addDelivery(deliveryInfo);//返回1成功添加,返回0添加失败
        //总回收数需要减少
        int state = 0;
        if (id != 0) {
            RetrieveInfo info2 = new RetrieveInfo();
            DeliveryInfo deliveryInfo2 = new DeliveryInfo();
            deliveryInfo2.setDeliveryId(id);
            info2.setDeliveryInfo(deliveryInfo2);
            info2.setDeliveryId(id);
            state = retrieveDao.addRetrieve(info2);
        }


        return state;
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
                        if (deliveryInfo.getState() != 4) {//not resent ones
                            num++;
                        }
                    }
                }
                if (num < survey.getTimes()) {//if not reach the times
                    //check if it's the right day, if so, do the delivery
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(patient.getCreateTime());
                        calendar.add(Calendar.MONTH, num);
                        Date endDate = calendar.getTime();
                        if (System.currentTimeMillis() < endDate.getTime()) {//Now it's the time to deliver the survey~~

                            //TODO 大神，加入微信发送code并删除此行

                            //add new DeliveryInfo after sending survey to Patient
                            DeliveryInfo deliveryInfo = new DeliveryInfo();
                            deliveryInfo.setSurvey(survey);
                            deliveryInfo.setPatient(patient);
                            int addDelivery = deliveryDao.addDelivery(deliveryInfo);//返回1成功添加,返回0添加失败
                            if(addDelivery == 1) {// added successfully
                                break;//each patient only send once, or patients will receive all the remaining surveys
                            }
                            else {
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
    public boolean checkDeliveryInfo() {
        // 检查分发表是否有逾期
        //主要步骤
        /*
         *	1.得到所有的未答卷的分发记录(包括未答卷,逾期未答卷,重发未答卷,重发逾期未答卷)
         *
         * 	2.遍历所有的未答卷的分发记录(包括未答卷,逾期为答卷,重发为答卷,重发逾期未答卷)
         *
         * 	3.查看当前时间和分发的截止的时间的大小
         *		3.1 如果小于,直接跳过
         *		3.2如果大于则需要进行逾期处理的,进行第4步操作
         *
         *	4.用当前时间减去截止时间得到逾期的天数
         *
         *	5.为当前分发记录设置逾期天数,并进行数据库修改
         *
         *	6.需要生成提醒记录
         *		6.1 得到当前分发记录的提醒金额,和当前的逾期天数进行相乘,得到提醒金额
         *		6.2 将当前分发记录的id和提醒的金额设置进新生成的提醒记录
         *
         *	7.设置当前分发记录的状态
         *		7.1 如果是未答卷，则改为逾期未答卷
         *		7.2如果是重发未答卷，则改为重发逾期未答卷
         *		7.3如果是逾期未归,则不改变
         *		7.4如果是重发逾期不答卷,则不改变
         *
         */
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
                    //需要生成提醒记录
                    ForfeitInfo forfeitInfo = new ForfeitInfo();
                    forfeitInfo.setDeliveryId(deliveryInfo.getDeliveryId());
                    forfeitInfo.setDeliveryInfo(deliveryInfo);
                    // 得到当前分发记录的提醒金额,和当前的逾期天数进行相乘,得到提醒金额
                    double pay = deliveryInfo.getPenalty() * day;
                    //将当前分发记录的id和提醒的金额设置进新生成的提醒记录
                    forfeitInfo.setForfeit(pay);
                    //生成的提醒记录
                    boolean flag = forfeitDao.addForfeitInfo(forfeitInfo);
                    if (!flag) {
                        return false;

                    }
                }
            }
        }
        return true;

    }

    @Override
    public int resendSurvey(DeliveryInfo deliveryInfo) {
        //重发步骤：
        /*
         * 1. 得到分发的记录
         *
         * 2. 得到分发的记录的状态
         * 		2.1 如果已经是重发状态(包括重发未答卷,重发逾期未答卷),则返回已经重发过了,返回-2
         * 		2.2 如果是答卷状态(包括答卷,重发答卷),则返回已经答卷,无法重发,返回-1
         *		2.3 如果都不是以上情况,则进行下一步。
         *
         * 3. 得到分发的病人
         *
         * 4. 得到分发的病人类型
         *
         * 5. 得到可重发的天数
         *
         * 6. 在当前记录的截止日期上叠加可重发天数
         * 		6.1 如果叠加后的时候比当前时间小,则返回你已超重发期了,无法进行重发,提示病人快去答卷和设置提醒  返回-3
         * 		6.2如果大于当前时间进行下一步
         *
         * 7. 得到当前分发记录的状态
         * 		7.1 如果当前记录为逾期未答卷,则需要取消当前分发记录的提醒记录,并将该记录的状态设置为重发未答卷
         * 		7.2 如果为未答卷状态,直接将当前状态设置为重发未答卷
         *
         * 8. 为当前分发记录进行设置,设置之后重新update该记录 返回1
         */
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
        Integer resendDays = deliveryInfoById.getPatient().getPatientType().getResendDays();
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
