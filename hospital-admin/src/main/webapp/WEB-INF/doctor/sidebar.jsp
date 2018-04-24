<%--
  Created by IntelliJ IDEA.
  User: QQQ
  Date: 2018/4/22
  Time: 上午11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

<header class="main-header"><a href="javascript:void(0);" class="logo"><span class="logo-mini"><b>后台</b></span>
  <span class="logo-lg"><b>呼吸天使问卷管理系统</b></span></a>
  <nav role="navigation" class="navbar navbar-static-top"><a href="#" data-toggle="offcanvas" role="button"
                                                             class="sidebar-toggle"><span class="sr-only">Toggle navigation</span></a>
    <div style="float: left; color: rgb(255, 255, 255); padding: 15px 10px;">欢迎您， <s:property
            value="#session.doctor.name"/>医生
    </div>
    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
        <li><a href="#updateinfo" data-toggle="modal"><i class="fa fa-user"></i> 个人资料</a></li>
        <li><a href="#updatepwd" data-toggle="modal"><i class="fa fa-lock"></i> 修改密码</a></li>
        <li><a href="${pageContext.request.contextPath}/doctorLoginAction_logout.action"><i
                class="fa fa-sign-out"></i> 退出系统</a></li>
      </ul>
    </div>
  </nav>
</header>
<aside class="main-sidebar">
  <section class="sidebar" style="height: auto;">
    <ul class="sidebar-menu">
      <li class="header">导航菜单</li>
      <s:if test="#session.doctor.authorization.surveySet==1">
        <li class="active"><a href="javascript:;"><i class="fa fa-file-text"></i><span>问卷管理</span><i
                class="fa fa-angle-left pull-right"></i></a>
          <ul class="treeview-menu menu-open" style="display: block;">
            <li>
              <a href="${pageContext.request.contextPath}/doctor/surveyManageAction_findSurveyByPage.action"><i
                      class="fa fa-list-ol"></i> 问卷列表</a></li>
            <s:if test="#session.doctor.authorization.typeSet==1">
              <li>
                <a href="${pageContext.request.contextPath}/doctor/surveyTypeManageAction_findSurveyTypeByPage.action"><i
                        class="fa fa-files-o"></i> 问卷分类</a></li>
            </s:if>
          </ul>
        </li>
      </s:if>
      <s:if test="#session.doctor.authorization.planSet==1">
        <li class="active"><a href="javascript:;"><i class="fa fa-files-o"></i><span>随访管理</span><i
                class="fa fa-angle-left pull-right"></i></a>
          <ul class="treeview-menu menu-open" style="display: block;">
            <li>
              <a href="${pageContext.request.contextPath}/doctor/planManageAction_getAllPlan.action"><i
                      class="fa fa-list"></i> 随访设置</a></li>
          </ul>
        </li>
      </s:if>
      <s:if test="#session.doctor.authorization.patientSet==1">
        <li class="active"><a href="javascript:;"><i class="fa fa-wheelchair"></i><span>病人管理</span><i
                class="fa fa-angle-left pull-right"></i></a>
          <ul class="treeview-menu menu-open" style="display: block;">
            <li>
              <a href="${pageContext.request.contextPath}/doctor/patientManageAction_findPatientByPage.action"><i
                      class="fa fa-medkit"></i> 病人列表</a></li>
            <li>
              <a href="${pageContext.request.contextPath}/doctor/deliveryManageAction_findDeliveryInfoByPage.action"><i
                      class="fa fa-send-o"></i> 随访信息</a></li>
          </ul>
        </li>
      </s:if>
      <s:if test="#session.doctor.authorization.superSet==1"><!-- 对超级医生和普通医生进行权限区分 -->
      <li class="active"><a href="javascript:;"><i class="fa fa-user-md"></i><span>医生管理</span><i
              class="fa fa-angle-left pull-right"></i></a>
        <ul class="treeview-menu menu-open" style="display: block;">
          <li>
            <a href="${pageContext.request.contextPath}/doctor/doctorManageAction_findDoctorByPage.action"><i
                    class="fa fa-stethoscope"></i> 医生列表</a></li>
        </ul>
        <ul class="treeview-menu menu-open" style="display: block;">
          <li>
            <a href="${pageContext.request.contextPath}/doctor/hospitalManageAction_findHospitals.action"><i
                    class="fa fa-hospital-o"></i> 医院管理</a></li>
        </ul>
      </li>
      </s:if>
      <s:if test="#session.doctor.authorization.sysSet==1">
        <li class="active"><a href="javascript:;"><i class="fa fa-cog"></i><span>系统设置</span><i
                class="fa fa-angle-left pull-right"></i></a>
          <ul class="treeview-menu menu-open" style="display: block;">
            <li>
              <a href="${pageContext.request.contextPath}/doctor/patientTypeManageAction_getAllPatientType.action"><i
                      class="fa fa-wrench"></i> 配置管理</a></li>
          </ul>
        </li>
      </s:if>
    </ul>
  </section>
</aside>