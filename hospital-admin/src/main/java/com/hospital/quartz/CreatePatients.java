package com.hospital.quartz;

import com.hospital.domain.Doctor;
import com.hospital.domain.Patient;
import com.hospital.domain.PatientType;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.util.Md5Utils;
import org.apache.commons.lang.RandomStringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;


/**
 * 定时任务
 * This file is only for testing Quartz and Charts.
 * @author c
 */
public class CreatePatients extends QuartzJobBean {

    private PatientService patientService;

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    private DoctorService doctorService;

    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public CreatePatients() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException {
        //boolean createPatients = true;
        ////得到当前时间
        //Date createTime = new Date(System.currentTimeMillis());
        //Doctor tmpDoctor = new Doctor();
        //tmpDoctor.setAid(6);
        //Doctor doctor = doctorService.getDoctorById(tmpDoctor);
        ////得到当前病人类型
        //PatientType type = new PatientType();
        //type.setPatientTypeId(1);
        //Patient patient = new Patient("testPatient", Md5Utils.md5("123456"), "12312312312", type,
        //        "testEmail@123.com", doctor, RandomStringUtils.randomAlphanumeric(10), createTime,
        //        Integer.valueOf(RandomStringUtils.random(1, "01")));//1位随机数:1或者0

        //Patient oldPatient = patientService.getPatientByopenID(patient);//检查是否已经存在该openID的病人
        //int success = 0;
        //if (oldPatient != null) {
        //    success = -1;//已存在该id
        //} else {
        //    try {
        //        createPatients = patientService.addPatient(patient);
        //    } catch (Throwable e) {
        //        // TODO: handle exception
        //        e.printStackTrace();
        //    }
        //}
        //if (!createPatients) {
        //    System.err.println("定时生成病人信息出现了错误!!!");
        //}
    }
}
