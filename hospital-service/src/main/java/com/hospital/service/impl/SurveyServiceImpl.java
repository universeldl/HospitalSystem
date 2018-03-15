package com.hospital.service.impl;

import com.hospital.dao.DeliveryDao;
import com.hospital.dao.ForfeitDao;
import com.hospital.dao.SurveyDao;
import com.hospital.dao.SurveyTypeDao;
import com.hospital.domain.*;
import com.hospital.service.SurveyService;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SurveyServiceImpl implements SurveyService {

    private SurveyDao surveyDao;

    private SurveyTypeDao surveyTypeDao;
    private DeliveryDao deliveryDao;
    private ForfeitDao forfeitDao;

    public void setDeliveryDao(DeliveryDao deliveryDao) {
        this.deliveryDao = deliveryDao;
    }


    /**
     * @param surveyTypeDao the surveyTypeDao to set
     */
    public void setSurveyTypeDao(SurveyTypeDao surveyTypeDao) {
        this.surveyTypeDao = surveyTypeDao;
    }

    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
    }

    public void setSurveyDao(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    @Override
    public PageBean<Survey> findSurveyByPage(int pageCode, int pageSize) {
        // TODO Auto-generated method stub
        return surveyDao.findSurveyByPage(pageCode, pageSize);
    }

    @Override
    public boolean addSurvey(Survey survey) {
        // TODO Auto-generated method stub
        return surveyDao.addSurvey(survey);
    }

    @Override
    public boolean addQuestion(Question question) {
        // TODO Auto-generated method stub
        return surveyDao.addQuestion(question);
    }

    @Override
    public Survey getSurveyById(Survey survey) {
        // TODO Auto-generated method stub
        return surveyDao.getSurveyById(survey);
    }

    @Override
    public Survey updateSurveyInfo(Survey updateSurvey) {
        // TODO Auto-generated method stub
        return surveyDao.updateSurveyInfo(updateSurvey);
    }

    @Override
    public PageBean<Survey> querySurvey(Survey survey, int pageCode, int pageSize) {
        // TODO Auto-generated method stub
        return surveyDao.querySurvey(survey, pageCode, pageSize);
    }

    @Override
    public int deleteSurvey(Survey survey) {
        // TODO Auto-generated method stub
        //删除问卷需要注意的事项：如果该答卷有尚未答卷的记录或者尚未设置的延期记录,则不能删除
        //得到该答卷的分发记录
        List<DeliveryInfo> deliveryInfos = deliveryDao.getDeliveryInfoBySurvey(survey);
        for (DeliveryInfo deliveryInfo : deliveryInfos) {
            if (!(deliveryInfo.getState() == 2 || deliveryInfo.getState() == 5)) {
                return -1;//该答卷还在分发中,无法删除
            }
            //得到该分发记录的提醒信息
            ForfeitInfo forfeitInfo = new ForfeitInfo();
            forfeitInfo.setDeliveryId(deliveryInfo.getDeliveryId());
            ForfeitInfo forfeitInfoById = forfeitDao.getForfeitInfoById(forfeitInfo);
            if (forfeitInfoById != null) {
                if (forfeitInfoById.getIsPay() == 0) {
                    return -2;//尚未设置的延期
                }
            }
        }
        boolean deleteSurvey = surveyDao.deleteSurvey(survey);
        if (deleteSurvey) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<Survey> findAllSurveys() {
        // TODO Auto-generated method stub
        return surveyDao.findAllSurveys();
    }


    @Override
    public JSONObject batchAddSurvey(String fileName, Doctor doctor) {
        List<Survey> surveys = new ArrayList<Survey>();
        List<Survey> failSurveys = new ArrayList<Survey>();
        String str[] = new String[]{"问卷编号", "问卷类型", "问卷名称", "作者名称", "科室", "价格", "数量", "允许答卷最长天数", "描述", "用户注册时发送"};
        // TODO Auto-generated method stub
        String realPath = ServletActionContext.getServletContext().getRealPath(fileName);
        //创建workbook
        try {
            Workbook workbook = Workbook.getWorkbook(new File(realPath));
            //获取第一个工作表sheet
            Sheet sheet = workbook.getSheet(0);

            if (sheet.getColumns() != 8) {
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


                String type = sheet.getCell(0, i).getContents().trim();
                String surveyName = sheet.getCell(1, i).getContents().trim();
                String author = sheet.getCell(2, i).getContents().trim();
                String publish = sheet.getCell(3, i).getContents().trim();
                String num = sheet.getCell(4, i).getContents().trim();
                String bday = sheet.getCell(5, i).getContents().trim();
                String description = sheet.getCell(6, i).getContents().trim();
                String sendOnRegister = sheet.getCell(7, i).getContents().trim();

                if ("".equals(type) && "".equals(surveyName) && "".equals(author) && "".equals(publish) && "".equals(num) && "".equals(description)) {
                    //说明这条数据是空的
                    continue;
                }

                Survey survey = new Survey();
                survey.setSurveyName(surveyName);
                survey.setAuthor(author);
                survey.setDescription(description);
                survey.setDepartment(publish);
                SurveyType surveyType = new SurveyType();
                surveyType.setTypeName(type);
                survey.setSurveyType(surveyType);

                try {
                    if (Integer.parseInt(bday) <= 0) {
                        //说明不是正整数
                        survey.setNum(-1);
                        failSurveys.add(survey);
                        continue;
                    }
                    survey.setNum(Integer.parseInt(bday));
                    survey.setCurrentNum(Integer.parseInt(bday));
                } catch (NumberFormatException e) {
                    //说明不是整数
                    survey.setNum(-1);
                    failSurveys.add(survey);
                    continue;
                }

                try {
                    if (Integer.parseInt(num) <= 0) {
                        //说明不是正整数
                        survey.setNum(-1);
                        failSurveys.add(survey);
                        continue;
                    }
                    survey.setNum(Integer.parseInt(num));
                    survey.setCurrentNum(Integer.parseInt(num));
                } catch (NumberFormatException e) {
                    //说明不是整数
                    survey.setNum(-1);
                    failSurveys.add(survey);
                    continue;
                }


                if (sendOnRegister.equals("是")) {
                    survey.setSendOnRegister(true);
                } else {
                    survey.setSendOnRegister(false);
                }

                if ("".equals(surveyName) || "".equals(type)) {
                    //要是前2列有一列没有数据，说明这条数据是非法的
                    //保存这条非法数据
                    failSurveys.add(survey);
                    continue;
                }

                //需要根据类型名称找到对应的病人类型
                SurveyType typeByName = surveyTypeDao.getSurveyTypeByName(surveyType);
                if (typeByName == null) {
                    //找不到这个类型，就说明这条数据非法,跳出循环
                    //保存这条非法数据
                    surveyType.setTypeName(surveyType.getTypeName() + "(没有该类型)");
                    failSurveys.add(survey);
                    continue;
                }

                survey.setSurveyType(typeByName);
                //有这个类型的病人,接下来就是封装数据，准备进行批量添加
                survey.setPutdate(new Date(System.currentTimeMillis()));
                survey.setDoctor(doctor);
                surveys.add(survey);
            }
            workbook.close();
            int success = surveyDao.batchAddSurvey(surveys, failSurveys);

            JSONObject jsonObject = new JSONObject();
            if (failSurveys.size() != 0) {
                //把不成功的导出成excel文件
                String exportExcel = exportExcel(failSurveys);
                jsonObject.put("state", "2");
                jsonObject.put("message", "成功" + success + "条,失败" + failSurveys.size() + "条");
                jsonObject.put("failPath", "doctor/FileDownloadAction.action?fileName=" + exportExcel);
                return jsonObject;
            } else {
                jsonObject.put("state", "1");
                jsonObject.put("message", "成功" + success + "条,失败" + failSurveys.size() + "条");
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
     * @param failSurveys
     * @return 返回文件路径
     */
    public String exportExcel(List<Survey> failSurveys) {
        //用数组存储表头
        String[] title = {"问卷类型", "问卷名称", "作者名称", "科室", "数量", "允许答卷最长天数", "描述", "用户注册时发送"};
        String path = ServletActionContext.getServletContext().getRealPath("/download");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Calendar cal = Calendar.getInstance();
        String time = simpleDateFormat.format(cal.getTime());
        String fileName = time + "_failSurveys.xls";
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
            for (int i = 1; i <= failSurveys.size(); i++) {
                label = new Label(0, i, failSurveys.get(i - 1).getSurveyType().getTypeName());
                sheet.addCell(label);
                label = new Label(1, i, failSurveys.get(i - 1).getSurveyName());
                sheet.addCell(label);
                label = new Label(2, i, failSurveys.get(i - 1).getAuthor());
                sheet.addCell(label);
                label = new Label(3, i, failSurveys.get(i - 1).getDepartment());
                sheet.addCell(label);

                if (failSurveys.get(i - 1).getNum() == null) {
                    label = new Label(4, i, "数据错误");
                    sheet.addCell(label);
                } else if (failSurveys.get(i - 1).getNum().equals(-1)) {
                    label = new Label(4, i, "数据错误");
                    sheet.addCell(label);
                } else {
                    label = new Label(4, i, failSurveys.get(i - 1).getNum().toString());
                    sheet.addCell(label);
                }

                if (failSurveys.get(i - 1).getBday() == null) {
                    label = new Label(5, i, "数据错误");
                    sheet.addCell(label);
                } else if (failSurveys.get(i - 1).getBday().equals(-1)) {
                    label = new Label(5, i, "数据错误");
                    sheet.addCell(label);
                } else {
                    label = new Label(5, i, failSurveys.get(i - 1).getBday().toString());
                    sheet.addCell(label);
                }

                label = new Label(5, i, failSurveys.get(i - 1).getDescription());
                sheet.addCell(label);
                label = new Label(9, i, (failSurveys.get(i - 1).isSendOnRegister()?"是":"否"));
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


    @Override
    public String exportSurvey() {
        List<Survey> findAllSurveys = surveyDao.findAllSurveys();
        String exportSurveyExcel = exportSurveyExcel(findAllSurveys);
        return "doctor/FileDownloadAction.action?fileName=" + exportSurveyExcel;
    }


    /**
     * 导出所有的问卷excel文件
     *
     * @param surveys
     * @return 返回文件路径
     */
    public String exportSurveyExcel(List<Survey> surveys) {
        //用数组存储表头
        String[] title = {"问卷类型", "问卷名称", "作者名称", "科室", "数量", "允许答卷最长天数", "总回收数", "生成时间", "操作医生", "描述", "用户注册时发送"};
        String path = ServletActionContext.getServletContext().getRealPath("/download");
        String fileName = +System.currentTimeMillis() + "_allSurveys.xls";
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
            for (int i = 1; i <= surveys.size(); i++) {
                label = new Label(0, i, surveys.get(i - 1).getSurveyType().getTypeName());
                sheet.addCell(label);
                label = new Label(1, i, surveys.get(i - 1).getSurveyName());
                sheet.addCell(label);
                label = new Label(2, i, surveys.get(i - 1).getAuthor());
                sheet.addCell(label);
                label = new Label(3, i, surveys.get(i - 1).getDepartment());
                sheet.addCell(label);
                label = new Label(4, i, surveys.get(i - 1).getNum().toString());
                sheet.addCell(label);
                label = new Label(5, i, surveys.get(i - 1).getBday().toString());
                sheet.addCell(label);
                label = new Label(6, i, surveys.get(i - 1).getCurrentNum().toString());
                sheet.addCell(label);
                label = new Label(7, i, surveys.get(i - 1).getPutdate().toLocaleString());
                sheet.addCell(label);
                label = new Label(8, i, surveys.get(i - 1).getDoctor().getName());
                sheet.addCell(label);
                label = new Label(9, i, surveys.get(i - 1).getDescription());
                sheet.addCell(label);
                label = new Label(19, i, (surveys.get(i - 1).isSendOnRegister()?"是":"否"));
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

}
