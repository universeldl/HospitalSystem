package com.hospital.service.impl;

import com.hospital.dao.*;
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
import java.math.BigDecimal;
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

    private DeliveryDao deliveryDao;

    public void setDeliveryDao(DeliveryDao deliveryDao) {
        this.deliveryDao = deliveryDao;
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

        Calendar firstRegisterDate = Calendar.getInstance();
        firstRegisterDate.set(3000,1,1);
        for (Patient patient : failPatients) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(patient.getCreateTime());
            if (firstRegisterDate.after(calendar)) {
                firstRegisterDate = calendar;
            }
        }

        System.out.println("first register date = " + firstRegisterDate.toString());



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
            WritableFont bold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
            WritableCellFormat titleFormate = new WritableCellFormat(bold);//生成一个单元格样式控制对象
            titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
            titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中

            Label label = null;

            int row = 0; //row, from 0 to the end

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

            label = new Label(0, row, "年龄");
            sheet.addCell(label);
            String age = String.valueOf(AgeUtils.getAgeFromBirthTime(patient.getBirthday()));
            label = new Label(1, row, age);
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "生日");
            sheet.addCell(label);
            String birthday = patient.getBirthday().toLocaleString();
            label = new Label(1, row, birthday);
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "身高");
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "体重");
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "家长手机");
            sheet.addCell(label);
            label = new Label(1, row, patient.getPhone());
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "入组日期");
            sheet.addCell(label);
            label = new Label(1, row, patient.getCreateTime().toLocaleString());
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "治疗方案");
            sheet.addCell(label);
            row++;

            label = new Label(0, row, "过敏原");
            sheet.addCell(label);
            row++;

            int headerRow = row;
            int headerCol = 0;

            sheet.setColumnView(headerCol, 6000);




            // Survey == 2 || survey == 3
            for (RetrieveInfo retrieveInfo : patient.getRetrieveInfos()) {
                DeliveryInfo deliveryInfo = retrieveInfo.getDeliveryInfo();
                Survey survey = deliveryInfo.getSurvey();

                if (survey.getSurveyId() == 2 || survey.getSurveyId() == 3) {

                    Label surveyTitle = new Label(0, headerRow, survey.getSurveyName(), titleFormate);
                    sheet.setRowView(headerRow, 600, false);//设置第一行的高度
                    sheet.addCell(surveyTitle);
                    row++;

                    Set<Answer> tmpAnswers = retrieveInfo.getAnswers();

                    int age1 = AgeUtils.getAgeFromBirthTime(patient.getBirthday(), deliveryInfo.getDeliveryDate());

                    List<Answer> myAnswers = new ArrayList<>();
                    for (Answer answer : tmpAnswers) {
                        Question question = answer.getQuestion();
                        if ((question.getStartAge() == 99 && question.getEndAge() == 99) ||
                                (question.getStartAge() == -1 && question.getEndAge() == -1)) {
                            myAnswers.add(answer);
                        } else if (age1 >= question.getStartAge() && age1 <= question.getEndAge()) {
                            myAnswers.add(answer);
                        }
                    }

                    Collections.sort(myAnswers, new Comparator<Answer>() {
                        @Override
                        public int compare(Answer s1, Answer s2) {
                            return s1.getQuestion().getSortId()-(s2.getQuestion().getSortId());
                        }
                    });


                    int control_score = 0;
                    for (Answer answer : myAnswers) {
                        Question question = answer.getQuestion();
                        label = new Label(headerCol, row, question.getQuestionContent());
                        sheet.addCell(label);

                        if (question.getQuestionType() == 1 || question.getQuestionType() == 2) {
                            Integer score = 0;
                            for (Choice choice : answer.getChoices()) {
                                score += choice.getScore().intValue();
                            }
                            label = new Label(headerCol+1, row, String.valueOf(score));
                        } else {
                            label = new Label(headerCol+1, row, answer.getTextChoiceContent());
                        }
                        sheet.addCell(label);
                        row++;

                        if (survey.getSurveyId() == 3) {
                            if (question.getQuestionId() >= 90 && question.getQuestionId() <=97) {
                                for (Choice choice : answer.getChoices()) {
                                    control_score += choice.getScore().intValue();
                                }
                            }

                            if (question.getQuestionId() == 93 || question.getQuestionId() == 97) {
                                label = new Label(headerCol, row, "哮喘控制\n" +
                                        "0=控制，1-2分=部分控制，3-4分=未控制", titleFormate);
                                sheet.addCell(label);

                                label = new Label(headerCol+1, row, String.valueOf(control_score));
                                sheet.addCell(label);
                                row++;
                            }
                        }
                    }
                    break;
                }
            }

            // survey == 4
            int maxTotalMonth = 0;//max total months to display
            Survey s = new Survey();
            s.setSurveyId(4);
            Survey survey = surveyDao.getSurveyById(s);
            if (survey != null) {
                int totalMonth = survey.getFrequency() * survey.getTimes() + 2;// +1 to include the titles in first col; +1 again to include the date of sign-in in 2nd col.
                if (totalMonth > maxTotalMonth) {
                    maxTotalMonth = totalMonth;
                }
            } else {
                return null;
            }

            Label surveyTitle = new Label(0, row, survey.getSurveyName(), titleFormate);
            sheet.setRowView(row, 600, false);
            sheet.addCell(surveyTitle);

            int cur_col = 1;

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(patient.getCreateTime());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (int j = 0; j < maxTotalMonth; j++) {//from the 3rd col till the end

                Calendar endCal = (Calendar) startCal.clone();
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DATE, -1);

                String ds = "第" + j + "个月\r";

