<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>呼吸天使问卷管理系统</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/all-skins.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script src="${pageContext.request.contextPath}/jQuery/jquery-3.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-dropdown.min.js"></script>

    <script src="${pageContext.request.contextPath}/ajax-lib/ajaxutils.js"></script>
    <script src="${pageContext.request.contextPath}/js/doctorUpdateInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/doctorUpdatePwd.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>

    <!-- add specific js in here -->
    <script src="${pageContext.request.contextPath}/js/updatePlan.js"></script>
    <script src="${pageContext.request.contextPath}/js/addPlan.js"></script>
    <script src="${pageContext.request.contextPath}/js/deletePlan.js"></script>
    <!-- add specific js in here -->

    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>

<body class="skin-blue sidebar-mini">
<!-- Site wrapper -->
<div id="rrapp" class="wrapper">
    <%@include file="sidebar.jsp"%>

<%--    <header class="main-header"><a href="javascript:void(0);" class="logo"><span class="logo-mini"><b>后台</b></span>
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
    </aside>--%>
    <div class="content-wrapper" style="min-height: 800px;">
        <section class="content-header">
            <ol id="nav_title" class="breadcrumb" style="position: static; float: none;">
                <li class="active"><i class="fa fa-home"
                                      style="font-size: 20px; position: relative; top: 2px; left: -3px;"></i> 首页
                </li>
                <li class="active">控制台</li>
            </ol>
        </section>
        <section class="content" style="background: rgb(255, 255, 255); height: auto;">
            <!-- <h2>Hello World!</h2> -->
            <div name="showList">
                <div class="grid-btn">
                    <a class="btn btn-primary" id="btn_add" data-toggle="modal" data-target="#addModal"><i
                            class="fa fa-plus"></i> 添加随访计划</a>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <table id="data_list" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>随访计划编号</th>
                            <th>病人类型</th>
                            <th>病人性别</th>
                            <th>年龄范围最小值（含）</th>
                            <th>年龄范围最大值（含）</th>
                            <th>病例类型</th>
                            <th>随访问卷</th>
                            <th>随访激活状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>


                        <!---在此插入信息-->
                        <s:if test="#request.plans!=null">
                            <s:iterator value="#request.plans" var="plan">
                                <tbody>
                                <td><s:property value="#plan.planId"/></td>
                                <td><s:property value="#plan.patientType.patientTypeName"/></td>

                                <td>
                                    <s:if test="#plan.sex == 1">
                                        男
                                    </s:if>
                                    <s:elseif test="#plan.sex == 2">
                                        女
                                    </s:elseif>
                                    <s:elseif test="#plan.sex == 3">
                                        不限
                                    </s:elseif>
                                </td>
                                <td><s:property value="#plan.beginAge"/></td>
                                <td><s:property value="#plan.endAge"/></td>
                                <td>
                                <s:if test="#plan.oldPatient == 1">
                                    仅限新病例
                                </s:if>
                                <s:elseif test="#plan.oldPatient == 2">
                                    仅限既往病例
                                </s:elseif>
                                <s:elseif test="#plan.oldPatient == 3">
                                    仅限哮喘无忧用户
                                </s:elseif>
                                <s:elseif test="#plan.oldPatient == 4">
                                    不限
                                </s:elseif>
                                </td>
                                <td>
                                    <s:iterator value="#plan.surveys" var="survey">
                                        <p><s:property value="#survey.surveyName"/></p>
                                    </s:iterator>
                                </td>
                                <td>
                                    <s:if test="#plan.active == 1">
                                        激活
                                    </s:if>
                                    <s:elseif test="#plan.active == 0">
                                        未激活
                                    </s:elseif>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                            data-target="#updateModal" onclick="updatePlan(<s:property
                                            value="#plan.planId"/>)">修改
                                    </button>
                                    <button type="button" class="btn btn-danger btn-xs"
                                            onclick="deletePlan(<s:property value="#plan.planId"/>)">删除
                                    </button>
                                </td>
                                </tbody>
                            </s:iterator>
                        </s:if>
                        <s:else>
                            <tbody>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            <td>暂无数据</td>
                            </tbody>
                        </s:else>

                    </table>

                </div>
            </div>


        </section>
    </div>
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            Version 1.0.0
        </div>
        Copyright © 2017 <a href="http://www.scmc.com.cn/" target="_blank">上海儿童医学中心</a> All Rights Reserved
    </footer>
</div>
<!-- ./wrapper -->


