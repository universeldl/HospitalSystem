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
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
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
    <script src="${pageContext.request.contextPath}/js/addProvince.js"></script>
    <script src="${pageContext.request.contextPath}/js/updateProvince.js"></script>
    <script src="${pageContext.request.contextPath}/js/deleteProvince.js"></script>
    <script src="${pageContext.request.contextPath}/js/addCity.js"></script>
    <script src="${pageContext.request.contextPath}/js/deleteCity.js"></script>
    <script src="${pageContext.request.contextPath}/js/updateCity.js"></script>
    <script src="${pageContext.request.contextPath}/js/addHospital.js"></script>
    <script src="${pageContext.request.contextPath}/js/updateHospital.js"></script>
    <script src="${pageContext.request.contextPath}/js/deleteHospital.js"></script>

    <!-- add specific js in here -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/jQuery/ajaxfileupload.js"></script>
    <script src="${pageContext.request.contextPath}/js/ajax_upload.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>

    <script src="${pageContext.request.contextPath}/js/batchAddPatient.js"></script>
    <script src="${pageContext.request.contextPath}/js/echarts.common.min.js"></script>
    <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <!-- add specific js in here -->

    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            $("#datepicker").datepicker({
                changeMonth: true,
                changeYear: true
            });
        });
    </script>
</head>

<body class="skin-blue sidebar-mini">
<!-- Site wrapper -->
<div id="rrapp" class="wrapper">
    <%@include file="sidebar.jsp"%>
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



                <div class="row">
                    <div class="col-lg-12">

                        <h4> 省份管理 ：</h4>

<%--                        <div name="showList">
                            <div class="grid-btn">
                                <a class="btn btn-primary" data-toggle="modal" onclick="addProvince()">添加</a>
                            </div>
                        </div>--%>

                        <div name="showList">
                            <div class="grid-btn">
                                <a class="btn btn-primary" id="btn_add_province" data-toggle="modal" data-target="#addProvinceModal"><i
                                        class="fa fa-plus"></i> 添加省份</a>
                            </div>
                        </div>


                        <table id="province_list" class="table table-hover table-bordered" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>省（直辖市）</th>
                                <th>操作</th>
                            </tr>
                            </thead>

                            <s:if test="#request.provinces!=null">
                                <s:iterator value="#request.provinces" var="province">
                                    <tbody>
                                    <td><s:property value="#province.name"/></td>
                                    <td>
                                        <button type="button" class="btn btn-info btn-xs" data-toggle="modal"
                                                data-target="#updateProvinceModal"
                                                onclick="updateProvince(<s:property
                                                        value="#province.provinceId"/>)">修改
                                        </button>
                                        <button type="button" class="btn btn-danger btn-xs"
                                                onclick="deleteProvince(<s:property
                                                        value="#province.provinceId"/>)">删除
                                        </button>
                                    </td>
                                    </tbody>

                                </s:iterator>
                            </s:if>
                            <s:else>
                                <tbody>
                                <td>暂无数据</td>
                                <td>暂无数据</td>
                                </tbody>
                            </s:else>

                        </table>
                    </div>
                </div>


            <div class="row">
                <div class="col-lg-12">

                    <h4> 城市管理 ：</h4>

                    <div name="showList">
                        <div class="grid-btn">
                            <a class="btn btn-primary" id="btn_add_city"
                               data-toggle="modal" data-target="#addCityModal"><i
                                    class="fa fa-plus"></i> 添加城市</a>
                        </div>
                    </div>


                    <table id="city_list" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>市（区、县）</th>
                            <th>省（直辖市）</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <s:if test="#request.cities!=null">
                            <s:iterator value="#request.cities" var="city">
                                    <tbody>
                                    <td><s:property value="#city.name"/></td>
                                    <td><s:property value="#city.province.name"/></td>
                                    <td>
                                        <button type="button" class="btn btn-info btn-xs" data-toggle="modal"
                                                data-target="#updateCityModal"
                                                onclick="updateCity(<s:property
                                                        value="#city.cityId"/>)">修改
                                        </button>
                                        <button type="button" class="btn btn-danger btn-xs"
                                                onclick="deleteCity(<s:property
                                                        value="#city.cityId"/>)">删除
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
                            </tbody>
                        </s:else>
                    </table>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">

                    <h4> 医院管理 ：</h4>

                    <div name="showList">
                        <div class="grid-btn">
                            <a class="btn btn-primary" id="btn_add_hospital"
                               data-toggle="modal" data-target="#addHospitalModal"><i
                                    class="fa fa-plus"></i> 添加医院</a>
                        </div>
                    </div>


                    <table id="hospital_list" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>医院名称</th>
                            <th>市（区、县）</th>
                            <th>省（直辖市）</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <s:if test="#request.hospitals!=null">
                            <s:iterator value="#request.hospitals" var="hospital">
                                <tbody>
                                <td><s:property value="#hospital.name"/></td>
                                <td><s:property value="#hospital.city.name"/></td>
                                <td><s:property value="#hospital.city.province.name"/></td>
                                <td>
                                    <button type="button" class="btn btn-info btn-xs" data-toggle="modal"
                                            data-target="#updateHospitalModal"
                                            onclick="updateHospital(<s:property
                                                    value="#hospital.hospitalId"/>)">修改
                                    </button>
                                    <button type="button" class="btn btn-danger btn-xs"
                                            onclick="deleteHospital(<s:property
                                                    value="#hospital.hospitalId"/>)">删除
                                    </button>
                                </td>
                                </tbody>
                            </s:iterator>
                        </s:if>
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


