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
    <script src="${pageContext.request.contextPath}/js/common.js"></script>

    <script src="${pageContext.request.contextPath}/ajax-lib/ajaxutils.js"></script>
    <script src="${pageContext.request.contextPath}/js/doctorUpdateInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/doctorUpdatePwd.js"></script>

    <!-- add specific js in here -->
    <script src="${pageContext.request.contextPath}/js/getAllSurveyTypes.js"></script>
    <script src="${pageContext.request.contextPath}/js/addSurvey.js"></script>
    <script src="${pageContext.request.contextPath}/js/addQuestion.js"></script>

    <script src="${pageContext.request.contextPath}/js/updateSurvey.js"></script>
    <script src="${pageContext.request.contextPath}/js/deleteSurvey.js"></script>
    <script src="${pageContext.request.contextPath}/js/getSurveyInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/addSurveyNum.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jQuery/ajaxfileupload.js"></script>
    <script src="${pageContext.request.contextPath}/js/ajax_upload.js"></script>
    <script src="${pageContext.request.contextPath}/js/batchAddSurvey.js"></script>
    <script src="${pageContext.request.contextPath}/js/exportSurvey.js"></script>
    <!-- add specific js in here -->

    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>

<body class="skin-blue sidebar-mini">
<!-- Site wrapper -->
<div id="rrapp" class="wrapper">
    <%@include file="sidebar.jsp"%>
<%--
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

            <div class="panel panel-info">
                <div class="panel-heading">查询</div>
                <form class="form-horizontal"
                      action="${pageContext.request.contextPath}/doctor/surveyManageAction_querySurvey.action"
                      method="post">
                    <div class="form-group">
                        <div class="col-sm-2 control-label">问卷分类</div>
                        <div class="col-sm-10">
                            <select class="form-control" id="surveyTypeId" name="surveyTypeId">
                                <option value="-1">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-2 control-label">问卷名称</div>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="surveyName" name="surveyName"
                                   placeholder="问卷名称"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-2 control-label">作者名称</div>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="author" name="author" placeholder="作者名称"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-2 control-label">科室名称</div>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="department" name="department"
                                   placeholder="科室名称"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-2 control-label"></div>
                        <button type="submit" class="btn btn-primary" id="btn_query" onclick="query()">查询</button>
                    </div>
                </form>
            </div>
            <div name="showList">
                <div class="grid-btn">
                    <a class="btn btn-primary" id="btn_add" data-toggle="modal" data-target="#addModal"><i
                            class="fa fa-plus"></i> 添加问卷</a>
                    <!--<a class="btn btn-primary" data-toggle="modal" data-target="#batchAddModal"><i
                            class="fa fa-times"></i> 批量添加</a>-->
                    <a class="btn btn-primary" onclick="exportSurvey()"><i class="fa fa-share"></i> 导出</a>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <table id="data_list" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>问卷类型</th>
                            <th>问卷名称</th>
                            <th>作者名称</th>
                            <th>科室</th>
                            <th>生成日期</th>
                            <th>总分发数</th>
                            <th>总回收数</th>
                            <th>操作</th>

                        </tr>
                        </thead>


                        <!---在此插入信息-->
                        <s:if test="#request.pb.beanList!=null">
                            <s:iterator value="#request.pb.beanList" var="survey">
                                <tbody>
                                <td><s:property value="#survey.surveyType.typeName"/></td>
                                <td><s:property value="#survey.surveyName"/></td>
                                <td><s:property value="#survey.author"/></td>
                                <td><s:property value="#survey.department"/></td>
                                <td><s:date name="#survey.putdate" format="yyyy-MM-dd"/></td>
                                <td><s:property value="#survey.num"/></td>
                                <td><s:property value="#survey.currentNum"/></td>
                                <td>
                                    <button type="button" class="btn btn-info btn-xs" data-toggle="modal"
                                            data-target="#findModal"
                                            onclick="getSurveyInfo(<s:property value="#survey.surveyId"/>)">查看
                                    </button>
                                    <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                            data-target="#updateModal" id="btn_update"
                                            onclick="updateSurvey(<s:property value="#survey.surveyId"/>)">修改
                                    </button>
                                    <button type="button" class="btn btn-danger btn-xs"
                                            onclick="deleteSurvey(<s:property value="#survey.surveyId"/>)">删除
                                    </button>
                                    <input type="hidden" id="question_action"
                                           value="${pageContext.request.contextPath}/doctor/surveyManageAction_questionManage.action">
                                    <button type="button" class="btn btn-success btn-xs"
                                            onclick="question(<s:property value="#survey.surveyId"/>)">试题
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
                            </tbody>
                        </s:else>

                    </table>


                    <s:if test="#request.pb!=null">

                        <%-- 定义页码列表的长度，5个长 --%>
                        <c:choose>
                            <%-- 第一条：如果总页数<=5，那么页码列表为1 ~ totalPage 从第一页到总页数--%>
                            <%--如果总页数<=5的情况 --%>
                            <c:when test="${pb.totalPage <= 5 }">
                                <c:set var="begin" value="1"/>
                                <c:set var="end" value="${pb.totalPage }"/>
                            </c:when>
                            <%--总页数>5的情况 --%>
                            <c:otherwise>
                                <%-- 第二条：按公式计算，让列表的头为当前页-2；列表的尾为当前页+2 --%>
                                <c:set var="begin" value="${pb.pageCode-2 }"/>
                                <c:set var="end" value="${pb.pageCode+2 }"/>

                                <%-- 第三条：第二条只适合在中间，而两端会出问题。这里处理begin出界！ --%>
                                <%-- 如果begin<1，那么让begin=1，相应end=5 --%>
                                <c:if test="${begin<1 }">
                                    <c:set var="begin" value="1"/>
                                    <c:set var="end" value="5"/>
                                </c:if>
                                <%-- 第四条：处理end出界。如果end>tp，那么让end=tp，相应begin=tp-4 --%>
                                <c:if test="${end>pb.totalPage }">
                                    <c:set var="begin" value="${pb.totalPage-4 }"/>
                                    <c:set var="end" value="${pb.totalPage }"/>
                                </c:if>
                            </c:otherwise>
                        </c:choose>


                        <div class="pull-right"><!--右对齐--->
                            <ul class="pagination">
                                <li class="disabled"><a href="#">第<s:property
                                        value="#request.pb.pageCode"/>页/共<s:property
                                        value="#request.pb.totalPage"/>页</a></li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=1">首页</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${pb.pageCode-1 }">&laquo;</a>
                                </li><!-- 上一页 -->
                                    <%-- 循环显示页码列表 --%>
                                <c:forEach begin="${begin }" end="${end }" var="i">
                                    <c:choose>
                                        <%--如果是当前页则设置无法点击超链接 --%>
                                        <c:when test="${i eq pb.pageCode }">
                                            <li class="active"><a>${i }</a>
                                            <li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>
                                                <a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${i}">${i}</a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                    <%--如果当前页数没到总页数，即没到最后一页,则需要显示下一页 --%>
                                <c:if test="${pb.pageCode < pb.totalPage }">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${pb.pageCode+1}">&raquo;</a>
                                    </li>
                                </c:if>
                                    <%--否则显示尾页 --%>
                                <li>
                                    <a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${pb.totalPage}">尾页</a>
                                </li>
                            </ul>
                        </div>
                    </s:if>
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


