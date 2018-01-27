package com.hospital.service.impl;

import com.hospital.dao.ForfeitDao;
import com.hospital.dao.PatientDao;
import com.hospital.dao.PatientTypeDao;
import com.hospital.domain.*;
import com.hospital.service.PatientService;
import com.hospital.util.CheckUtils;
import com.hospital.util.Md5Utils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class PatientServiceImpl implements PatientService {

    private PatientDao patientDao;

    private PatientTypeDao patientTypeDao;

    private ForfeitDao forfeitDao;


    public void setForfeitDao(ForfeitDao forfeitDao) {
        this.forfeitDao = forfeitDao;
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
    public List<Patient> getPatientsByDoctor(Doctor doctor) {
        return patientDao.getPatientsByDoctor(doctor);
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
    public PageBean<Patient> findPatientByPage(int pageCode, int pageSize) {
        return patientDao.findPatientByPage(pageCode, pageSize);
    }


    @Override
    public Patient getPatientById(Patient patient) {
        return patientDao.getPatientById(patient);
    }


    @Override
    public int deletePatient(Patient patient) {
        //删除病人需要注意的点：如果该病人有尚未答卷的问卷或者尚未设置的延期,则不能删除
        //得到该病人的分发集合
        Patient patientById = patientDao.getPatientById(patient);
        Set<DeliveryInfo> deliveryInfos = patientById.getDeliveryInfos();
        for (DeliveryInfo deliveryInfo : deliveryInfos) {
            if (!(deliveryInfo.getState() == 2 || deliveryInfo.getState() == 5)) {
                return -1;//有尚未答卷的问卷
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
        boolean deletePatient = patientDao.deletePatient(patient);
        if (deletePatient) {
            return 1;
        }
        return 0;
    }


    @Override
    public PageBean<Patient> queryPatient(Patient patient, int pageCode, int pageSize) {
        return patientDao.queryPatient(patient, pageCode, pageSize);
    }


    @Override
    public Patient getPatientByopenID(Patient patient) {
        // TODO Auto-generated method stub
        return patientDao.getPatientByopenID(patient);
    }


    @Override
    public Patient getPatientByOpenID(Patient patient) {
        // TODO Auto-generated method stub
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
        String[] title = {"用户名码", "姓名", "病人类型", "邮箱", "联系方式"};
        String path = ServletActionContext.getServletContext().getRealPath("/download");
        String fileName = +System.currentTimeMillis() + "_" + name;
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
                label = new Label(2, i, failPatients.get(i - 1).getPatientType().getPatientTypeName());
                sheet.addCell(label);
                label = new Label(3, i, failPatients.get(i - 1).getEmail());
                sheet.addCell(label);
                label = new Label(4, i, failPatients.get(i - 1).getPhone());
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
    public String exportPatient() {
        List<Patient> findAllPatients = patientDao.findAllPatients();
        String exportPatientExcel = exportExcel(findAllPatients, "allPatients.xls");
        return "doctor/FileDownloadAction.action?fileName=" + exportPatientExcel;
    }


}