/*                ds += formatter.format(startCal.getTime()) + " —— "
                        + formatter.format(endCal.getTime());*/
                Label monthLabel = new Label(cur_col, row, ds);
                sheet.setColumnView(cur_col, 1800);

                sheet.addCell(monthLabel);

                startCal.add(Calendar.MONTH, 1);
                cur_col += 2;
            }
            row++;

            int cur_row = row;
            int max_row = 0;
            for (Question question : survey.getQuestions()) {


                int age1 = AgeUtils.getAgeFromBirthTime(patient.getBirthday());
                if ((question.getStartAge() == 99 && question.getEndAge() == 99) ||
                        (question.getStartAge() == -1 && question.getEndAge() == -1)) {
                } else if (age1 >= question.getStartAge() && age1 <= question.getEndAge()) {
                } else {
                    System.out.println("age  = " + age1 + " quesiton id = " + question.getQuestionId() + " continued");
                    continue;
                }
                System.out.println("age  = " + age1 + " quesiton id = " + question.getQuestionId() + " NOT continued");

                String title = question.getQuestionContent();
                label = new Label(headerCol, cur_row, title);
                sheet.addCell(label);
                cur_row++;

                if (question.getQuestionId() == 124 ||
                        question.getQuestionId() == 178) {
                    label = new Label(headerCol, cur_row, "哮喘控制\r0=控制，1-2分=部分控制，3-4分=未控制", titleFormate);
                    sheet.addCell(label);
                    cur_row++;

                    label = new Label(headerCol, cur_row, "控制水平（控制／部分控制／未控制)", titleFormate);
                    sheet.addCell(label);
                    cur_row++;
                }
                max_row = max_row > cur_row ? max_row : cur_row;
            }

            cur_col = 1;
            DeliveryInfo tmpDeliveryInfo = new DeliveryInfo();
            tmpDeliveryInfo.setPatient(patient);
            tmpDeliveryInfo.setSurvey(survey);
            startCal.setTime(patient.getCreateTime());
            for (int j = 0; j < maxTotalMonth; j++) {//from the 3rd col till the end
                Calendar endCal = (Calendar) startCal.clone();
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DATE, -1);

                cur_row = row;

                List<DeliveryInfo> deliveryInfos = deliveryDao.getDelivryInfoBySurveyAndDate(tmpDeliveryInfo, startCal, endCal);
                if (deliveryInfos != null && !deliveryInfos.isEmpty()) {

                    RetrieveInfo retrieveInfo = deliveryInfos.get(0).getRetrieveInfo();
                    if (retrieveInfo != null) {
                        Set<Answer> tmpAnswers = retrieveInfo.getAnswers();

                        int age1 = AgeUtils.getAgeFromBirthTime(patient.getBirthday(), retrieveInfo.getDeliveryInfo().getDeliveryDate());

                        List<Answer> myAnswers = new ArrayList<>();
                        for (Answer answer : tmpAnswers) {
                            Question question = answer.getQuestion();
                            if ((question.getStartAge() == 99 && question.getEndAge() == 99) ||
                                    (question.getStartAge() == -1 && question.getEndAge() == -1)) {
                                myAnswers.add(answer);
                            } else if (age1 >= question.getStartAge() && age1 <= question.getEndAge()) {
                                myAnswers.add(answer);
                            }
                        }

                        Collections.sort(myAnswers, new Comparator<Answer>() {
                            @Override
                            public int compare(Answer s1, Answer s2) {
                                return s1.getQuestion().getSortId()-(s2.getQuestion().getSortId());
                            }
                        });

                        int totalScore = 0;
                        for(Answer answer : myAnswers) {
                            Question question = answer.getQuestion();
                            //if (answer.getTextChoiceContent() == null) {
                            if (question.getQuestionType() == 1 || question.getQuestionType() == 2) {
                                Integer score = 0;
                                for (Choice choice : answer.getChoices()) {
                                    score += choice.getScore().intValue();
                                }
                                label = new Label(cur_col, cur_row, String.valueOf(score));
                            } else {
                                label = new Label(cur_col, cur_row, answer.getTextChoiceContent());
                            }
                            sheet.addCell(label);
                            cur_row++;

                            if (answer.getQuestion().getQuestionId() == 121 ||
                                answer.getQuestion().getQuestionId() == 122 ||
                                answer.getQuestion().getQuestionId() == 123 ||
                                answer.getQuestion().getQuestionId() == 124 ||
                                answer.getQuestion().getQuestionId() == 125 ||
                                answer.getQuestion().getQuestionId() == 126 ||
                                answer.getQuestion().getQuestionId() == 177 ||
                                answer.getQuestion().getQuestionId() == 178) {
                                for (Choice choice : answer.getChoices()) {
                                    totalScore += choice.getScore().intValue();
                                }
                            }

                            if (answer.getQuestion().getQuestionId() == 124 ||
                                    answer.getQuestion().getQuestionId() == 178) {
                                String ds = "第" + j + "个月";
                                label = new Label(cur_col, cur_row, ds, titleFormate);
                                sheet.addCell(label);
                                cur_row++;

                                label = new Label(cur_col, cur_row, String.valueOf(totalScore), titleFormate);
                                sheet.addCell(label);
                                cur_row++;
                            }

                        }
                    }

                }
                startCal.add(Calendar.MONTH, 1);
                cur_col += 2;
            }

            row = max_row;
            // survey = 6 CACT
            s.setSurveyId(6);
            survey = surveyDao.getSurveyById(s);
            if (survey == null) {
                return null;
            }

            surveyTitle = new Label(0, row, survey.getSurveyName(), titleFormate);
            sheet.setRowView(row, 600, false);
            sheet.addCell(surveyTitle);

            cur_col = 1;
            startCal = Calendar.getInstance();
            startCal.setTime(patient.getCreateTime());
            for (int j = 0; j < maxTotalMonth; j++) {//from the 3rd col till the end

                Calendar endCal = (Calendar) startCal.clone();
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DATE, -1);

                String ds = "第" + j + "个月\r";

