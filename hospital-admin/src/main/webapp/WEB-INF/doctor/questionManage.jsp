<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
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

    <!-- add specific js in here -->
    <script src="${pageContext.request.contextPath}/js/jquery.tablednd_0_5.js"></script>
    <script src="${pageContext.request.contextPath}/js/sortQuestion.js"></script>
    <script src="${pageContext.request.contextPath}/js/addQuestion.js"></script>
    <script src="${pageContext.request.contextPath}/js/updateQuestion.js"></script>
    <script src="${pageContext.request.contextPath}/js/deleteQuestion.js"></script>
    <script src="${pageContext.request.contextPath}/js/getQuestionInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>

    <!-- add specific js in here -->

    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/foundation/6.0.5/foundation.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <style>
        /** 新增按钮 **/
        .addVar, .addUpdateVar {
            margin: 0 0 0 52px;
            padding: 5px;
            display: inline-block;
            background-color: #3A9668;
            color: #f1f1f1;
            border: 1px solid #005;
            border-radius: 4px;
        }

        /** 删除按钮 **/
        .removeVar, .removeUpdateVar {
            margin: auto;
            padding: 5px;
            display: inline-block;
            background-color: #B02109;
            color: #f1f1f1;
            border: 1px solid #005;
            border-radius: 4px;
        }

        .addVar:hover, .addUpdateVar:hover, .removeVar:hover, .removeUpdateVar:hover {
            cursor: pointer;
        }

        input, textarea {
            padding: 5px;
            font-size: 16px;
        }
    </style>
    <script>
        var varCount = 2;       //只增不减，不然很可能出错
        var choiceCount = 2;    //实际数量，有增有减

        $(function () {
            //新增
            $('.addUpdateVar').on('click', function () {
                varCount++;
                choiceCount++;
                $node = '<div class="form-group">'
                    + '<label for="choiceOption' + varCount + '" class="col-sm-3 control-label">选项: </label>'
                    + '<div class="col-sm-5">'
                    + '<input type="text" class="form-control" name="choiceOption' + varCount + '" id="choiceOption' + varCount + '" placeholder="请输入选项内容">'
                    + '<label class="control-label" for="choiceOption' + varCount + '" style="display: none;"></label>'
                    + '</div>'
                    + '<div class="col-sm-2">'
                    + '<input type="text" class="form-control" name="score' + varCount + '" id="score' + varCount + '" placeholder="分数">'
                    + '<label class="control-label" for="score' + varCount + '" style="display: none;"></label>'
                    + '</div>'
                    + '<p><span class="removeUpdateVar">删除</span></p>'
                    + '</div>';
                //新表单项加到“新增”前面
                $(this).parent().before($node);
            });
            //新增
            $('.addVar').on('click', function () {
                varCount++;
                choiceCount++;
                $node = '<div class="form-group">'
                    + '<label for="choiceOption' + varCount + '" class="col-sm-3 control-label">选项: </label>'
                    + '<div class="col-sm-5">'
                    + '<input type="text" class="form-control" name="choiceOption' + varCount + '" id="choiceOption' + varCount + '" placeholder="请输入选项内容">'
                    + '<label class="control-label" for="choiceOption' + varCount + '" style="display: none;"></label>'
                    + '</div>'
                    + '<div class="col-sm-2">'
                    + '<input type="text" class="form-control" name="score' + varCount + '" id="score' + varCount + '" placeholder="分数">'
                    + '<label class="control-label" for="score' + varCount + '" style="display: none;"></label>'
                    + '</div>'
                    + '<p><span class="removeVar">删除</span></p>'
                    + '</div>';
                //新表单项加到“新增”前面
                $(this).parent().before($node);
            });

            //删除
            $('form').on('click', '.removeVar', function () {
                if (getAddCount() <= 2) {
                    alert("至少需要有两条选项!");
                } else {
                    $(this).parent().parent().remove();
                    choiceCount--;
                }
            });
            //删除
            $('form').on('click', '.removeUpdateVar', function () {
                if (getUpdateCount() <= 2) {
                    alert("至少需要有两条选项!");
                } else {
                    $(this).parent().parent().remove();
                    choiceCount--;
                }
            });
        });
    </script>
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

            <div name="showList">
                <div class="grid-btn">
                    <button type="button" class="btn btn-primary" data-toggle="modal"
                            data-target="#addQuestionModal" id="btn_add"
                            onclick="addQuestion(<s:property value="#request.survey.surveyId"/>)"><i
                            class="fa fa-plus"></i> 添加新问题
                    </button>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <table id="data_list" class="table table-hover table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>问题</th>
                            <th>类型</th>
                            <th>起始年龄（含）</th>
                            <th>结束年龄（含）</th>
                            <th>操作</th>
                        </tr>
                        </thead>


                        <!---在此插入信息-->
                        <s:if test="#request.survey.questions!=null">
                            <tbody>
                            <s:iterator value="#request.survey.questions" var="question">
                                <tr id="<s:property value="#question.questionId"/>">
                                    <td><s:property value="#question.questionContent"/></td>
                                    <td>
                                        <s:if test="#question.questionType== 1">
                                            多选题
                                        </s:if>
                                        <s:elseif test="#question.questionType== 2">
                                            单选题
                                        </s:elseif>
                                        <s:elseif test="#question.questionType== 3">
                                            问答题
                                        </s:elseif>
                                        <s:elseif test="#question.questionType== 4">
                                            问答题(日期）
                                        </s:elseif>
                                        <s:elseif test="#question.questionType== 5">
                                            图片题
                                        </s:elseif>
                                    </td>
                                    <td>
                                        <s:if test="#question.startAge==-1">
                                            N/A
                                        </s:if>
                                        <s:else>
                                            <s:property value="#question.startAge"/>
                                        </s:else>
                                    </td>
                                    <td>
                                        <s:if test="#question.endAge==-1">
                                            N/A
                                        </s:if>
                                        <s:else>
                                            <s:property value="#question.endAge"/>
                                        </s:else>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-info btn-xs" data-toggle="modal"
                                                data-target="#findModal"
                                                onclick="getQuestionInfo(<s:property value="#question.questionId"/>)">查看
                                        </button>
                                        <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                                data-target="#updateModal" id="btn_update"
                                                onclick="updateQuestion(<s:property value="#request.survey.surveyId"/>,
                                                    <s:property value="#question.questionId"/>)">修改
                                        </button>
                                        <button type="button" class="btn btn-danger btn-xs"
                                                onclick="deleteQuestion(<s:property value="#question.questionId"/>)">删除
                                        </button>
                                    </td>
                                </tr>
                            </s:iterator>
                            </tbody>
                        </s:if>
                        <s:else>
                            <tbody>
                            <tr>
                                <td>暂无数据</td>
                                <td>暂无数据</td>
                                <td>暂无数据</td>
                                <td>暂无数据</td>
                                <td>暂无数据</td>
                            </tr>
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


