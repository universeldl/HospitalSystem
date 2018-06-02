package com.hospital.service.impl;

import com.hospital.dao.PatientDao;
import com.hospital.dao.PatientTypeDao;
import com.hospital.dao.PlanDao;
import com.hospital.dao.SurveyDao;
import com.hospital.domain.*;
import com.hospital.service.PatientService;
import com.hospital.util.AgeUtils;
import com.hospital.util.CheckUtils;
import com.hospital.util.DateUtils;
import com.hospital.util.Md5Utils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


public class PatientServiceImpl implements PatientService {

    private PatientDao patientDao;

    private PatientTypeDao patientTypeDao;

    private PlanDao planDao;


    private SurveyDao surveyDao;

    public void setSurveyDao(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }


    /**
     * @param patientTypeDao the patientTypeDao to set
     */
    public void setPatientTypeDao(PatientTypeDao patientTypeDao) {
        this.patientTypeDao = patientTypeDao;
    }


    public void setPatientDao(PatientDao patientDao) {
        this.patientDao = patientDao;
    }


    @Override
    public Patient getPatient(Patient patient) {
        return patientDao.getPatient(patient);
    }

    @Override
    public Integer[] getAdditionsForLast12Months(Doctor doctor) {
        return patientDao.getAdditionsForLast12Months(doctor);
    }

/*    @Override
    public List<Patient> findAllPatients() {
        return patientDao.findAllPatients();
    }*/

    @Override
    public List<Patient> getPatientsByDoctor(Doctor doctor) {
        return patientDao.getPatientsByDoctor(doctor);
    }

    @Override
    public List<Integer> getPatientSexByDoctor(Doctor doctor) {
        return patientDao.getPatientSexByDoctor(doctor);
    }

    @Override
    public Patient updatePatientInfo(Patient patient) {
        return patientDao.updatePatientInfo(patient);
    }


    @Override
    public boolean addPatient(Patient patient) {
        return patientDao.addPatient(patient);
    }


    @Override
    public PageBean<Patient> findPatientByPage(int pageCode, int pageSize, Doctor doctor) {
        return patientDao.findPatientByPage(pageCode, pageSize, doctor);
    }

    @Override
    public PageBean<Patient> findRecyclePatientByPage(int pageCode, int pageSize, Doctor doctor) {
        return patientDao.findRecyclePatientByPage(pageCode, pageSize, doctor);
    }

    @Override
    public Patient getPatientById(Patient patient) {
        return patientDao.getPatientById(patient);
    }


    @Override
    public int deletePatient(Patient patient) {
        //删除病人需要注意的点：如果该病人有尚未答卷的问卷或者尚未设置的延期,则不能删除
        //得到该病人的分发集合
/*        Patient patientById = patientDao.getPatientById(patient);
        Set<DeliveryInfo> deliveryInfos = patientById.getDeliveryInfos();
        for (DeliveryInfo deliveryInfo : deliveryInfos) {
            if (deliveryInfo.getRetrieveInfo() == null) {
                return -1;//有尚未答卷的问卷
            }
        }*/
        boolean deletePatient = patientDao.deletePatient(patient);
        if (deletePatient) {
            return 1;
        }
        return 0;
    }


