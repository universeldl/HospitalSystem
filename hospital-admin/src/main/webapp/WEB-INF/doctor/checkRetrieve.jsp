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
    <script src="${pageContext.request.contextPath}/js/updateAnswer.js"></script>
    <script src="${pageContext.request.contextPath}/js/getRetrieveInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/getAnswerSheetInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/retrieveSurvey.js"></script>
    <!-- add specific js in here -->

    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>

<body class="skin-blue sidebar-mini">
<!-- Site wrapper -->
<div id="rrapp" class="wrapper">
    <%@include file="sidebar.jsp"%>

<%--  <header class="main-header"><a href="javascript:void(0);" class="logo"><span class="logo-mini"><b>后台</b></span>
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

            <!-- content -->
            <div class="row">
                <div class="col-md-10">

                    <s:if test="#request.myAnswers!=null">
                        <s:iterator value="#request.myAnswers" var="answer" status="answerIdx">

                            <!--多选题-->
                            <s:if test="#answer.question.questionType==1">
                                <div class="col-md-12">
                                    <div class="panel panel-info">
                                        <div class="panel-heading">
                                            <div class="text-muted bootstrap-admin-box-title"
                                                 <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                                <s:property value="#answerIdx.index+1"/>. ${answer.question.questionContent}.(多选题)
                                                <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">
                                                    (最后修改人： <s:property value="#answer.lastModified"/>医生修改于 <s:property value="#answer.modifiedDate"/>)
                                                </s:if>
                                                <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                                        data-target="#updateAnswerModal"
                                                        style="float:right;color:white;"
                                                        onclick="updateAnswerById(<s:property value="#answer.answerId"/>)">修改
                                                </button>
                                            </div>
                                        </div>
                                        <div class="bootstrap-admin-panel-content"
                                             <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                            <s:iterator value="#answer.question.choices" var="choice"
                                                        status="choiceIdx">
                                                <ul>
                                                    <s:set var="flag" value="true"></s:set>
                                                    <s:iterator value="#answer.choices" var="cho">
                                                        <s:if test="#cho.choiceId == #choice.choiceId">
                                                            <s:set var="flag" value="false"></s:set>
                                                        </s:if>
                                                    </s:iterator>
                                                    <s:if test="#flag == false">
                                                        <div>
                                                            <input type="checkbox"
                                                                   name='question<s:property value="#answer.question.questionId"/>'
                                                                   id='question<s:property value="#answerIdx.index"/>choice<s:property value="#choiceIdx.index" />'
                                                                   value='<s:property value="#choice.choiceId"/>'
                                                                   checked="checked" disabled="disabled">
                                                            <s:property
                                                                    value="#choiceIdx.index+1"/>. ${choice.choiceContent}.
                                                        </div>
                                                    </s:if>
                                                    <s:else>
                                                        <div>
                                                            <input type="checkbox"
                                                                   name='question<s:property value="#answer.question.questionId"/>'
                                                                   id='question<s:property value="#answerIdx.index"/>choice<s:property value="#choiceIdx.index" />'
                                                                   value='<s:property value="#choice.choiceId"/>'
                                                                   disabled="disabled">
                                                            <s:property value="#choiceIdx.index+1"/>. ${choice.choiceContent}.
                                                        </div>
                                                    </s:else>
                                                </ul>
                                            </s:iterator>
                                            <s:if test="#answer.question.textChoice==1">
                                                <ul>
                                                    <div>
                                                        <input type="checkbox"
                                                               name='question<s:property value="#answer.question.questionId"/>'
                                                               id='textChoice<s:property value="#answer.question.choices.size+1" />'
                                                               disabled="disabled"
                                                        <s:if test="#answer.textChoice==1"> checked="checked" </s:if> >
                                                        <s:property value="#answer.question.choices.size+1"/>.
                                                        其它：
                                                    </div>
                                                    <div>
                                                    <textarea class="form-control" rows="3" id="updateQuestionContent5" disabled="disabled"><s:property value="#answer.textChoiceContent"/></textarea>

                                                    </div>
                                                </ul>
                                            </s:if>
                                        </div>
                                    </div>
                                </div>
                            </s:if>
                            <!--单选题-->
                            <s:elseif test="#answer.question.questionType== 2">
                                <div class="col-md-12">
                                    <div class="panel panel-info">
                                        <div class="panel-heading">
                                            <div class="text-muted bootstrap-admin-box-title"
                                                 <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                                <s:property value="#answerIdx.index+1"/>. ${answer.question.questionContent}.(单选题)
                                                <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">
                                                    (最后修改人： <s:property value="#answer.lastModified"/>医生修改于 <s:property value="#answer.modifiedDate"/>)
                                                </s:if>
                                                <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                                        data-target="#updateAnswerModal"
                                                        style="float:right;color:white;"
                                                        onclick="updateAnswerById(<s:property value="#answer.answerId"/>)">修改
                                                </button>
                                            </div>
                                        </div>
                                        <div class="bootstrap-admin-panel-content"
                                             <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                            <s:iterator value="#answer.question.choices" var="choice"
                                                        status="choiceIdx">
                                                <ul>
                                                    <s:set var="flag" value="true"></s:set>
                                                    <s:iterator value="#answer.choices" var="cho">
                                                        <s:if test="#cho.choiceId == #choice.choiceId">
                                                            <s:set var="flag" value="false"></s:set>
                                                        </s:if>
                                                    </s:iterator>
                                                    <s:if test="#flag == false">
                                                        <div>
                                                            <input type="radio"
                                                                name='question<s:property value="#answer.question.questionId"/>'
                                                                id='question<s:property value="#answerIdx.index"/>choice<s:property value="#choiceIdx.index" />'
                                                                value='<s:property value="#choice.choiceId"/>'
                                                                checked="checked" disabled="disabled">
                                                            <s:property value="#choiceIdx.index+1"/>. ${choice.choiceContent}.
                                                        </div>
                                                    </s:if>
                                                    <s:else>
                                                        <div>
                                                            <input type="radio"
                                                                name='question<s:property value="#answer.question.questionId"/>'
                                                                id='question<s:property value="#answerIdx.index"/>choice<s:property value="#choiceIdx.index" />'
                                                                value='<s:property value="#choice.choiceId"/>'
                                                                disabled="disabled">
                                                            <s:property value="#choiceIdx.index+1"/>. ${choice.choiceContent}.
                                                        </div>
                                                    </s:else>
                                                </ul>
                                            </s:iterator>
                                            <s:if test="#answer.question.textChoice==1">
                                                <ul>
                                                    <div>
                                                        <input type="radio"
                                                               name='question<s:property value="#answer.question.questionId"/>'
                                                               id='textChoice<s:property value="#answer.question.choices.size+1" />'
                                                               disabled="disabled"
                                                        <s:if test="#answer.textChoice==1"> checked="checked" </s:if> >
                                                        <s:property value="#answer.question.choices.size+1"/>.
                                                        其它：
                                                    </div>
                                                    <div>
                                                    <textarea class="form-control" rows="3" id="updateQuestionContent4" disabled="disabled"><s:property value="#answer.textChoiceContent"/></textarea>
                                                    </div>
                                                </ul>
                                            </s:if>
                                        </div>
                                    </div>
                                </div>
                            </s:elseif>
                            <!--问答题-->
                            <s:elseif test="#answer.question.questionType== 3">
                                <div class="col-md-12">
                                    <div class="panel panel-info">
                                        <div class="panel-heading">
                                            <div class="text-muted bootstrap-admin-box-title"
                                                 <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                                <s:property value="#answerIdx.index+1"/>. ${answer.question.questionContent}.(问答题)
                                                <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">
                                                    (最后修改人： <s:property value="#answer.lastModified"/>医生修改于 <s:property value="#answer.modifiedDate"/>)
                                                </s:if>
                                                <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                                        data-target="#updateAnswerModal"
                                                        style="float:right;color:white;"
                                                        onclick="updateAnswerById(<s:property
                                                                value="#answer.answerId"/>)">修改
                                                </button>
                                            </div>
                                        </div>
                                        <div class="bootstrap-admin-panel-content"
                                             <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                            <div>
                                                <textarea class="form-control" rows="3" id="updateQuestionContent3" disabled="disabled"><s:property value="#answer.textChoiceContent"/></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </s:elseif>
                            <!--问答题（日期）-->
                            <s:elseif test="#answer.question.questionType== 4">
                                <div class="col-md-12">
                                    <div class="panel panel-info">
                                        <div class="panel-heading">
                                            <div class="text-muted bootstrap-admin-box-title"
                                                 <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                                <s:property value="#answerIdx.index+1"/>. ${answer.question.questionContent}.(问答题-日期)
                                                <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">
                                                    (最后修改人： <s:property value="#answer.lastModified"/>医生修改于 <s:property value="#answer.modifiedDate"/>)
                                                </s:if>
                                                <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                                        data-target="#updateAnswerModal"
                                                        style="float:right;color:white;"
                                                        onclick="updateAnswerById(<s:property
                                                                value="#answer.answerId"/>)">修改
                                                </button>
                                            </div>
                                        </div>
                                        <div class="bootstrap-admin-panel-content"
                                             <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                            <div>
                                                <textarea class="form-control" rows="3" id="updateQuestionContent2" disabled="disabled"><s:property value="#answer.textChoiceContent"/></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </s:elseif>
                            <!--图片题-->
                            <s:elseif test="#answer.question.questionType== 5">
                                <div class="col-md-12">
                                    <div class="panel panel-info">
                                        <div class="panel-heading">
                                            <div class="text-muted bootstrap-admin-box-title"
                                                 <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">style="color:red;"</s:if>>
                                                <s:property value="#answerIdx.index+1"/>. ${answer.question.questionContent}.(图片题)
                                                <s:if test="#answer.lastModified!=null && #answer.lastModified!=''">
                                                    (最后修改人： <s:property value="#answer.lastModified"/>医生修改于 <s:property value="#answer.modifiedDate"/>)
                                                </s:if>
                                                <!--<button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                                        data-target="#updateAnswerModal"
                                                        style="float:right;color:white;"
                                                        onclick="updateAnswerById(<s:property
                                                                value="#answer.answerId"/>)">修改
                                                </button> -->
                                            </div>
                                        </div>
                                        <div class="bootstrap-admin-panel-content"
                                            <s:generator val="#answer.textChoiceContent"  separator=";" id="tc" >
                                                <s:iterator status="st" value="#tc" id="pic" >
                                                    <br/>
                                                        <img id="mutationImage" style="width:750px;" src="${pageContext.request.contextPath}/doctor/retrieveManageAction_IoReadImage.action?imgName=<s:property value="pic"/>"/>
                                                        <input type="hidden" id="photo" name="photo"/>
                                                    <br/>
                                                </s:iterator>
                                            </s:generator>
                                        </div>
                                    </div>
                                </div>
                            </s:elseif>
                        </s:iterator>
                    </s:if>
                    <!--
                    <div class="col-md-12" align="center">
                        <button type="button" class="btn btn-primary" onclick="assignment()">提交答卷</button>
                    </div>
                    -->

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


<!--------------------------------------修改答案的模糊框------------------------>
<form id="updateForm" class="form-horizontal">   <!--保证样式水平不混乱-->
    <div class="modal fade" id="updateAnswerModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="updateModalLabel">
                        修改答案信息
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <p id="updateAnsChos">请选择：</p>
                        <ul>
                            <div id="updateChoicesDiv"></div>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="update_answer">
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