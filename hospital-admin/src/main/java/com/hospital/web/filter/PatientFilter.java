package com.hospital.web.filter;

import com.hospital.domain.Patient;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class PatientFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        //根据session中的用户对象的Type属性来判断是否为病人
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        //从session中获取用户对象
        Object obj = session.getAttribute("patient");
        if (obj != null && obj instanceof Patient) {
            //用户为医生,放行
            chain.doFilter(request, response);

        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }


    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