<!--------------------------------------添加的模糊框------------------------>
<form class="form-horizontal" id="addForm">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        添加新随访计划
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->

                    <div class="form-group">
                        <label for="addBeginAge" class="col-sm-3 control-label">年龄范围最小值（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addBeginAge" placeholder="请输入年龄范围最小值">
                            <label class="control-label" for="addBeginAge" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="addEndAge" class="col-sm-3 control-label">年龄范围最大值（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addEndAge" placeholder="请输入年龄范围最大值">
                            <label class="control-label" for="addEndAge" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="addOldPatient" class="col-sm-3 control-label">病例类型</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addOldPatient">
                                <option value="-1">请选择</option>
                                <option value="1">新病例</option>
                                <option value="2">既往病例</option>
                                <option value="3">原哮喘无忧用户</option>
                                <option value="4">不限</option>
                            </select>
                            <label class="control-label" for="addOldPatient" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="addSex" class="col-sm-3 control-label">性别</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addSex">
                                <option value="-1">请选择</option>
                                <option value="1">男</option>
                                <option value="2">女</option>
                                <option value="3">不限</option>
                            </select>
                            <label class="control-label" for="addSex" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="addpatientType" class="col-sm-3 control-label">病人类型</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addpatientType">
                                <option value="-1">请选择</option>
                            </select>
                            <label class="control-label" for="addpatientType" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="addSurveys" class="col-sm-3 control-label">问卷（可多选）</label>
                        <div class="col-sm-7">
                            <select multiple="" class="form-control" name="addSurveys" id="addSurveys">
                            </select>
                            <label class="control-label" for="addSurveys" style="display: none;"></label>
                        </div>
                    </div>

                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="addPlan">
                        添加
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!--------------------------------------添加的模糊框------------------------>


<!-- 修改模态框（Modal） -->
<form class="form-horizontal" id="updateForm">   <!--保证样式水平不混乱-->
    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="updateModalLabel">
                        修改随访计划
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->

                    <div class="form-group">
                        <label for="updateBeginAge" class="col-sm-3 control-label">年龄范围最小值（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateBeginAge" placeholder="请输入年龄范围最小值">
                            <label class="control-label" for="updateBeginAge" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updateEndAge" class="col-sm-3 control-label">年龄范围最大值（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateEndAge" placeholder="请输入年龄范围最大值">
                            <label class="control-label" for="updateEndAge" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="updateOldPatient" class="col-sm-3 control-label">病例类型</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateOldPatient">
                                <option value="-1">请选择</option>
                                <option value="1">新病例</option>
                                <option value="2">既往病例</option>
                                <option value="3">原哮喘无忧用户</option>
                                <option value="3">不限</option>

                            </select>
                            <label class="control-label" for="updateOldPatient" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="updateSex" class="col-sm-3 control-label">性别</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateSex">
                                <option value="-1">请选择</option>
                                <option value="1">男</option>
                                <option value="2">女</option>
                                <option value="3">不限</option>
                            </select>
                            <label class="control-label" for="updateSex" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updatePatientType" class="col-sm-3 control-label">病人类型</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updatePatientType">
                                <option value="-1">请选择</option>
                            </select>
                            <label class="control-label" for="updatePatientType" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updateSurveys" class="col-sm-3 control-label">问卷（可多选）</label>
                        <div class="col-sm-7">
                            <select multiple="" class="form-control" name="updateSurveys" id="updateSurveys">
                            </select>
                            <label class="control-label" for="updateSurveys" style="display: none;"></label>
                        </div>
                    </div>

                    <!---------------------表单-------------------->

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="update_Plan">
                        修改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!-------------------------------------------------------------->


<!------------------------------修改密码模糊框-------------------------------->

<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="updatepwd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        修改密码
                    </h4>
                </div>

                <div class="modal-body">

                    <!--正文-->
                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">原密码</label>
                        <div class="col-sm-7">
                            <input type="password" class="form-control" id="oldPwd" placeholder="请输入原密码">
                            <label class="control-label" for="oldPwd" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">新密码</label>
                        <div class="col-sm-7">
                            <input type="password" class="form-control" id="newPwd" placeholder="请输入新密码">
                            <label class="control-label" for="newPwd" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">确认密码</label>
                        <div class="col-sm-7">
                            <input type="password" class="form-control" id="confirmPwd" placeholder="请输入确认密码">
                            <label class="control-label" for="confirmPwd" style="display: none;"></label>
                        </div>
                    </div>
                    <!--正文-->


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="update_doctorPwd">
                        修改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!-------------------------------------------------------------->


<!-------------------------个人资料模糊框------------------------------------->

<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="updateinfo" tabindex="-1" role="dialog" aria-labelledby="ModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="ModalLabel">
                        个人资料
                    </h4>
                </div>

                <div class="modal-body">

                    <!--正文-->
                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">用户名</label>
                        <div class="col-sm-7">

                            <input type="text" class="form-control" id="username"
                                   value='<s:property value="#session.doctor.username"/>'>
                            <label class="control-label" for="username" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">真实姓名</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="name" placeholder="请输入您的真实姓名"
                                   value='<s:property value="#session.doctor.name"/>'>
                            <label class="control-label" for="name" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">联系号码</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="phone" placeholder="请输入您的联系号码"
                                   value='<s:property value="#session.doctor.phone"/>'>
                            <label class="control-label" for="phone" style="display: none;"></label>
                        </div>
                    </div>

                    <!--正文-->


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="doctor_updateInfo">
                        修改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!-------------------------------------------------------------->


<div class="modal fade" id="modal_info" tabindex="-1" role="dialog" aria-labelledby="addModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="infoModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-lg-12" id="div_info"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="btn_info_close" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<div id="loading" style="display: none;">
    <img src="${pageContext.request.contextPath}/img/loading.gif">
</div>

</body>
</html>