package com.hospital.quartz;

import com.hospital.domain.Doctor;
import com.hospital.domain.Patient;
import com.hospital.domain.PatientType;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.service.DeliveryService;
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
public class SendSurvey extends QuartzJobBean {

    private PatientService patientService;

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    private DeliveryService deliveryService;

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    private DoctorService doctorService;

    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public SendSurvey() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException {
        boolean checkDeliveryInfo = true;
        try {
            checkDeliveryInfo = deliveryService.checkAndDoDelivery2();
        } catch (Throwable e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (!checkDeliveryInfo) {
            System.err.println("定时分发随访问卷出现了错误!!!");
        }
    }
}
