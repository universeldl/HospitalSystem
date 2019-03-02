package com.hospital.action;

import com.hospital.domain.Authorization;
import com.hospital.domain.Doctor;
import com.hospital.domain.Hospital;
import com.hospital.domain.PageBean;
import com.hospital.service.AuthorizationService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class DoctorManageAction extends ActionSupport {

    private DoctorService doctorService;
    private AuthorizationService authorizationService;
    private HospitalService hospitalService;

    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private int id;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    private String name;
    private String phone;
    private String pwd;
    private Integer hospitalId;

    private int pageCode;//当前页数


    private String doctorUserName;    //查询医生用户名
    private String doctorName;//查询医生姓名

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setDoctorUserName(String doctorUserName) {
        this.doctorUserName = doctorUserName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    public String getHospitals() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");

        List<Hospital> hospitals = hospitalService.getAllVisibleHospitals();

        JSONArray jsonArray = new JSONArray();

        int idx = 0;
        for (Hospital hospital : hospitals) {
            JSONObject h = new JSONObject();
            h.put("hospitalId", hospital.getHospitalId());
            h.put("hospitalName", hospital.getName());
            jsonArray.add(idx, h);
            idx++;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hospitals", jsonArray);

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 得到指定的普通医生
     * Ajax请求该方法
     * 向浏览器返回该医生的json对象
     *
     * @return
     */
    public String getDoctor() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Doctor doctor = new Doctor();
        doctor.setAid(id);
        Doctor newDoctor = doctorService.getDoctorById(doctor);
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return name.equals("authorization") || name.equals("hospital") || name.equals("accessibleHospitals") ;
            }
        });

        JSONObject jsonObject = JSONObject.fromObject(newDoctor, jsonConfig);
        jsonObject.put("hospitalId", newDoctor.getHospital().getHospitalId());
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * 修改指定医生
     *
     * @return
     */
    public String updateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setAid(id);
        Doctor updateDoctor = doctorService.getDoctorById(doctor);//查出需要修改的医生对象
        Hospital hospital = new Hospital();
        hospital.setHospitalId(hospitalId);
        Hospital hospitalByID = hospitalService.getHospitalByID(hospital);
        int success = 0;

        if (hospitalByID == null) {
            success = -1;
        } else {
            updateDoctor.setUsername(username);
            updateDoctor.setName(name);
            updateDoctor.setPhone(phone);
            updateDoctor.setHospital(hospitalByID);

            //update accessible hospitals
            updateDoctor.getAccessibleHospitals().clear();
            List<Integer> hospitalIds = new ArrayList<>();
            ActionContext ctx = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
            Map<String, String[]> pMap = request.getParameterMap();
            int idx = 0;
            for (String[] value : pMap.values()) {
                if (idx < 5) {
                    idx++;
                } else {
                    for (String v : value) {
                        if(Integer.parseInt(v) != 0)
                            hospitalIds.add(Integer.parseInt(v));
                    }
                    idx++;
                }
            }
            for (int i = 0; i < hospitalIds.size(); i++) {
                Hospital hospital1 = new Hospital();
                hospital1.setHospitalId(hospitalIds.get(i));
                Hospital addHospital = hospitalService.getHospitalByID(hospital1);//得到医院
                updateDoctor.getAccessibleHospitals().add(addHospital);
            }

            Doctor newDoctor = doctorService.updateDoctorInfo(updateDoctor);//修改该医生
            if (newDoctor != null) {
                success = 1;
                //由于是转发并且js页面刷新,所以无需重查
            }
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 添加医生
     *
     * @return
     */
    public String addDoctor() {
        Doctor doctor = new Doctor();
        doctor.setUsername(username);
        Doctor doctor2 = doctorService.getDoctorByUserName(doctor);//按照姓名查找医生，查看用户名是否已经存在
        Hospital hospital = new Hospital();
        hospital.setHospitalId(hospitalId);
        Hospital hospitalByID = hospitalService.getHospitalByID(hospital);
        int success = 0;
        boolean b = false;
        if (hospitalByID == null ) {
            success = -1;//医院错误
        } if (doctor2 != null) {
            success = -1;//已经存在该医生
        } else {
            doctor.setName(name);
            doctor.setPhone(phone);
            doctor.setPwd(Md5Utils.md5("123456"));
            doctor.setHospital(hospitalByID);
            Authorization authorization = new Authorization();
            authorization.setDoctor(doctor);
            authorization.setPatientSet(1);
            authorization.setDeliverySet(1);
            authorization.setRetrieveSet(1);
            doctor.setAuthorization(authorization);//设置权限

            //add accessible hospitals
            List<Integer> hospitalIds = new ArrayList<>();
            ActionContext ctx = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
            Map<String, String[]> pMap = request.getParameterMap();
            int idx = 0;
            for (String[] value : pMap.values()) {
                if (idx < 4) {
                    idx++;
                } else {
                    for (String v : value) {
                        if(Integer.parseInt(v) != 0)
                            hospitalIds.add(Integer.parseInt(v));
                    }
                    idx++;
                }
            }
            for (int i = 0; i < hospitalIds.size(); i++) {
                Hospital hospital1 = new Hospital();
                hospital1.setHospitalId(hospitalIds.get(i));
                Hospital addHospital = hospitalService.getHospitalByID(hospital1);//得到医院
                b = doctor.getAccessibleHospitals().add(addHospital);
                if (!b) break;//break whenever add failing
            }

            b = doctorService.addDoctor(doctor);//添加医生,返回是否添加成功
            if (b) {
                success = 1;
            } else {
                success = 0;
            }
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);//向浏览器响应是否成功的状态码
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 根据页码查询医生
     *
     * @return
     */
    public String findDoctorByPage() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<Doctor> pb = doctorService.findDoctorByPage(pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findDoctorByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    /**
     * @return
     */
    public String queryDoctor() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<Doctor> pb = null;
        if ("".equals(doctorUserName.trim()) && "".equals(doctorName.trim())) {
            pb = doctorService.findDoctorByPage(pageCode, pageSize);
        } else {
            Doctor doctor = new Doctor();
            doctor.setUsername(doctorUserName);
            doctor.setName(doctorName);
            pb = doctorService.queryDoctor(doctor, pageCode, pageSize);

        }
        if (pb != null) {
            pb.setUrl("queryDoctor.action?doctorUserName=" + doctorUserName + "&doctorName=" + doctorName + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }

    /**
     * 删除指定医生
     *
     * @return
     */
    public String deleteDoctor() {
        Doctor doctor = new Doctor();
        doctor.setAid(id);
        boolean deleteDoctor = doctorService.deleteDoctor(doctor);
        int success = 0;
        if (deleteDoctor) {
            success = 1;
            //由于是转发并且js页面刷新,所以无需重查
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }


    public String getAllDoctors() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<Doctor> allDoctors = doctorService.getAllDoctors();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return name.equals("authorization") || name.equals("hospital") || name.equals("accessibleHospitals") ;
            }
        });

        String json = JSONArray.fromObject(allDoctors, jsonConfig).toString();//List------->JSONArray
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}