<!--------------------------------------增加问卷问题的模糊框------------------------>
<form id="addForm" class="form-horizontal">   <!--保证样式水平不混乱-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="addQuestionModal" tabindex="-1" role="dialog" aria-labelledby="addModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="addModalLabel">
                        新增问卷问题
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->


                    <div class="form-group">
                        <button type="button" class="btn btn-primary" onclick="addMultiChoiceDisplay()" id="add1"><i
                                class="fa fa-plus"></i> 多选题
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="addSingleChoiceDisplay()" id="add2"><i
                                class="fa fa-plus"></i> 单选题
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="addTextDisplay()" id="add3"><i
                                class="fa fa-plus"></i> 问答题
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="addTextDateDisplay()" id="add4"><i
                                class="fa fa-plus"></i> 问答题（日期）
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="addPictureDisplay()" id="add5"><i
                                class="fa fa-plus"></i> 图片题
                        </button>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问题题目</label>
                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" id="addQuestionContent"
                                      placeholder="请输入问题题目"></textarea>
                            <label class="control-label" for="addQuestionContent" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">起始年龄（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addStartAge">
                            <label class="control-label" for="addStartAge" style="display:none;"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">结束年龄（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="addEndAge">
                            <label class="control-label" for="addEndAge" style="display:none;"></label>
                        </div>
                    </div>

                    <div id="choicesBlock">
                        <div class="form-group">
                            <label>
                                <input type="checkbox" id="textChoice" class="col-sm-3 control-label">
                                添加一个可供病人自由回答的选项
                            </label>
                        </div>
                        <div class="form-group">
                            <label for="choiceOption1" class="col-sm-3 control-label">选项: </label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="choiceOption1" id="choiceOption1"
                                       placeholder="请输入选项内容">
                                <label class="control-label" for="choiceOption1" style="display: none;"></label>
                            </div>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="score1" id="score1" placeholder="分数">
                                <label class="control-label" for="score1" style="display: none;"></label>
                            </div>
                            <p><span class="removeVar">删除</span></p>
                        </div>
                        <div class="form-group">
                            <label for="choiceOption2" class="col-sm-3 control-label">选项: </label>
                            <div class="col-sm-5">
                                <input type="text" class="form-control" name="choiceOption2" id="choiceOption2"
                                       placeholder="请输入选项内容">
                                <label class="control-label" for="choiceOption2" style="display: none;"></label>
                            </div>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="score2" id="score2" placeholder="分数">
                                <label class="control-label" for="score2" style="display: none;"></label>
                            </div>
                            <p><span class="removeVar">删除</span></p>
                        </div>
                        <p><span class="addVar">新增一项</span></p>
                    </div>


                    <!---------------------表单-------------------->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="add_Question">
                        确认添加
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!--------------------------------------增加问卷问题的模糊框------------------------>


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
                        查看问题信息
                    </h4>
                </div>
                <div class="modal-body" id="checkQuestion">

                    <!---------------------表单-------------------->

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">问题</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findQuestionContent" readonly="readonly">
                            <label class="control-label" for="findQuestionContent" style="display:none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="firstname" class="col-sm-3 control-label">类型</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findQuestionType" readonly="readonly">
                            <label class="control-label" for="findQuestionType" style="display:none;"></label>
                        </div>
                    </div>

                    <div class="form-group" id="checkTextChoice">
                        <label for="firstname" class="col-sm-3 control-label">含有自由回答选项</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findTextChoice" readonly="readonly">
                            <label class="control-label" for="findTextChoice" style="display:none;"></label>
                        </div>
                    </div>

                    <div class="form-group" id="checkStartAge">
                        <label for="firstname" class="col-sm-3 control-label">起始年龄（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findStartAge" readonly="readonly">
                            <label class="control-label" for="findStartAge" style="display:none;"></label>
                        </div>
                    </div>
                    <div class="form-group" id="checkEndAge">
                        <label for="firstname" class="col-sm-3 control-label">结束年龄（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="findEndAge" readonly="readonly">
                            <label class="control-label" for="findEndAge" style="display:none;"></label>
                        </div>
                    </div>

                    <div id="checkChoicesDiv"></div>

                </div>


                <!---------------------表单-------------------->
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</form>
<!--------------------------------------查看的模糊框------------------------>