/*                ds += formatter.format(startCal.getTime()) + " —— "
                        + formatter.format(endCal.getTime());*/
                Label monthLabel = new Label(cur_col, row, ds);
                sheet.setColumnView(cur_col, 1800);

                sheet.addCell(monthLabel);

                startCal.add(Calendar.MONTH, 1);
                cur_col += 2;
            }
            row++;

            surveyTitle = new Label(0, row, "总分", titleFormate);
            sheet.addCell(surveyTitle);

            cur_col = 1;
            tmpDeliveryInfo = new DeliveryInfo();
            tmpDeliveryInfo.setPatient(patient);
            tmpDeliveryInfo.setSurvey(survey);
            startCal.setTime(patient.getCreateTime());
            for (int j = 0; j < maxTotalMonth; j++) {//from the 3rd col till the end
                Calendar endCal = (Calendar) startCal.clone();
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DATE, -1);
                int totalScore = 0;

                List<DeliveryInfo> deliveryInfos = deliveryDao.getDelivryInfoBySurveyAndDate(tmpDeliveryInfo, startCal, endCal);
                if (deliveryInfos != null && !deliveryInfos.isEmpty()) {

                    RetrieveInfo retrieveInfo = deliveryInfos.get(0).getRetrieveInfo();
                    if (retrieveInfo != null) {
                        Set<Answer> tmpAnswers = retrieveInfo.getAnswers();
                        for(Answer answer : tmpAnswers) {
                            for (Choice choice : answer.getChoices()) {
                                totalScore += choice.getScore().intValue();
                            }
                        }
                        label = new Label(cur_col, row, String.valueOf(totalScore));
                        sheet.addCell(label);
                    }
                }
                startCal.add(Calendar.MONTH, 1);
                cur_col += 2;
            }
            row++;

            // survey = 5
            s.setSurveyId(5);
            survey = surveyDao.getSurveyById(s);
            if (survey == null) {
                return null;
            }

            surveyTitle = new Label(0, row, survey.getSurveyName(), titleFormate);
            sheet.setRowView(row, 600, false);
            sheet.addCell(surveyTitle);

            cur_col = 1;
            startCal = Calendar.getInstance();
            startCal.setTime(patient.getCreateTime());
            for (int j = 0; j < maxTotalMonth; j++) {//from the 3rd col till the end

                Calendar endCal = (Calendar) startCal.clone();
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DATE, -1);

                String ds = "第" + j + "个月\r";
