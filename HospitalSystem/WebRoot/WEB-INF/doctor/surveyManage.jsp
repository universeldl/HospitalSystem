<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"   prefix="s"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
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
            
            <script src="${pageContext.request.contextPath}/ajax-lib/ajaxutils.js"></script>
            <script src="${pageContext.request.contextPath}/js/doctorUpdateInfo.js"></script>
             <script src="${pageContext.request.contextPath}/js/doctorUpdatePwd.js"></script>

                
                
       <script src="${pageContext.request.contextPath}/js/getAllSurveyTypes.js"></script>
       			 <script src="${pageContext.request.contextPath}/js/addSurvey.js"></script>
                
                 <script src="${pageContext.request.contextPath}/js/updateSurvey.js"></script>
                  <script src="${pageContext.request.contextPath}/js/deleteSurvey.js"></script>
                   <script src="${pageContext.request.contextPath}/js/getSurveyInfo.js"></script>
                   <script src="${pageContext.request.contextPath}/js/addSurveyNum.js"></script>
        	      <script type="text/javascript" src="${pageContext.request.contextPath}/jQuery/ajaxfileupload.js"></script>
				 <script src="${pageContext.request.contextPath}/js/ajax_upload.js"></script>
				 <script src="${pageContext.request.contextPath}/js/batchAddSurvey.js"></script>
				 <script src="${pageContext.request.contextPath}/js/exportSurvey.js"></script>
</head>



<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

<script type="text/javascript">
function choiceDisplay()
{
    var options = new Array("inputOptionA", "inputOptionB", "inputOptionC", "inputOptionD", "inputOptionE");
    for(var i=0;i<options.length;i++)
      document.getElementById(options[i]).style.display="block";
}
function choiceHide()
{
    var options = new Array("inputOptionA", "inputOptionB", "inputOptionC", "inputOptionD", "inputOptionE");
    for(var i=0;i<options.length;i++)
        document.getElementById(options[i]).style.display="none";
}
</script>


