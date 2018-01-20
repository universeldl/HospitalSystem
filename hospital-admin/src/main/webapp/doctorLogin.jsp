<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-CN" class="bootstrap-doctor-vertical-centered">
<head>
    <meta charset="UTF-8">
    <title>呼吸天使问卷管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-doctor-theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-doctor-theme.css">
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" media="all">
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/jQuery/jquery-3.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/ajax-lib/ajaxutils.js"></script>
    <script src="${pageContext.request.contextPath}/js/doctorLogin.js"></script>
</head>


<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

<body>

<div class="login">
    <div class="message">欢迎登录呼吸天使问卷管理系统</div>
    <div id="darkbannerwrap"></div>

    <form id="post">
        <input id="username" placeholder="医生用户名" required="" type="text">
        <hr class="hr15">
        <input id="password" placeholder="密码" required="" type="password">
        <hr class="hr15">
        <input type="button" id="login_submit" value="登  录" style="width:100%;">
        <hr class="hr20">
        <a onClick="alert('请联系管理员')">忘记密码</a>
    </form>

    <div class="modal fade" id="modal_info" tabindex="-1" role="dialog" aria-labelledby="addModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
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
</div>
</body>

</html>