    @Override
    public PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize, Doctor doctor,
                                          Integer hospitalId, Integer cityId, Integer provinceId) {
        return patientDao.queryPatient(patient, pageCode, pageSize, doctor, hospitalId, cityId, provinceId);
    }

    @Override
    public PageBean<Patient> queryRecyclePatient(Patient patient, int pageCode, int pageSize, Doctor doctor) {
        return patientDao.queryRecyclePatient(patient, pageCode, pageSize, doctor);
    }

    @Override
    public Patient getPatientByOpenId(Patient patient) {
        return patientDao.getPatientByopenID(patient);
    }

    @Override
    public JSONObject batchAddPatient(String fileName, Doctor doctor) {
        List<Patient> patients = new ArrayList<Patient>();
        List<Patient> failPatients = new ArrayList<Patient>();
        String str[] = new String[]{"用户名码", "姓名", "病人类型", "邮箱", "联系方式"};
        // TODO Auto-generated method stub
        String realPath = ServletActionContext.getServletContext().getRealPath(fileName);
        //创建workbook
        try {
            Workbook workbook = Workbook.getWorkbook(new File(realPath));
            //获取第一个工作表sheet
            Sheet sheet = workbook.getSheet(0);

            if (sheet.getColumns() != 5) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error", "请下载模板,填入数据上传");
                jsonObject.put("state", "-1");
                return jsonObject;
            } else {
                //获取数据

                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, 0);
                    if (!cell.getContents().equals(str[j])) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("error", "请下载模板,填入数据上传");
                        jsonObject.put("state", "-1");
                        return jsonObject;
                    }
                }

            }


            //接下来就是真正的模板文件，需要解析它

            //获取数据
            for (int i = 1; i < sheet.getRows(); i++) {


                String id = sheet.getCell(0, i).getContents().trim();
                String name = sheet.getCell(1, i).getContents().trim();
                String type = sheet.getCell(2, i).getContents().trim();
                String email = sheet.getCell(3, i).getContents().trim();
                String phone = sheet.getCell(4, i).getContents().trim();

                if ("".equals(id) && "".equals(name) && "".equals(type) && "".equals(email) && "".equals(phone)) {
                    //说明这条数据是空的
                    continue;
                }

                Patient patient = new Patient();
                patient.setOpenID(id);
                patient.setName(name);
                patient.setEmail(email);
                patient.setPhone(phone);
                PatientType patientType = new PatientType();
                patientType.setPatientTypeName(type);
                patient.setPatientType(patientType);
                if ("".equals(id) || "".equals(name) || "".equals(type)) {
                    //要是前3列有一列没有数据，说明这条数据是非法的
                    //保存这条非法数据
                    failPatients.add(patient);
                    continue;
                }

                //需要根据类型名称找到对应的病人类型
                PatientType typeByName = patientTypeDao.getTypeByName(patientType);
                if (typeByName == null) {
                    //找不到这个类型，就说明这条数据非法,跳出循环
                    //保存这条非法数据
                    patientType.setPatientTypeName(patientType.getPatientTypeName() + "(没有该类型)");
                    patient.setPatientType(patientType);
                    failPatients.add(patient);
                    continue;
                }


                //判断这个电话号码格式
                if (!CheckUtils.checkMobileNumber(phone)) {
                    //不是电话格式
                    patient.setPhone(patient.getPhone() + "(手机格式有误)");
                    failPatients.add(patient);
                    continue;
                }


                //判断这个邮箱的格式
                if (!CheckUtils.checkEmail(email)) {
                    //不是邮箱格式
                    patient.setEmail(patient.getEmail() + "(邮箱格式有误)");
                    failPatients.add(patient);
                    continue;
                }


                //有这个类型的病人,接下来就是封装数据，准备进行批量添加
                patient.setPwd(Md5Utils.md5("123456"));//默认密码123456
                patient.setPatientType(typeByName);
                patient.setCreateTime(new Date(System.currentTimeMillis()));
                patient.setDoctor(doctor);
                patients.add(patient);
            }
            workbook.close();
            int success = patientDao.batchAddPatient(patients, failPatients);

            JSONObject jsonObject = new JSONObject();
            if (failPatients.size() != 0) {
                //把不成功的导出成excel文件
                String exportExcel = exportExcel(failPatients, "failPatients.xls");
                jsonObject.put("state", "2");
                jsonObject.put("message", "成功" + success + "条,失败" + failPatients.size() + "条");
                jsonObject.put("failPath", "doctor/FileDownloadAction.action?fileName=" + exportExcel);
                return jsonObject;
            } else {
                jsonObject.put("state", "1");
                jsonObject.put("message", "成功" + success + "条,失败" + failPatients.size() + "条");
                return jsonObject;
            }


        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 导出excel文件
     *
     * @param failPatients 失败的数据
     * @return 返回文件路径
     */
    public String exportExcel(List<Patient> failPatients, String name) {
        //用数组存储表头
        String[] title = {"用户名", "姓名", "性别", "病人类型", "邮箱", "联系方式"};
        String path = ServletActionContext.getServletContext().getRealPath("/download");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Calendar cal = Calendar.getInstance();
        String time = simpleDateFormat.format(cal.getTime());
        String fileName = time + "_" + name;
        //创建Excel文件
        File file = new File(path, fileName);
        try {
            file.createNewFile();
            //创建工作簿
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("sheet1", 0);

            Label label = null;

            //第一行设置列名
            for (int i = 0; i < title.length; i++) {
                label = new Label(i, 0, title[i]);
                sheet.addCell(label);
            }

            //追加数据
            for (int i = 1; i <= failPatients.size(); i++) {
                label = new Label(0, i, failPatients.get(i - 1).getOpenID());
                sheet.addCell(label);
                label = new Label(1, i, failPatients.get(i - 1).getName());
                sheet.addCell(label);
                int sex = failPatients.get(i - 1).getSex();
                String addSex;
                if (sex == 1)
                    addSex = "男";
                else if (sex == 0)
                    addSex = "女";
                else
                    addSex = "不详";
                label = new Label(2, i, addSex);
                sheet.addCell(label);
                label = new Label(3, i, failPatients.get(i - 1).getPatientType().getPatientTypeName());
                sheet.addCell(label);
                label = new Label(4, i, failPatients.get(i - 1).getEmail());
                sheet.addCell(label);
                label = new Label(5, i, failPatients.get(i - 1).getPhone());
                sheet.addCell(label);
            }
            //写入数据
            workbook.write();

            workbook.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileName;
    }


    /**
     * 导出单个病人资料及答卷情况到excel文件
     *
     * @param patient
     * @return 返回文件路径
     */
    public String exportOnePatient(Patient patient, String name) {

        String path = ServletActionContext.getServletContext().getRealPath("/download");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Calendar cal = Calendar.getInstance();
        String time = simpleDateFormat.format(cal.getTime());
        String fileName = time + "_" + name;
        //创建Excel文件
        File file = new File(path, fileName);

        try {
            file.createNewFile();
            //创建工作簿
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("sheet1", 0);

            Label label = null;

            int row = 0; //row, from 0 to the end

            label = new Label(0, row, "用户名");
            sheet.addCell(label);
            label = new Label(1, row, patient.getOpenID());
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "姓名");
            sheet.addCell(label);
            label = new Label(1, row, patient.getName());
            sheet.addCell(label);
            row++;

            int sex = patient.getSex();
            String addSex;
            if (sex == 1)
                addSex = "男";
            else if (sex == 0)
                addSex = "女";
            else
                addSex = "不详";
            label = new Label(0, row, "性别");
            sheet.addCell(label);
            label = new Label(1, row, addSex);
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "病人类型");
            sheet.addCell(label);
            label = new Label(1, row, patient.getPatientType().getPatientTypeName());
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "邮箱");
            sheet.addCell(label);
            label = new Label(1, row, patient.getEmail());
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "联系方式");
            sheet.addCell(label);
            label = new Label(1, row, patient.getPhone());
            sheet.addCell(label);
            row++;


            int headerRow = 0;
            int headerCol = 0;

            List<Survey> surveys = new ArrayList<>();

            //拿到该病人所有随访问卷(不止是当前Plan里有的survey，还包含之前plan里的survey)
            for (Survey survey : surveyDao.findAllSurveys()) {
                for (RetrieveInfo retrieveInfo : patient.getRetrieveInfos()) {
                    if (survey.getSurveyId().equals(retrieveInfo.getSurvey().getSurveyId())) {
                        surveys.add(survey);
                    }
                }
            }

            //开始写所有随访答卷内容
            for (Survey survey : surveys) {
                row++;
                headerCol = 0;
                headerRow = row;
                //构造表头
                //sheet.mergeCells(0, row, 1, row);//添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
                WritableFont bold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
                WritableCellFormat titleFormate = new WritableCellFormat(bold);//生成一个单元格样式控制对象
                titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
                titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中
                Label surveyTitle = new Label(0, headerRow, survey.getSurveyName(), titleFormate);
                sheet.setRowView(headerRow, 600, false);//设置第一行的高度
                sheet.addCell(surveyTitle);
                //构造问题列
                int qId = 0;
                for (Question question : survey.getQuestions()) {
                    row++;
                    qId++;
                    label = new Label(0, row, qId + "." + question.getQuestionContent());
                    sheet.addCell(label);
                }
                for (RetrieveInfo retrieveInfo : patient.getRetrieveInfos()) {
                    //构造答案列
                    if (survey.getSurveyId().equals(retrieveInfo.getSurvey().getSurveyId())) {
                        row = headerRow;
                        headerCol++;

                        //print retrieveDate
                        bold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
                        titleFormate = new WritableCellFormat(bold);//生成一个单元格样式控制对象
                        titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
                        titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中
                        Date retrieveDate = retrieveInfo.getRetrieveDate();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = formatter.format(retrieveDate);
                        label = new Label(headerCol, headerRow, dateString, titleFormate);
                        sheet.addCell(label);
                        row++;

                        for (Answer answer : retrieveInfo.getAnswers()) {
                            //label = new Label(headerCol, row, answer.getQuestion().getQuestionContent());
                            //sheet.addCell(label);
                            String choiceContents = "";
                            for (Choice choice : answer.getChoices()) {
                                if(choiceContents.equals("")) {
                                    choiceContents = choiceContents + choice.getScore().intValue();
                                }
                                else {
                                    choiceContents = choiceContents + "," + choice.getScore().intValue();
                                }
                            }
                            if (answer.getTextChoice() != null && answer.getTextChoiceContent() != null && answer.getTextChoice() == 1) {
                                if(choiceContents.equals("")) {
                                    choiceContents = choiceContents + answer.getTextChoiceContent();
                                }
                                else {
                                    choiceContents = choiceContents + "," + answer.getTextChoiceContent();
                                }
                            }
                            label = new Label(headerCol, row, choiceContents);
                            sheet.addCell(label);
                            row++;
                        }
                        row++;
                    }
                }
            }
            //结束写所有随访答卷内容


            //写入数据
            workbook.write();

            workbook.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileName;
    }


    @Override
    public String exportPatient(Doctor doctor) {
        List<Patient> findAllPatients = patientDao.findAllPatientsByDoctor(doctor);
        String exportPatientExcel = exportExcel(findAllPatients, "allPatients.xls");
        return "doctor/FileDownloadAction.action?fileName=" + exportPatientExcel;
    }


    @Override
    public String exportSinglePatient(Patient patient) {
        String exportPatientExcel = exportOnePatient(patient, "patient.xls");
        return "doctor/FileDownloadAction.action?fileName=" + exportPatientExcel;
    }


    @Override
    public boolean updatePlan() {
/*
        List<Patient> patients = patientDao.findAllPatients();
*/
        List<Integer> patientIds = patientDao.findAllActivePatientIds();
        for (Integer patientId : patientIds) {
            //System.out.println("plan1:" + patient.getPlan().getPlanId());

            Patient tmpPatient = new Patient();
            tmpPatient.setPatientId(patientId);
            Patient patient = patientDao.getPatientById(tmpPatient);
            //int age = 7;
            int age = AgeUtils.getAgeFromBirthTime(patient.getBirthday());
            Plan plan = new Plan();
            plan.setBeginAge(age);
            plan.setEndAge(age);  //trick here, set beginAge=endAge to get plan
            plan.setOldPatient(patient.getOldPatient());
            if (patient.getSex() == 1) {
                plan.setSex(2);  //be careful about sex, Patient.sex is not compatible with Plan.sex
            } else if (patient.getSex() == 0) {
                plan.setSex(1);
            }
            plan.setPatientType(patient.getPatientType());
            Plan newPlan = planDao.getPlan(plan);

            if (!patient.getPlan().getPlanId().equals(newPlan.getPlanId())) {
                patient.setPlan(newPlan);
                patientDao.updatePatientInfo(patient);
            }
            //System.out.println("plan2:" + patient.getPlan().getPlanId());

        }
        return true;
    }


}