<!-------------------------------------批量添加的模糊框------------------------>
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="batchAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        批量添加新问卷
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->
                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">下载模板</label>
                        <div class="col-sm-7" style="padding-top: 7px">
                            <a href="${pageContext.request.contextPath}/doctor/FileDownloadAction.action?fileName=survey.xls">点击下载</a><br/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">上传文件</label>
                        <div class="col-sm-7">
                            <label for="inputfile"></label>
                            <!--为了jquery获得basePath的值，必须写（如果没有更好的办法） -->
                            <input type="hidden" value="<%=basePath%>" id="basePath"/>
                            <input type="hidden" id="excel"/>
                            <!--id是给jquery使用的，name是给后台action使用的，必须和后台的名字相同！！ -->
                            <input type="file" id="upload" name="upload"/><br/>
                            <label class="control-label" for="upload" style="display: none;"></label>

                        </div>
                    </div>


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="batchAdd">
                        添加
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!--------------------------------------添加的模糊框------------------------>


<!--------------------------------------查看的模糊框------------------------>
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="findModal" tabindex="-1" role="dialog" aria-labelledby="findModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="findModalLabel">
                        查看问卷信息
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->
                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问卷名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findSurveyName" readonly="readonly">

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问卷类型</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findSurveyType" readonly="readonly">

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">作者名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findAuthor" readonly="readonly">

                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">科室</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findDepartment" readonly="readonly">

                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">随访周期</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findFrequency" readonly="readonly">

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">随访次数</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findTimes" readonly="readonly">

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">用户注册时发送</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findSendOnRegister" readonly="readonly">

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">总分发数</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findNum" readonly="readonly">

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">当前数量</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findCurrentNum" readonly="readonly">

                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">允许答卷最大天数</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findBday" readonly="readonly">

                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">操作医生</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findDoctor" readonly="readonly">

                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">简介</label>
                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" id="findDescription" readonly="readonly"></textarea>
                        </div>
                    </div>

                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!--------------------------------------查看的模糊框------------------------>


