<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"   prefix="s"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
              <script src="${pageContext.request.contextPath}/js/updateDoctor.js"></script>
              <script src="${pageContext.request.contextPath}/js/addDoctor.js"></script>
                <script src="${pageContext.request.contextPath}/js/deleteDoctor.js"></script>
        <script src="${pageContext.request.contextPath}/js/updateAuthorization.js"></script>
</head>



<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>


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
                    <li>
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
                    <li class="active">
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
                                <form class="form-horizontal" action="${pageContext.request.contextPath}/doctor/doctorManageAction_queryDoctor.action" method="post">
                                    <div class="col-lg-5 form-group">
                                        <label class="col-lg-4 control-label" for="query_ano">医生用户名</label>
                                        <div class="col-lg-8">
                                            <input class="form-control" type="text" id="doctorUserName" name="doctorUserName">
                                            <label class="control-label" for="doctorUserName" style="display: none;"></label>
                                        </div>
                                    </div>
                                    <div class="col-lg-5 form-group">
                                        <label class="col-lg-4 control-label" for="query_aname" >医生名称</label>
                                        <div class="col-lg-8">
                                            <input class="form-control" type="text" id="doctorName" name="doctorName">
                                            <label class="control-label" for="doctorName" style="display: none;"></label>
                                        </div>
                                    </div>
                                    <div class="col-lg-2 form-group">
                                        <button type="submit" class="btn btn-primary" id="btn_query" onclick="query()">查询</button>
                                        <button type="button" class="btn btn-primary" id="btn_add" data-toggle="modal" data-target="#addModal">添加</button>          
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
                                <th>医生用户名</th>
                                <th>医生姓名</th>
                                <th>联系号码</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            
                            
                            <!---在此插入信息-->
                            <s:if test="#request.pb.beanList!=null">
                            <s:iterator value="#request.pb.beanList" var="doctor">
                             <tbody>
	                         	   <td><s:property value="#doctor.username"/></td>
	                                <td><s:property value="#doctor.name"/></td>
	                                <td><s:property value="#doctor.phone"/></td>
	                                <td>
	                                	<s:if test="#doctor.authorization.superSet!=1">
		                                	<button type="button" class="btn btn-warning btn-xs" data-toggle="modal" data-target="#updateModal" onclick="updateDoctor(<s:property value="#doctor.aid"/>)">修改</button>
		                                	<button type="button" class="btn btn-danger btn-xs" onclick="deleteDoctor(<s:property value="#doctor.aid"/>)">删除</button>
		                                	<button type="button" class="btn btn-success btn-xs" data-toggle="modal" data-target="#powerModal" onclick="power(<s:property value="#doctor.aid"/>)">权限</button>
	                                	 </s:if>
	                                	  <s:else>
	                                	  			超级医生
	                                	    </s:else>
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
                           <li><a href="${pageContext.request.contextPath}/doctor/doctorManageAction_${pb.url }pageCode=1">首页</a></li>
                           <li><a href="${pageContext.request.contextPath}/doctor/doctorManageAction_${pb.url }pageCode=${pb.pageCode-1 }">&laquo;</a></li><!-- 上一页 -->
                           <%-- 循环显示页码列表 --%>
								<c:forEach begin="${begin }" end="${end }" var="i">
								  <c:choose>
								  <%--如果是当前页则设置无法点击超链接 --%>
								  	<c:when test="${i eq pb.pageCode }">							  	
								  			<li class="active"><a>${i }</a><li>							 
								  	</c:when>
								  	<c:otherwise>
								  		<li><a href="${pageContext.request.contextPath}/doctor/doctorManageAction_${pb.url }pageCode=${i}">${i}</a></li>
								  	</c:otherwise>
								  </c:choose>
								</c:forEach>
				        	   <%--如果当前页数没到总页数，即没到最后一页,则需要显示下一页 --%>
							  <c:if test="${pb.pageCode < pb.totalPage }">
								  <li><a href="${pageContext.request.contextPath}/doctor/doctorManageAction_${pb.url }pageCode=${pb.pageCode+1}">&raquo;</a></li>
							</c:if>
							<%--否则显示尾页 --%>
							<li><a href="${pageContext.request.contextPath}/doctor/doctorManageAction_${pb.url }pageCode=${pb.totalPage}">尾页</a></li>
							</ul>
                           </div>
                    </s:if>           
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    
    
    
    
    
    
    
    
    
    
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
														添加新医生
													</h4>
												</div>
												<div class="modal-body">
												
										<!---------------------表单-------------------->
										 <div class="form-group">
											<label for="firstname" class="col-sm-3 control-label">用户名</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addUsername"  placeholder="请输入医生用户名">
												<label class="control-label" for="addUsername" style="display:none;"></label>
												</div>
										</div>
											
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">真实姓名</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addName"  placeholder="请输入医生真实姓名">
													<label class="control-label" for="addName" style="display:none;"></label>
												</div>
										</div>
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">联系电话</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addPhone"  placeholder="请输入医生联系电话">
													<label class="control-label" for="addPhone" style="display:none;"></label>
												</div>
										</div>
										<!---------------------表单-------------------->
									</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭
													</button>
													<button type="button" class="btn btn-primary" id="addDoctor">
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
														修改医生信息
													</h4>
												</div>
												<div class="modal-body">
												
										<!---------------------表单-------------------->
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">用户名</label>
												<div class="col-sm-7">
													<input type="hidden" id="updateId">
													<input type="text" class="form-control" id="updateUsername">
												<label class="control-label" for="updateUsername" style="display:none;"></label>
												</div>
										</div>
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">真实姓名</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="updateName"  placeholder="请输入医生真实姓名">
												<label class="control-label" for="updateName" style="display:none;"></label>
												</div>
										</div>
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">联系电话</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="updatePhone"  placeholder="请输入医生联系电话">
												<label class="control-label" for="updatePhone" style="display:none;"></label>
												</div>
										</div>
										
										
										<!---------------------表单-------------------->
															
										</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭
													</button>
													<button type="button" class="btn btn-primary" id="updateDoctor">
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
										<label class="control-label" for="oldPwd" style="display:none;"></label>		
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">新密码</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" id="newPwd"  placeholder="请输入新密码">
										<label class="control-label" for="newPwd" style="display:none;"></label>			
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">确认密码</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" id="confirmPwd"  placeholder="请输入确认密码">
										<label class="control-label" for="confirmPwd" style="display:none;"></label>			
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
											<label class="control-label" for="username" style="display:none;"></label>		
								</div>
							</div>				
								
								
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">真实姓名</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" id="name"  placeholder="请输入您的真实姓名" value='<s:property value="#session.doctor.name"/>'>
											<label class="control-label" for="name" style="display:none;"></label>			
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">联系号码</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" id="phone"  placeholder="请输入您的联系号码" value='<s:property value="#session.doctor.phone"/>'>
									<label class="control-label" for="phone" style="display:none;"></label>		
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
				    
    
        				  <!-- 权限模态框（Modal） -->
                                     <!-------------------------------------------------------------->  
                                
                                        <!-- 权限模态框（Modal） -->
                               <form class="form-horizontal">   <!--保证样式水平不混乱-->   
									<div class="modal fade" id="powerModal" tabindex="-1" role="dialog" aria-labelledby="powerModalLabel" aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
														&times;
													</button>
													<h4 class="modal-title" id="powerModalLabel">
														设置医生权限
													</h4>
												</div>
												<div class="modal-body">
												
										<!---------------------表单-------------------->
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">问卷分类管理权限</label>
												<div class="col-sm-7">
													<input type="hidden" id="aid">
													<input type="checkbox"  name="power" id="typeSet" value="typeSet">
												</div>
										</div>
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">问卷管理权限</label>
												<div class="col-sm-7">
													<input type="checkbox" name="power" id="surveySet" value="surveySet">
												</div>
										</div>
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">病人管理权限</label>
												<div class="col-sm-7">
													<input type="checkbox" name="power" id="patientSet" value="patientSet">
												</div>
										</div>
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">分发管理权限</label>
												<div class="col-sm-7">
													<input type="checkbox" name="power" id="deliverySet" value="deliverySet">
												</div>
										</div>
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">答卷管理权限</label>
												<div class="col-sm-7">
													<input type="checkbox" name="power" id="retrieveSet" value="retrieveSet">
												</div>
										</div>
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">逾期管理权限</label>
												<div class="col-sm-7">
													<input type="checkbox" name="power" id="forfeitSet" value="forfeitSet">
												</div>
										</div>
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">系统设置权限</label>
												<div class="col-sm-7">
													<input type="checkbox" name="power" id="sysSet" value="sysSet">
												</div>
										</div>
										
										
										<!---------------------表单-------------------->
															
										</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭
													</button>
													<button type="button" class="btn btn-primary"  onclick="setPower()">
														设置
													</button>
												</div>
											</div><!-- /.modal-content -->
										</div><!-- /.modal -->
									</div>
	
                                 </form>
                                   <!-------------------------------------------------------------->
 
 

 
</body>
</html>
