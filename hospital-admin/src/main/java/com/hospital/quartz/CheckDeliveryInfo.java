package com.hospital.quartz;

import com.hospital.service.DeliveryService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时任务
 *
 * @author c
 */
public class CheckDeliveryInfo extends QuartzJobBean {

    private DeliveryService deliveryService;

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public CheckDeliveryInfo() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException {
        boolean checkDeliveryInfo = true;
        try {
            checkDeliveryInfo = deliveryService.checkDeliveryInfo();
        } catch (Throwable e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (!checkDeliveryInfo) {
            System.err.println("定时检查分发表逾期出现了错误!!!");
        }

    }


}