<!--------------------------------------浏览问卷的模态框------------------------>
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="checkSurvey" tabindex="-1" role="dialog" aria-labelledby="addNumModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="addNumModalLabel">
                        浏览问卷
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->

                    <% int i = 1; %>
                    <s:iterator value="#request.myAnswers" var="answer">
                        <div class="form-group">
                            <label for="firstname" class="col-lg-3 control-label"></label>
                            <div class="col-lg-7">
                                <p class="text-left"><b><%=i%>. ${answer.question.questionContent}.</b></p>
                            </div>
                        </div>
                        <s:iterator value="#answer.question.choices" var="choice">
                            <div class="form-group">
                                <label for="firstname" class="col-lg-3 control-label"></label>
                                <div class="form-group" class="col-lg-7  col-lg-offset-3">
                                    <label>
                                        <input type="radio" name="optionsRadios" id="optionsRadios1"
                                               value="option1">${choice.choiceContent}.
                                    </label>
                                </div>
                            </div>
                        </s:iterator>
                        <% i++; %>
                    </s:iterator>

                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="check_Survey">
                        新增
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!--------------------------------------浏览问卷的模态框------------------------>


<!--------------------------------------添加的模糊框------------------------>
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        添加新问卷
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问卷名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addSurveyName" placeholder="请输入问卷名称">
                            <label class="control-label" for="addSurveyName" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问卷类型</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addSurveyType">
                                <option value="-1">请选择</option>
                            </select>
                            <label class="control-label" for="addSurveyType" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">作者名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addAuthor" placeholder="请输入作者名称">
                            <label class="control-label" for="addAuthor" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">科室</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addDepartment" placeholder="请输入科室">
                            <label class="control-label" for="addDepartment" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">用户注册时发送</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addSendOnRegister">
                                <option value="-1">请选择</option>
                                <option value=true>是</option>
                                <option value=false>否</option>
                            </select>
                            <label class="control-label" for="addSendOnRegister" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">随访周期</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addFrequency">
                                <option value="-1">请选择</option>
                                <option value="1">1个月</option>
                                <option value="2">2个月</option>
                                <option value="3">3个月</option>
                                <option value="6">6个月</option>
                                <option value="12">12个月</option>
                            </select>
                            <label class="control-label" for="addFrequency" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">允许答卷最大天数</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addBday" placeholder="请输入允许答卷最大天数">
                            <label class="control-label" for="addBday" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">随访次数</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addTimes" placeholder="请输入问卷随访次数">
                            <label class="control-label" for="addTimes" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">简介</label>
                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" id="addDescription"
                                      placeholder="请输入问卷简介"></textarea>
                            <label class="control-label" for="addDescription" style="display: none;"></label>
                        </div>
                    </div>

                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="addSurvey">
                        添加
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!--------------------------------------添加的模糊框------------------------>


<!-- 修改模态框（Modal） -->
<!-------------------------------------------------------------->

<!-- 修改模态框（Modal） -->
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="updateModalLabel">
                        修改问卷信息
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问卷名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateSurveyName" placeholder="请输入问卷名称">
                            <label class="control-label" for="updateSurveyName" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问卷类型</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateSurveyType">
                                <option value="-1">请选择</option>
                            </select>
                            <label class="control-label" for="updateSurveyType" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">用户注册时发送</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateSendOnRegister">
                                <option value="-1">请选择</option>
                                <option value="true">是</option>
                                <option value="false">否</option>

                            </select>
                            <label class="control-label" for="updateSendOnRegister" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">随访周期</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateFrequency">
                                <option value="-1">请选择</option>
                                <option value="1">1个月</option>
                                <option value="2">2个月</option>
                                <option value="3">3个月</option>
                                <option value="6">6个月</option>
                                <option value="12">12个月</option>
                            </select>
                            <label class="control-label" for="updateFrequency" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">允许答卷最大天数</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateBday" placeholder="请输入允许答卷最大天数">
                            <label class="control-label" for="updateBday" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">随访次数</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateTimes" placeholder="请输入问卷随访次数">
                            <label class="control-label" for="updateTimes" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">作者名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateAuthor" placeholder="请输入作者名称">
                            <label class="control-label" for="updateAuthor" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">科室</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateDepartment" placeholder="请输入科室">
                            <label class="control-label" for="updateDepartment" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">简介</label>
                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" id="updateDescription"
                                      placeholder="请输入问卷简介"></textarea>
                            <label class="control-label" for="updateDescription" style="display: none;"></label>
                        </div>
                    </div>


                    <!---------------------表单-------------------->

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="updateSurvey">
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