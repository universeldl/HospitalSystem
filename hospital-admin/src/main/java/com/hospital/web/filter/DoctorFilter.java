package com.hospital.web.filter;

import com.hospital.domain.Doctor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class DoctorFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//根据session中的用户对象的Type属性来判断是否为医生
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		//从session中获取用户对象
		Object obj =  session.getAttribute("doctor");
		if(obj!=null && obj instanceof Doctor){
			//用户为医生,放行
			chain.doFilter(request, response);
			
		}else{
			resp.sendRedirect(req.getContextPath()+"/doctorLogin.jsp");
		}
		
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}