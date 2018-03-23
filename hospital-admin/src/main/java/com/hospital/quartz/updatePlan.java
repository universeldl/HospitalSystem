package com.hospital.quartz;

import com.hospital.service.DeliveryService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 定时任务
 * This file is only for testing Quartz and Charts.
 * @author c
 */
public class updatePlan extends QuartzJobBean {

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

    public updatePlan() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException {
        boolean updatePlan = true;
        try {
            updatePlan = patientService.updatePlan();
        } catch (Throwable e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (!updatePlan) {
            System.err.println("定时更新病人随访计划出现了错误!!!");
        }
    }
}