<body class="bootstrap-doctor-with-small-navbar">
    <nav class="navbar navbar-inverse navbar-fixed-top bootstrap-doctor-navbar bootstrap-doctor-navbar-under-small" role="navigation">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="collapse navbar-collapse main-navbar-collapse">
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/doctor/doctor.jsp"><strong>欢迎使用呼吸天使问卷管理系统</strong></a>
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a href="#" role="button" class="dropdown-toggle" data-hover="dropdown"> <i class="glyphicon glyphicon-user"></i> 欢迎您， <s:property value="#session.doctor.name"/> <i class="caret"></i></a>
                            
                                 <ul class="dropdown-menu">
                                 <li><a href="#updateinfo" data-toggle="modal">个人资料</a></li>
                                      <li role="presentation" class="divider"></li>
                                    <li><a href="#updatepwd" data-toggle="modal">修改密码</a></li>
                                     <li role="presentation" class="divider"></li>
                                    <li><a href="${pageContext.request.contextPath}/doctorLoginAction_logout.action">退出</a></li>
                                </ul>
                                
                            </li>
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
                    <li  class="active">
                        <a href="${pageContext.request.contextPath}/doctor/surveyManageAction_findSurveyByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 问卷管理</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/surveyTypeManageAction_findSurveyTypeByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 问卷分类管理</a>
                    </li>
                    <li >
                        <a href="${pageContext.request.contextPath}/doctor/deliveryManageAction_findDeliveryInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 问卷分发</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/retrieveManageAction_findRetrieveInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 答卷管理</a>
                    </li>
                    
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/deliverySearchAction_findRetrieveInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 分发查询</a>
                    </li>
                     <li>
                        <a href="${pageContext.request.contextPath}/doctor/forfeitManageAction_findForfeitInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 逾期处理</a>
                    </li>
              <s:if test="#session.doctor.authorization.superSet==1"><!-- 对超级医生和普通医生进行权限区分 -->
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/doctorManageAction_findDoctorByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 医生管理</a>
                    </li>
               </s:if>
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/patientManageAction_findPatientByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 病人管理</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/patientTypeManageAction_getAllPatientType.action"><i class="glyphicon glyphicon-chevron-right"></i> 系统设置</a>
                    </li>
                </ul>
            </div>

  <!-- content -->
            <div class="col-md-10">
                
                    
                        
                            
                        
                    
                
                <div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-default bootstrap-doctor-no-table-panel">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">查询</div>
                            </div>
                            <div class="bootstrap-doctor-no-table-panel-content bootstrap-doctor-panel-content collapse in">
                                <form class="form-horizontal" action="${pageContext.request.contextPath}/doctor/surveyManageAction_querySurvey.action" method="post">

                                     <div class="col-lg-5 form-group">
                                        <label class="col-lg-4 control-label" for="query_bno1">问卷分类</label>
                                          <div class="col-lg-8">
                                        <select class="form-control" id="surveyTypeId" name="surveyTypeId">
                                            <option value="-1">请选择</option>                                         
                                        </select>
                                        
                                    </div>
                                    </div>
                                       
                                    <div class="col-lg-5 form-group">
                                        <label class="col-lg-4 control-label" for="query_bname">问卷名称</label>
                                        <div class="col-lg-8">
                                            <input class="form-control" id="surveyName" name="surveyName" type="text" value="">
                                            <label class="control-label" for="query_bname" style="display: none;"></label>
                                        </div>
                                    </div>
                                    
                                  <div class="col-lg-5 form-group">
                                        <label class="col-lg-4 control-label" for="query_bname2">作者名称</label>
                                        <div class="col-lg-8">
                                            <input class="form-control" id="author" name="author" type="text" value="">
                                            <label class="control-label" for="query_bname2" style="display: none;"></label>
                                        </div>
                                    </div>
                                    
                                     <div class="col-lg-5 form-group">
                                        <label class="col-lg-4 control-label" for="query_bname3">科室名称</label>
                                        <div class="col-lg-8">
                                            <input class="form-control" id="department" name="department" type="text" value="">
                                            <label class="control-label" for="query_bname2" style="display: none;"></label>
                                        </div>
                                    </div>
                                    
                                  
                                    
                                  
                                    <div class="col-lg-2 form-group">

                                        <button type="submit" class="btn btn-primary" id="btn_query" onclick="query()">查询</button>
                                    </div>
                                    <div class="col-lg-2 form-group">

                                        <button type="button" class="btn btn-primary" id="btn_add" data-toggle="modal" data-target="#addModal">添加问卷</button>
                                    </div>
                                    
                                      <div class="col-lg-2 form-group">
											 <button type="button" class="btn btn-primary"   data-toggle="modal" data-target="#batchAddModal">批量添加</button>
										 </div>
										 
										  <div class="col-lg-2 form-group">
											 <button type="button" class="btn btn-primary" onclick="exportSurvey()">导出</button>
										 </div>
                                </form>
                            </div>
                        </div>
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
	                                  <td><s:date name="#survey.putdate" format="yyyy-MM-dd" /></td>
	                                    <td><s:property value="#survey.num"/></td>
	                                    <td><s:property value="#survey.currentNum"/></td>
	                                <td>
	                                <button type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#findModal" onclick="getSurveyInfo(<s:property value="#survey.surveyId"/>)" >查看</button>
	                                	<button type="button" class="btn btn-warning btn-xs" data-toggle="modal" data-target="#updateModal" id="btn_update" onclick="updateSurvey(<s:property value="#survey.surveyId"/>)">修改</button>
	                                	<button type="button" class="btn btn-danger btn-xs" onclick="deleteSurvey(<s:property value="#survey.surveyId"/>)">删除</button>
	                                	<button type="button" class="btn btn-success btn-xs" onclick="addSurveyNum(<s:property value="#survey.surveyId"/>)"  data-toggle="modal" data-target="#addNumModal">新增</button>
										<button type="button" class="btn btn-primary btn-xs" onclick="checkSurvey(<s:property value="#survey.surveyId"/>)"  data-toggle="modal" data-target="#checkSurvey">浏览</button>
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
                           <li class="disabled"><a href="#">第<s:property value="#request.pb.pageCode"/>页/共<s:property value="#request.pb.totalPage"/>页</a></li>
                           <li><a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=1">首页</a></li>
                           <li><a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${pb.pageCode-1 }">&laquo;</a></li><!-- 上一页 -->
                           <%-- 循环显示页码列表 --%>
								<c:forEach begin="${begin }" end="${end }" var="i">
								  <c:choose>
								  <%--如果是当前页则设置无法点击超链接 --%>
								  	<c:when test="${i eq pb.pageCode }">							  	
								  			<li class="active"><a>${i }</a><li>							 
								  	</c:when>
								  	<c:otherwise>
								  		<li><a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${i}">${i}</a></li>
								  	</c:otherwise>
								  </c:choose>
								</c:forEach>
				        	   <%--如果当前页数没到总页数，即没到最后一页,则需要显示下一页 --%>
							  <c:if test="${pb.pageCode < pb.totalPage }">
								  <li><a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${pb.pageCode+1}">&raquo;</a></li>
							</c:if>
							<%--否则显示尾页 --%>
							<li><a href="${pageContext.request.contextPath}/doctor/surveyManageAction_${pb.url }pageCode=${pb.totalPage}">尾页</a></li>
							</ul>
                           </div>
                    </s:if>           
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    
    
      
                   <!-------------------------------------批量添加的模糊框------------------------>  
                                 <form class="form-horizontal">   <!--保证样式水平不混乱-->   
                                        <!-- 模态框（Modal） -->
									<div class="modal fade" id="batchAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
									<div class="modal fade" id="findModal" tabindex="-1" role="dialog" aria-labelledby="findModalLabel" aria-hidden="true">
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
													<input type="text" class="form-control" id="findSurveyName"  readonly="readonly">
												
												</div>
										</div>
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">问卷类型</label>
											<div class="col-sm-7">
												<input type="text" class="form-control" id="findSurveyType"  readonly="readonly">
												
											</div>
										</div>
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">作者名称</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="findAuthor"  readonly="readonly">
												
												</div>
										</div>
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">科室</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="findDepartment"  readonly="readonly">
												
												</div>
										</div>
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">总分发数</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="findNum"  readonly="readonly">
												
												</div>
										</div>
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">当前数量</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="findCurrentNum"  readonly="readonly">
												
												</div>
										</div>
										

										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">操作医生</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="findDoctor"  readonly="readonly">
												
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
									<div class="modal fade" id="checkSurvey" tabindex="-1" role="dialog" aria-labelledby="addNumModalLabel" aria-hidden="true">
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

                                        <% int i=1; %>
                            			<s:iterator value="#request.myAnswers" var="answer">
											<div class="form-group">
												<label for="firstname" class="col-lg-3 control-label"></label>
												<div class="col-lg-7">
                                                    <p class="text-left"><b><%=i%>.  ${answer.question.questionContent}.</b></p>
												</div>
											</div>
												<s:iterator value="#answer.question.choices" var="choice">
												<div class="form-group">
													<label for="firstname" class="col-lg-3 control-label"></label>
													<div class="form-group" class="col-lg-7  col-lg-offset-3">
													   <label>
                                                            <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1">${choice.choiceContent}.
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
													<button type="button" class="btn btn-primary" id="add_SurveyNum">
														新增
													</button>
												</div>
											</div><!-- /.modal-content -->
										</div><!-- /.modal -->
									</div>

                                 </form>
 								<!--------------------------------------浏览问卷的模态框------------------------>


    
    
    
     <!--------------------------------------增加问卷问题的模糊框------------------------>
                                 <form class="form-horizontal">   <!--保证样式水平不混乱-->   
                                        <!-- 模态框（Modal） -->
									<div class="modal fade" id="addNumModal" tabindex="-1" role="dialog" aria-labelledby="addNumModalLabel" aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
														&times;
													</button>
													<h4 class="modal-title" id="addNumModalLabel">
														新增问卷问题
													</h4>
												</div>
												<div class="modal-body">
												
										<!---------------------表单-------------------->


                                            <button type="button" class="btn btn-default" onclick="choiceDisplay()" id="add1">选择题</button>
                                            <button type="button" class="btn btn-default" onclick="choiceHide()" id="add2">问答题</button>

                                                <input type="hidden" value="1" name="qstType">
                                                <div class="form-group">
                                                    <label for="firstname" class="col-sm-3 control-label">问题题目</label>
                                                    <div class="col-sm-7">
                                                        <textarea class="form-control" rows="3" id="addDescription" placeholder="请输入问题题目"></textarea>
                                                        <label class="control-label" for="addDescription" style="display: none;"></label>
                                                    </div>
                                                </div>
										        <div class="form-group" id="inputOptionA">
										        	<label for="inputOptionA" class="col-sm-3 control-label">A. </label>
											        	<div class="col-sm-7">
												        	<input type="text" class="form-control" id="inputOption" placeholder="请输入选项内容">
												        <label class="control-label" for="inputOption" style="display: none;"></label>
												        </div>
										        </div>
										        <div class="form-group" id="inputOptionB">
										        	<label for="inputOptionB" class="col-sm-3 control-label">B. </label>
											        	<div class="col-sm-7">
												        	<input type="text" class="form-control" id="inputOption" placeholder="请输入选项内容">
												        <label class="control-label" for="inputOption" style="display: none;"></label>
												        </div>
										        </div>
										        <div class="form-group" id="inputOptionC">
										        	<label for="inputOptionC" class="col-sm-3 control-label">C. </label>
											        	<div class="col-sm-7">
												        	<input type="text" class="form-control" id="inputOption" placeholder="请输入选项内容">
												        <label class="control-label" for="inputOption" style="display: none;"></label>
												        </div>
										        </div>
										        <div class="form-group" id="inputOptionD">
										        	<label for="inputOptionD" class="col-sm-3 control-label">D. </label>
											        	<div class="col-sm-7">
												        	<input type="text" class="form-control" id="inputOption" placeholder="请输入选项内容">
												        <label class="control-label" for="inputOption" style="display: none;"></label>
												        </div>
										        </div>
										        <div class="form-group" id="inputOptionE">
										        	<label for="inputOptionE" class="col-sm-3 control-label">E. </label>
											        	<div class="col-sm-7">
												        	<input type="text" class="form-control" id="inputOption" placeholder="请输入选项内容">
												        <label class="control-label" for="inputOption" style="display: none;"></label>
												        </div>
										        </div>
                                                <input type="submit" class="btn btn-success" id="add_qst" value="继续添加">


										<!---------------------表单-------------------->
                                    </div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭
													</button>
													<button type="button" class="btn btn-primary" id="add_SurveyNum">
														新增
													</button>
												</div>
											</div><!-- /.modal-content -->
										</div><!-- /.modal -->
									</div>

                                 </form>	
 								<!--------------------------------------增加问卷问题的模糊框------------------------>
    
    
    
    
    

    
    
    
    
    
    
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
													<input type="text" class="form-control" id="addSurveyName"  placeholder="请输入问卷名称">
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
													<input type="text" class="form-control" id="addAuthor"  placeholder="请输入作者名称">
												<label class="control-label" for="addAuthor" style="display: none;"></label>	
												</div>
										</div>
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">科室</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addDepartment"  placeholder="请输入科室">
												<label class="control-label" for="addDepartment" style="display: none;"></label>	
												</div>
										</div>
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">总分发数</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addNum"  placeholder="请输入问卷总分发数">
												<label class="control-label" for="addNum" style="display: none;"></label>	
												</div>
										</div>
										

										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">简介</label>
												<div class="col-sm-7">
												<textarea class="form-control" rows="3" id="addDescription" placeholder="请输入问卷简介"></textarea>
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
									<div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel" aria-hidden="true">
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
													<input type="text" class="form-control" id="updateSurveyName"  placeholder="请输入问卷名称">
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
											<label for="firstname" class="col-sm-3 control-label">作者名称</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="updateAuthor"  placeholder="请输入作者名称">
												<label class="control-label" for="updateAuthor" style="display: none;"></label>
												</div>
										</div>


										<div class="form-group">
											<label for="firstname" class="col-sm-3 control-label">科室</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="updateDepartment"  placeholder="请输入科室">
												<label class="control-label" for="updateDepartment" style="display: none;"></label>
												</div>
										</div>



										<div class="form-group">
											<label for="firstname" class="col-sm-3 control-label">简介</label>
												<div class="col-sm-7">
												<textarea class="form-control" rows="3" id="updateDescription" placeholder="请输入问卷简介"></textarea>
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
				<div class="modal fade" id="updatepwd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
									<input type="password" class="form-control" id="oldPwd"  placeholder="请输入原密码">
										<label class="control-label" for="oldPwd" style="display: none;"></label>
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">新密码</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" id="newPwd"  placeholder="请输入新密码">
											<label class="control-label" for="newPwd" style="display: none;"></label>	
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">确认密码</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" id="confirmPwd"  placeholder="请输入确认密码">
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
									<input type="text" class="form-control" id="username"  value='<s:property value="#session.doctor.username"/>'>
											<label class="control-label" for="username" style="display: none;"></label>		
								</div>
							</div>				
								
								
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">真实姓名</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" id="name"  placeholder="请输入您的真实姓名" value='<s:property value="#session.doctor.name"/>'>
											<label class="control-label" for="name" style="display: none;"></label>	
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">联系号码</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" id="phone"  placeholder="请输入您的联系号码" value='<s:property value="#session.doctor.phone"/>'>
											<label class="control-label" for="phone" style="display: none;"></label>
													
								</div>
							</div>	
							
								<!--正文-->
								
								
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default" data-dismiss="modal">关闭
								</button>
								<button type="button" class="btn btn-primary" id="doctor_updateInfo" >
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
				                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
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