<!-- 修改模态框（Modal） -->
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <div class="modal fade" id="updateProvinceModal" tabindex="-1" role="dialog" aria-labelledby="updateProvinceModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="updateProvinceModalLabel">
                        修改省份名称
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->


                    <div class="form-group">
                        <label for="addProvinceName" class="col-sm-3 control-label">省份名称</label>
                        <div class="col-sm-7">
                            <input type="hidden" id="updateProvinceId">
                            <input type="text" class="form-control" id="updateProvinceName" placeholder="请输入省份名称">
                            <label class="control-label" for="updateProvince" style="display: none;"></label>
                        </div>
                    </div>


                    <!---------------------表单-------------------->

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="updateProvince">
                        修改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>


<!--------------------------------------添加的模糊框------------------------>
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="addProvinceModal" tabindex="-1" role="dialog" aria-labelledby="ProvinceModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="ProvinceModalLabel">
                        添加新省份（直辖市）
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->
                    <div class="form-group">
                        <label for="addProvinceName" class="col-sm-3 control-label">省份（直辖市）名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addProvinceName" placeholder="请输入省份（直辖市）名称">
                            <label class="control-label" for="addProvince" style="display: none;"></label>
                        </div>
                    </div>


                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="addProvince">
                        添加
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>


<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <div class="modal fade" id="updateHospitalModal" tabindex="-1" role="dialog" aria-labelledby="updateHospitalModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="updateHospitalModalLabel">
                        修改医院
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->


                    <div class="form-group">
                        <label for="updateHospitalName" class="col-sm-3 control-label">医院名称</label>
                        <div class="col-sm-7">
                            <input type="hidden" id="updateHospitalId">
                            <input type="text" class="form-control" id="updateHospitalName">
                            <label class="control-label" for="updateHospitalName" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updateHospitalProvince" class="col-sm-3 control-label">所属省份（直辖市）</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateHospitalProvince" onchange="loadCity2()">
                                <option value="-1" selected disabled>请选择</option>
                                <s:if test="#request.provinces!=null">
                                    <s:iterator value="#request.provinces" var="province">
                                        <option value=<s:property value="#province.provinceId"/> >
                                            <s:property value='#province.name'/>
                                        </option>
                                    </s:iterator>
                                </s:if>
                            </select>
                            <label class="control-label" for="updateHospitalProvince" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updateHospitalCity" class="col-sm-3 control-label">所属城市（区、县）</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateHospitalCity">
                                <option value="-1" selected disabled>请选择</option>
                            </select>
                            <label class="control-label" for="updateHospitalCity" style="display: none;"></label>
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="updateHospital">
                        修改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>




