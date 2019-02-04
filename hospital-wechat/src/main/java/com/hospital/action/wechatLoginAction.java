package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.util.AccessTokenMgr;
import com.hospital.util.GetOpenIdOauth2;
import com.hospital.util.WechatUserMgr;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
/**
 * Created by QQQ on 2017/12/23.
 */
public class wechatLoginAction extends ActionSupport {

    String code;

    public void setCode(String code) {
        this.code = code;
    }

    private PatientService patientService;

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    private ProvinceService provinceService;


    public void setProvinceService(ProvinceService provinceService) { this.provinceService = provinceService; }

    private PatientTypeService patientTypeService;
    public void setPatientTypeService(PatientTypeService patientTypeService) {
        this.patientTypeService = patientTypeService;
    }


    private DeliveryService deliveryService;
    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public String login() {
        if (code != null) {
            AccessTokenMgr mgr = AccessTokenMgr.getInstance();
            String open_id = GetOpenIdOauth2.getOpenId(code, mgr);

            // testing
/*            if(open_id == null) {
                open_id = "oaBonw30UBjZkLW5rf19h7KunM7s";
            }*/

            if (open_id != null) {
                Patient patient = new Patient();
                patient.setOpenID(open_id);
                Patient new_patient = patientService.getPatientByOpenId(patient);

                System.out.println("openid = " + open_id);
                if (new_patient == null) {
                    System.out.println("newpatient == null");
                } else {
                    System.out.println("new patient = " + new_patient.getName() + " openid = " + new_patient.getOpenID());
                }

                if (new_patient == null) {
                    ServletActionContext.getRequest().setAttribute("appID", mgr.getAppId());
                    ServletActionContext.getRequest().setAttribute("patient", patient.getOpenID());

                    List<Province> provinceList = provinceService.getAllProvinces();
                    JSONArray jsonArray = new JSONArray();
                    for(Province province : provinceList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", province.getProvinceId().toString());
                        jsonObject.put("name", province.getName());
                        jsonArray.add(jsonObject);
                    }
                    ServletActionContext.getRequest().setAttribute("pl", jsonArray);

                    List<PatientType> patientTypeList = patientTypeService.getAllPatientType();
                    ServletActionContext.getRequest().setAttribute("ptl", patientTypeList);
                    return NONE;
                } else {
                    ServletActionContext.getRequest().setAttribute("patient", new_patient);

                    com.alibaba.fastjson.JSONObject wechatInfo = WechatUserMgr.getUserInfo(new_patient.getOpenID(), mgr);

                    String url = "";
                    String nickname = "";
                    if (wechatInfo != null) {
                        if (wechatInfo.containsKey("headimgurl")) {
                            url = wechatInfo.getString("headimgurl");
                        }
                        if (wechatInfo.containsKey("nickname")) {
                            nickname = wechatInfo.getString("nickname");
                        }
                    }
                    if (url.isEmpty()) {
                        url = "./img/scmc.jpg";
                    }

                    ServletActionContext.getRequest().setAttribute("imgUrl", url);
                    ServletActionContext.getRequest().setAttribute("nickname", nickname);

                    List<DeliveryInfo> deliveryInfos = deliveryService.getDeliveryInfosByPatientId(new_patient);

                    JSONArray emptyDeliverys = new JSONArray();
                    JSONArray answeredDeliverys = new JSONArray();
                    JSONArray overdueDeliverys = new JSONArray();

                    for(DeliveryInfo deliveryInfo : deliveryInfos) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", deliveryInfo.getDeliveryId().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = formatter.format(deliveryInfo.getDeliveryDate());

                        jsonObject.put("date", dateString);
                        jsonObject.put("name", deliveryInfo.getSurvey().getSurveyName());

                        if (deliveryInfo.getRetrieveInfo() == null) {
                            Calendar current = Calendar.getInstance();
                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(deliveryInfo.getEndDate());
                            if (current.after(endDate)) {
                                overdueDeliverys.add(jsonObject);
                            } else {
                                emptyDeliverys.add(jsonObject);
                            }
                        } else {
                            answeredDeliverys.add(jsonObject);
                        }
                    }

                    ServletActionContext.getRequest().setAttribute("emptyDeliverys", emptyDeliverys);
                    ServletActionContext.getRequest().setAttribute("answeredDeliverys", answeredDeliverys);
                    ServletActionContext.getRequest().setAttribute("overdueDeliverys", overdueDeliverys);

                    return LOGIN;
                }
            } else {
                ServletActionContext.getRequest().setAttribute("errorMsg", "用户名获取错误,请稍后再试");
                return ERROR;
            }
        }
        ServletActionContext.getRequest().setAttribute("errorMsg", "无法获取用户名");
        return ERROR;
    }

    public String getAppId() {
        String app_id = AccessTokenMgr.getInstance().getAppId();
        try {
            PrintWriter pw = ServletActionContext.getResponse().getWriter();
            pw.print(app_id);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
