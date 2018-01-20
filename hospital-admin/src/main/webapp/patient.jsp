<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-CN" class="ax-vertical-centered">
<head>
    <meta charset="UTF-8">
    <title>呼吸天使问卷管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-doctor-theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-doctor-theme.css">
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/jQuery/jquery-3.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-dropdown.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/patient.js"></script>

    <script src="${pageContext.request.contextPath}/ajax-lib/ajaxutils.js"></script>
    <script src="${pageContext.request.contextPath}/js/patientUpdateInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/patientUpdatePwd.js"></script>

</head>


<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>


<body class="bootstrap-doctor-with-small-navbar">
<nav class="navbar navbar-inverse navbar-fixed-top bootstrap-doctor-navbar bootstrap-doctor-navbar-under-small"
     role="navigation">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="collapse navbar-collapse main-navbar-collapse">
                    <a class="navbar-brand"
                       href="${pageContext.request.contextPath}/patient.jsp"><strong>欢迎使用呼吸天使问卷管理系统</strong></a>
                    <ul class="nav navbar-nav navbar-right">
                        <s:if test="#session.patient!=null"><!-- 判断是否已经登录 -->
                        <li class="dropdown">
                            <a href="#" role="button" class="dropdown-toggle" data-hover="dropdown"> <i
                                    class="glyphicon glyphicon-user"></i> 欢迎您，<s:property
                                    value="#session.patient.name"/><i class="caret"></i></a>
                            <ul class="dropdown-menu">
                                <li><a href="#updateinfo" data-toggle="modal">个人资料</a></li>
                                <li role="presentation" class="divider"></li>
                                <li><a href="#updatepwd" data-toggle="modal">修改密码</a></li>
                                <li role="presentation" class="divider"></li>
                                <li><a href="${pageContext.request.contextPath}/patientLoginAction_logout.action">退出</a>
                                </li>
                            </ul>
                        </li>
                        </s:if>
                        <s:else><!-- 如果未登录，出现登录按钮 -->
                        <button type="button" class="btn btn-default btn-sm " id="btn_login" style="margin: 10"
                                data-dismiss="modal">登录
                        </button>
                        </s:else>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</nav>

<div class="container">
    <!-- left, vertical navbar & content -->
    <div class="row">
        <!-- left, vertical navbar -->
        <div class="col-md-2 bootstrap-doctor-col-left">
            <ul class="nav navbar-collapse collapse bootstrap-doctor-navbar-side">
                <li>
                    <a href="${pageContext.request.contextPath}/surveyAction_findsurveyByPage.action"><i
                            class="glyphicon glyphicon-chevron-right"></i> 问卷查询</a>
                </li>
                <s:if test="#session.patient!=null"><!-- 判断是否登录 -->
                <li>
                    <a href="${pageContext.request.contextPath}/patient/deliveryAction_findMyDeliveryInfoByPage.action"><i
                            class="glyphicon glyphicon-chevron-right"></i> 分发信息</a>
                </li>
                </s:if>
                <s:if test="#session.patient!=null"><!-- 判断是否登录 -->
                <li>
                    <a href="${pageContext.request.contextPath}/patient/forfeitAction_findMyForfeitInfoByPage.action"><i
                            class="glyphicon glyphicon-chevron-right"></i> 逾期信息</a>
                </li>
                </s:if>


            </ul>
        </div>

        <!-- content -->
        <div class="col-md-10">
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="text-muted bootstrap-doctor-box-title">问卷查询</div>
                        </div>
                        <div class="bootstrap-doctor-panel-content">
                            <ul>
                                <li>根据问卷编号、问卷名称查询问卷信息</li>
                                <li>可查询问卷的编号、名称、分类、作者、价格、总回收数等</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <s:if test="#session.patient!=null"><!-- 判断用户是否登录 -->
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="text-muted bootstrap-doctor-box-title">分发信息</div>
                        </div>
                        <div class="bootstrap-doctor-panel-content">
                            <ul>
                                <li>根据分发编号、问卷名称查询自己分发的问卷信息</li>
                                <li>可查询除问卷的基本信息、分发日期、截止答卷日期、超期天数等</li>
                            </ul>
                        </div>
                    </div>
                </div>


            </div>
            </s:if>
            <s:if test="#session.patient!=null"><!-- 判断用户是否登录 -->
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="text-muted bootstrap-doctor-box-title">逾期信息</div>
                        </div>
                        <div class="bootstrap-doctor-panel-content">
                            <ul>
                                <li>根据分发编号、问卷名称查询自己分发的问卷信息</li>
                                <li>可查询除问卷的基本信息、分发日期、答卷时期、超期天数、需设置的提醒等</li>
                            </ul>
                        </div>
                    </div>
                </div>


            </div>
            </s:if>
        </div>
    </div>
</div>


<!-------------------------------------------------------------->

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
                            <label class="control-label" for="oldPwd" style="display: none"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">新密码</label>
                        <div class="col-sm-7">
                            <input type="password" class="form-control" id="newPwd" placeholder="请输入新密码">
                            <label class="control-label" for="newPwd" style="display: none"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">确认密码</label>
                        <div class="col-sm-7">
                            <input type="password" class="form-control" id="confirmPwd" placeholder="请输入确认密码">
                            <label class="control-label" for="confirmPwd" style="display: none"></label>
                        </div>
                    </div>
                    <!--正文-->


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="update_patientPwd">
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
                        <label for="firstname" class="col-sm-3 control-label">真实姓名</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="name" placeholder="请输入您的真实姓名"
                                   value='<s:property value="#session.patient.name"/>'>
                            <label class="control-label" for="name" style="display: none"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">联系号码</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="phone" placeholder="请输入您的联系号码"
                                   value='<s:property value="#session.patient.phone"/>'>
                            <label class="control-label" for="phone" style="display: none"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">邮箱</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="email" placeholder="请输入您的邮箱"
                                   value='<s:property value="#session.patient.email"/>'>
                            <label class="control-label" for="email" style="display: none"></label>
                        </div>
                    </div>

                    <!--正文-->


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="patient_updateInfo">
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


</body>
</html>