/*
                ds += formatter.format(startCal.getTime()) + " —— "
                        + formatter.format(endCal.getTime());*/
                Label monthLabel = new Label(cur_col, row, ds);
                sheet.setColumnView(cur_col, 1800);

                sheet.addCell(monthLabel);

                startCal.add(Calendar.MONTH, 1);
                cur_col += 2;
            }
            row++;

            cur_row = row;
            max_row = 0;
            for (Question question : survey.getQuestions()) {


                int age1 = AgeUtils.getAgeFromBirthTime(patient.getBirthday());
                if ((question.getStartAge() == 99 && question.getEndAge() == 99) ||
                        (question.getStartAge() == -1 && question.getEndAge() == -1)) {
                } else if (age1 >= question.getStartAge() && age1 <= question.getEndAge()) {
                } else {
                    System.out.println("age  = " + age1 + " quesiton id = " + question.getQuestionId() + " continued");
                    continue;
                }
                System.out.println("age  = " + age1 + " quesiton id = " + question.getQuestionId() + " NOT continued");

                String title = question.getQuestionContent();
                label = new Label(headerCol, cur_row, title);
                sheet.addCell(label);
                cur_row++;
                max_row = max_row > cur_row ? max_row : cur_row;
            }

            cur_col = 1;
            tmpDeliveryInfo = new DeliveryInfo();
            tmpDeliveryInfo.setPatient(patient);
            tmpDeliveryInfo.setSurvey(survey);
            startCal.setTime(patient.getCreateTime());
            for (int j = 0; j < maxTotalMonth; j++) {//from the 3rd col till the end
                Calendar endCal = (Calendar) startCal.clone();
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.DATE, -1);

                cur_row = row;

                List<DeliveryInfo> deliveryInfos = deliveryDao.getDelivryInfoBySurveyAndDate(tmpDeliveryInfo, startCal, endCal);
                if (deliveryInfos != null && !deliveryInfos.isEmpty()) {

                    RetrieveInfo retrieveInfo = deliveryInfos.get(0).getRetrieveInfo();
                    if (retrieveInfo != null) {
                        Set<Answer> tmpAnswers = retrieveInfo.getAnswers();

                        int age1 = AgeUtils.getAgeFromBirthTime(patient.getBirthday(), retrieveInfo.getDeliveryInfo().getDeliveryDate());

                        List<Answer> myAnswers = new ArrayList<>();
                        for (Answer answer : tmpAnswers) {
                            Question question = answer.getQuestion();
                            if ((question.getStartAge() == 99 && question.getEndAge() == 99) ||
                                    (question.getStartAge() == -1 && question.getEndAge() == -1)) {
                                myAnswers.add(answer);
                            } else if (age1 >= question.getStartAge() && age1 <= question.getEndAge()) {
                                myAnswers.add(answer);
                            }
                        }

                        Collections.sort(myAnswers, new Comparator<Answer>() {
                            @Override
                            public int compare(Answer s1, Answer s2) {
                                return s1.getQuestion().getSortId()-(s2.getQuestion().getSortId());
                            }
                        });

                        int totalScore = 0;
                        for(Answer answer : myAnswers) {
                            Question question = answer.getQuestion();
                            if (question.getQuestionType() == 1 || question.getQuestionType() == 2) {
                                Integer score = 0;
                                for (Choice choice : answer.getChoices()) {
                                    score += choice.getScore().intValue();
                                }
                                label = new Label(cur_col, cur_row, String.valueOf(score));
                            } else {
                                label = new Label(cur_col, cur_row, answer.getTextChoiceContent());
                            }
                            sheet.addCell(label);
                            cur_row++;


                            if (answer.getQuestion().getQuestionId() == 124 ||
                                    answer.getQuestion().getQuestionId() == 178) {
                                String ds = "第" + j + "个月";
                                label = new Label(cur_col, cur_row, ds, titleFormate);
                                sheet.addCell(label);
                                cur_row++;

                                label = new Label(cur_col, cur_row, String.valueOf(totalScore), titleFormate);
                                sheet.addCell(label);
                                cur_row++;
                            }

                        }
                    }

                }
                startCal.add(Calendar.MONTH, 1);
                cur_col += 2;
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
            System.out.println("update plan for : " + patient.getName());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("\ttime:" + df.format(new Date()));


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

            boolean good = genDeliveryInfoForPatient(patient);
            System.out.println("generate delivery for patient "+(good?"succeeded":"failed"));
        }
        return true;
    }

    private boolean genDeliveryInfoForPatient(Patient patient) {
        System.out.println("generate delivery LOG patient = " + patient.getName());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("\ttime:" + df.format(new Date()));

        Calendar curCalendar = Calendar.getInstance();

        //get all banned survey list of this patient
        String sourceStr = patient.getBannedSurveyList();
        String[] sourceStrArray = sourceStr.split(",");
        Set<Integer> bannedList = new HashSet<>();
        for (int i = 0; i < sourceStrArray.length; i++) {
            if (sourceStrArray[i].equals("")) continue;
            bannedList.add(Integer.parseInt(sourceStrArray[i]));
        }

        // get create time
        Calendar createDate = Calendar.getInstance();
        createDate.setTime(patient.getCreateTime());
        System.out.println("generate delivery LOG createDate = " + createDate.getTime().toString());

        // all survey
        Set<Survey> surveys = patient.getPlan().getSurveys();

        // all deliveryInfo
        Set<DeliveryInfo> deliveryInfos = patient.getDeliveryInfos();

        for (Survey survey : surveys) {
            System.out.println("generate delivery LOG survey = " + survey.getSurveyName());

            boolean isBanned = false;
            for (Integer sId : bannedList) {
                if (sId.equals(survey.getSurveyId())) {
                    isBanned = true;
                }
            }
            if (isBanned) continue;

            try {
                //threshold, only registry date after this date we will send this survey
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-23 00:00:00");

                //register date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(patient.getCreateTime());
                Date regDate = calendar.getTime();

                //one month later of the register date
                calendar.add(Calendar.MONTH, 1);
                Date endDate = calendar.getTime();

                if (survey.getSurveyId() == 7) {
                    if (regDate.before(date) || (regDate.after(date) && System.currentTimeMillis() > endDate.getTime())) {
                        continue;
                    }
                    if (patient.getOpenID() != "o5bAaxGhIV0ZksDNy8y26pk_XUI8") {
                        continue;
                    }
                }
                else if (survey.getSurveyId() == 8) {
                    if (regDate.before(date) || (regDate.after(date) && System.currentTimeMillis() < endDate.getTime())) {
                        continue;
                    }
                    if (patient.getOpenID() != "o5bAaxGhIV0ZksDNy8y26pk_XUI8") {
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Calendar start = (Calendar) createDate.clone();

            Integer num_cycle = 0;
            while(true) {
                num_cycle += 1;

                // finished all survey
                if (num_cycle > survey.getTimes()) break;

                Calendar end = (Calendar) start.clone();
                end.add(Calendar.DATE, survey.getBday());

                System.out.println("generate delivery LOG num_cycle = " + num_cycle.toString());
                System.out.println("generate delivery LOG start = " + start.getTime().toString());
                System.out.println("generate delivery LOG end = " + end.getTime().toString());


                if ((curCalendar.after(start) || curCalendar.equals(start)) && curCalendar.before(end)) {
                    if (num_cycle == 1 && !survey.isSendOnRegister()) {
                        // do nothing;
                        System.out.println("generate delivery LOG donoting!");

                    } else {
                        // should send message
                        DeliveryInfo deliveryInfoInCycle = null;
                        for (DeliveryInfo deliveryInfo : deliveryInfos) {
                            if (!deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) continue;
                            Calendar deliveryInfoDate = Calendar.getInstance();
                            deliveryInfoDate.setTime(deliveryInfo.getDeliveryDate());
                            if ((deliveryInfoDate.after(start) || deliveryInfoDate.equals(start))
                                    && deliveryInfoDate.before(end)) {
                                deliveryInfoInCycle = deliveryInfo;
                                break;
                            }
                        }

                        // there is already delivery Info
                        if (deliveryInfoInCycle != null) {
                            System.out.println("generate delivery LOG already have delivery id = " + deliveryInfoInCycle.getDeliveryId());

                            if (deliveryInfoInCycle.getRetrieveInfo() == null) {
                                System.out.println("generate delivery LOG resend delivery id = " + deliveryInfoInCycle.getDeliveryId());

                                deliveryInfoInCycle.setState(deliveryInfoInCycle.getState() + 1);
                                deliveryDao.updateDeliveryInfo(deliveryInfoInCycle);
                                /*
                                if ( !isBanned ) {
                                    sendTemplateMessage(deliveryInfoInCycle);
                                }
                                */
                            } else {
                                System.out.println("generate delivery LOG delivery id = " + deliveryInfoInCycle.getDeliveryId() + " already answered");
                            }
                        } else {
                            DeliveryInfo newDI = new DeliveryInfo();
                            newDI.setDoctor(patient.getDoctor());
                            newDI.setSurvey(survey);
                            newDI.setPatient(patient);
                            newDI.setState(0);//新deliveryInfo
                            int addDelivery = addDelivery(newDI);
                            newDI.setDeliveryId(addDelivery);
                            DeliveryInfo newDeliveryInfo = deliveryDao.getDeliveryInfoById(newDI);
                            System.out.println("generate delivery LOG create new delivery = " + addDelivery);

                            if (newDeliveryInfo != null) {
                                //if ( !isBanned ) {
                                //    sendTemplateMessage(newDeliveryInfo);
                                //}
                                //survey.setNum(survey.getNum() + 1);
                                //surveyDao.updateSurveyInfo(survey);// 问卷的总发送数增加
                                //System.out.println("checkAndDoDelivery2 survey number +1 = " + survey.getNum());
                            } else {
                                System.out.println("generate delivery newDeliveryInfo = null!!");
                                return false;
                            }
                        }
                    }

                    break;
                }

                start.add(Calendar.MONTH, survey.getFrequency());
            }
        }
        return true;
    }


    private int addDelivery(DeliveryInfo info) {
        Patient patient = patientDao.getPatientByopenID(info.getPatient());

        //得到该病人的最长答卷天数，过期将不再接受该次答卷
        int maxDay = info.getSurvey().getBday();//允许填卷天数

        //获得当前时间
        Date deliveryDate = new Date(System.currentTimeMillis());
        if (info.getDeliveryDate() != null) {
            deliveryDate = info.getDeliveryDate();
        }

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

        int id = deliveryDao.addDelivery(deliveryInfo);//返回非0成功添加,返回0添加失败

        return id;
    }
}