<!-- 修改模态框（Modal） -->
<!-------------------------------------------------------------->
<form id="updateForm" class="form-horizontal">   <!--保证样式水平不混乱-->
    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="updateModalLabel">
                        修改问题信息
                    </h4>
                </div>
                <div class="modal-body">

                    <!---------------------表单-------------------->

                    <div class="form-group">
                        <button type="button" class="btn btn-primary" onclick="updateMultiChoiceDisplay()" id="update1">
                            <i
                                    class="fa fa-plus"></i> 多选题
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="updateSingleChoiceDisplay()" id="update2">
                            <i class="fa fa-plus"></i> 单选题
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="updateTextDisplay()" id="update3"><i
                                class="fa fa-plus"></i> 问答题
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="updateTextDateDisplay()" id="update4">
                            <i class="fa fa-plus"></i> 问答题（日期）
                        </button>
                        <button type="button" class="btn btn-pinterest" onclick="updatePictureDisplay()" id="update5">
                            <i class="fa fa-plus"></i> 图片题
                        </button>
                    </div>


                    <div class="form-group">
                        <label for="updateQuestionContent" class="col-sm-3 control-label">问题题目</label>
                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" id="updateQuestionContent"></textarea>
                            <label class="control-label" for="updateQuestionContent" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updateStartAge" class="col-sm-3 control-label">起始年龄（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateStartAge"/>
                            <label class="control-label" for="updateStartAge" style="display: none;"></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="updateEndAge" class="col-sm-3 control-label">结束年龄（含）</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="updateEndAge"/>
                            <label class="control-label" for="updateEndAge" style="display: none;"></label>
                        </div>
                    </div>

                    <div id="updateChoicesBlock">
                        <div class="form-group">
                            <label>
                                <input type="checkbox" id="updateTextChoice" class="col-sm-3 control-label">
                                添加一个可供病人自由回答的选项
                            </label>
                        </div>
                        <div id="updateChoicesDiv"></div>
                        <p><span class="addUpdateVar">新增一项</span></p>
                    </div>

                    <!---------------------表单-------------------->

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary" id="update_Question">
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