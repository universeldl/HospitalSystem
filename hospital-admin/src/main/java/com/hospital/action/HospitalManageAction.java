package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class HospitalManageAction extends ActionSupport {

    private Integer provinceId;
    private String provinceName;
    private Integer cityId;
    private String cityName;
    private Integer hospitalId;
    private String hospitalName;
    private ProvinceService provinceService;
    private CityService cityService;
    private HospitalService hospitalService;

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    public void setProvinceService(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    public void deleteHospital() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (hospitalId == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Hospital tmpHospital = new Hospital();
        tmpHospital.setHospitalId(hospitalId);
        Hospital hospital = hospitalService.getHospitalByID(tmpHospital);
        if (hospital == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        boolean state = hospitalService.deleteHospital(hospital);

        try {
            response.getWriter().print(state?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    public void deleteCity() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (cityId == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        City tmpCity = new City();
        tmpCity.setCityId(cityId);
        City city = cityService.getCityByID(tmpCity);
        if (city == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        boolean state = cityService.deleteCity(city);
        try {
            response.getWriter().print(state?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    public void deleteProvince() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (provinceId == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province tmpProvince = new Province();
        tmpProvince.setProvinceId(provinceId);
        Province province = provinceService.getProvinceByID(tmpProvince);

        if (province == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        boolean state = provinceService.deleteProvince(province);
        try {
            response.getWriter().print(state?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }


    public void updateHospital() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (hospitalId == null || cityId == null || hospitalName == null
                || hospitalName.isEmpty()) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Hospital tmpHospital = new Hospital();
        tmpHospital.setName(hospitalName);
        Hospital tmpHospital2 = hospitalService.getHospitalByName(tmpHospital);
        if (tmpHospital2 != null && !tmpHospital2.getHospitalId().equals(hospitalId)) {
            try {
                response.getWriter().print(-2);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        tmpHospital.setHospitalId(hospitalId);
        Hospital hospital = hospitalService.getHospitalByID(tmpHospital);
        if (hospital == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        City tmpCity = new City();
        tmpCity.setCityId(cityId);
        City city = cityService.getCityByID(tmpCity);
        if (city == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        hospital.setName(hospitalName);
        hospital.setCity(city);
        Hospital newHospital = hospitalService.updateHospital(hospital);

        try {
            response.getWriter().print((newHospital!=null)?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }


    public void updateCity() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (provinceId == null || cityId == null || cityName == null
                || cityName.isEmpty()) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        City tmpCity = new City();
        tmpCity.setName(cityName);
        City tmpCity2 = cityService.getCityByName(tmpCity);

        if (tmpCity2 != null && (!tmpCity2.getCityId().equals(cityId))) {
            try {
                response.getWriter().print(-2);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        tmpCity.setCityId(cityId);
        City city = cityService.getCityByID(tmpCity);
        if (city == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province tmpProvince = new Province();
        tmpProvince.setProvinceId(provinceId);
        Province province = provinceService.getProvinceByID(tmpProvince);
        if (province == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        city.setName(cityName);
        city.setProvince(province);
        City newCity = cityService.updateCity(city);

        try {
            response.getWriter().print((newCity!=null)?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    public void updateProvince() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (provinceId == null || provinceName == null || provinceName.isEmpty() ) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province tmpProvince1 = new Province();
        tmpProvince1.setName(provinceName);
        Province tmpProvince2 = provinceService.getProvinceByName(tmpProvince1);
        if (tmpProvince2 != null) {
            try {
                response.getWriter().print(-2);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province tmpProvince = new Province();
        tmpProvince.setProvinceId(provinceId);
        Province province = provinceService.getProvinceByID(tmpProvince);

        if (province == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        if (province.getName().equals(provinceName)) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        province.setName(provinceName);
        Province newProvince = provinceService.updateProvince(province);
        try {
            response.getWriter().print((newProvince!=null)?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }


    public String findHospitals() {
        HttpServletRequest request = ServletActionContext.getRequest();

        List<Province> provinces = provinceService.getAllProvinces();
        List<City> cities = cityService.getAllCities();
        List<Hospital> hospitals = hospitalService.getAllVisibleHospitals();

        request.setAttribute("provinces", provinces);
        request.setAttribute("cities", cities);
        request.setAttribute("hospitals", hospitals);
        return SUCCESS;
    }

    public void getCityById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (cityId == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        City tmpCity = new City();
        tmpCity.setCityId(cityId);

        City city = cityService.getCityByID(tmpCity);
        if (city == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", city.getCityId().toString());
        jsonObject.put("name", city.getName());

        if (city.getProvince() != null) {
            jsonObject.put("provinceId", city.getProvince().getProvinceId());
        } else {
            jsonObject.put("provinceId", -1);

        }
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    public void getHospitalById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (hospitalId == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Hospital tmpHospital = new Hospital();
        tmpHospital.setHospitalId(hospitalId);
        Hospital hospital = hospitalService.getHospitalByID(tmpHospital);
        if (hospital == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", hospital.getHospitalId());
        jsonObject.put("name", hospital.getName());

        City city = hospital.getCity();
        if (city != null) {
            jsonObject.put("cityId", hospital.getCity().getCityId());

            Province province = city.getProvince();
            if (province != null) {
                jsonObject.put("provinceId", province.getProvinceId());
            } else {
                jsonObject.put("provinceId", -1);
            }
        } else {
            jsonObject.put("cityId", -1);
            jsonObject.put("provinceId", -1);
        }

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;

    }

    public void addHospital() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (provinceId == null || cityId == null || hospitalName == null ||
                hospitalName.isEmpty()) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }
        Hospital tmpHospital = new Hospital();
        tmpHospital.setName(hospitalName);
        Hospital duplicate = hospitalService.getHospitalByName(tmpHospital);
        if (duplicate != null) {
            try {
                response.getWriter().print(-2);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province tmpProvince = new Province();
        tmpProvince.setProvinceId(provinceId);
        Province province = provinceService.getProvinceByID(tmpProvince);
        if (province == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        City tmpCity = new City();
        tmpCity.setCityId(cityId);
        City city = cityService.getCityByID(tmpCity);
        if (city == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        if (!city.getProvince().getProvinceId().equals(province.getProvinceId())) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Hospital hospital = new Hospital();
        hospital.setName(hospitalName);
        hospital.setCity(city);
        hospital.setVisible(true);
        boolean state = hospitalService.addHospital(hospital);
        try {
            response.getWriter().print(state?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    public void findCityByProvinceID() {
        if (provinceId != null) {
            Province tmpProvince = new Province();
            tmpProvince.setProvinceId(provinceId);
            Province province = provinceService.getProvinceByID(tmpProvince);
            Set<City> cites = province.getCities();
            JSONArray jsonArray = new JSONArray();
            for(City city : cites) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", city.getCityId().toString());
                jsonObject.put("name", city.getName());
                jsonArray.add(jsonObject);
            }
            try {
                HttpServletResponse response = ServletActionContext.getResponse();
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(jsonArray.toString());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return;
    }

    public void getProvinceById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        if (provinceId == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province tmpProvince = new Province();
        tmpProvince.setProvinceId(provinceId);

        Province province = provinceService.getProvinceByID(tmpProvince);
        if (province == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", province.getProvinceId().toString());
        jsonObject.put("name", province.getName());

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    public void addCity() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");

        if (provinceId == null || cityName == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province tmpProvince = new Province();
        tmpProvince.setProvinceId(provinceId);
        Province province = provinceService.getProvinceByID(tmpProvince);
        if (province == null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        City tmpCity = new City();
        tmpCity.setName(cityName);
        City city = cityService.getCityByName(tmpCity);

        if (city != null) {
            try {
                response.getWriter().print(-2);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        tmpCity.setProvince(province);

        boolean state = cityService.addCity(tmpCity);
        try {
            response.getWriter().print(state?1:-1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;

    }

    public void addProvince() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");

        if (provinceName.isEmpty()) {
            try {
                response.getWriter().print(-2);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        Province province = new Province();
        province.setName(provinceName);

        Province duplicate = provinceService.getProvinceByName(province);
        if (duplicate != null) {
            try {
                response.getWriter().print(-1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            return;
        }

        boolean success = provinceService.addProvince(province);
        try {
            response.getWriter().print((success?1:-2));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

}