<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <div class="modal fade" id="updateCityModal" tabindex="-1" role="dialog" aria-labelledby="updateCityModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="updateCityModalLabel">
                        修改城市（区、县）
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->


                    <div class="form-group">
                        <label for="updateCityName" class="col-sm-3 control-label">城市（区、县）名称</label>
                        <div class="col-sm-7">
                            <input type="hidden" id="updateCityId">
                            <input type="text" class="form-control" id="updateCityName">
                            <label class="control-label" for="updateCityName" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updateCityProvince" class="col-sm-3 control-label">所属省份（直辖市）</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="updateCityProvince">
                                <option value="-1" selected disabled>请选择</option>
                                <s:if test="#request.provinces!=null">
                                    <s:iterator value="#request.provinces" var="province">
                                        <option value=<s:property value="#province.provinceId"/> >
                                            <s:property value='#province.name'/>
                                        </option>
                                    </s:iterator>
                                </s:if>
                            </select>
                            <label class="control-label" for="updateCityProvince" style="display: none;"></label>
                        </div>
                    </div>
                    <!---------------------表单-------------------->

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="updateCity">
                        修改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>

<!--------------------------------------添加的模糊框------------------------>
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="addCityModal" tabindex="-1" role="dialog" aria-labelledby="CityModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="CityModalLabel">
                        添加新城市（区、县）
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->
                    <div class="form-group">
                        <label for="addProvinceName" class="col-sm-3 control-label">城市（区、县）名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addCityName" placeholder="请输入城市（区、县）名称">
                            <label class="control-label" for="addProvince" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="addCityProvince" class="col-sm-3 control-label">所属省份（直辖市）</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addCityProvince">
                                <option value="-1" selected disabled>请选择</option>
                                <s:if test="#request.provinces!=null">
                                <s:iterator value="#request.provinces" var="province">
                                    <option value=<s:property value="#province.provinceId"/> >
                                        <s:property value='#province.name'/>
                                        </option>
                                </s:iterator>
                                </s:if>
                            </select>
                            <label class="control-label" for="addCityProvince" style="display: none;"></label>
                        </div>
                    </div>

                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="addCity">
                        添加
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>


<!--------------------------------------添加的模糊框------------------------>
<form class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="addHospitalModal" tabindex="-1" role="dialog" aria-labelledby="addHospitalModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="addHospitalModalLabel">
                        添加新医院
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->
                    <div class="form-group">
                        <label for="addHospitalName" class="col-sm-3 control-label">医院名称</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addHospitalName" placeholder="请输入医院名称">
                            <label class="control-label" for="addHospitalName" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="addHospitalProvince" class="col-sm-3 control-label">所属省份（直辖市）</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addHospitalProvince" onchange="loadCity()">
                                <option value="-1" selected disabled>请选择</option>
                                <s:if test="#request.provinces!=null">
                                    <s:iterator value="#request.provinces" var="province">
                                        <option value=<s:property value="#province.provinceId"/> >
                                            <s:property value='#province.name'/>
                                        </option>
                                    </s:iterator>
                                </s:if>
                            </select>
                            <label class="control-label" for="addHospitalProvince" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="addHospitalCity" class="col-sm-3 control-label">所属城市（区、县）</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="addHospitalCity">
                                <option value="-1" selected disabled>请选择</option>
                            </select>
                            <label class="control-label" for="addHospitalCity" style="display: none;"></label>
                        </div>
                    </div>

                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="addHospital">
                        添加
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>


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
                        <label for="oldPwd" class="col-sm-3 control-label">原密码</label>
                        <div class="col-sm-7">
                            <input type="password" class="form-control" id="oldPwd" placeholder="请输入原密码">
                            <label class="control-label" for="oldPwd" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="newPwd" class="col-sm-3 control-label">新密码</label>
                        <div class="col-sm-7">
                            <input type="password" class="form-control" id="newPwd" placeholder="请输入新密码">
                            <label class="control-label" for="newPwd" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="confirmPwd" class="col-sm-3 control-label">确认密码</label>
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
                        <label for="username" class="col-sm-3 control-label">用户名</label>
                        <div class="col-sm-7">

                            <input type="text" class="form-control" id="username"
                                   value='<s:property value="#session.doctor.username"/>'>
                            <label class="control-label" for="username" style="display: none;"></label>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="name" class="col-sm-3 control-label">真实姓名</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="name" placeholder="请输入您的真实姓名"
                                   value='<s:property value="#session.doctor.name"/>'>
                            <label class="control-label" for="name" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="phone" class="col-sm-3 control-label">联系号码</label>
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
<script src="${pageContext.request.contextPath}/js/getAllPatientTypes.js"></script>
</html>