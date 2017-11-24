<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"   prefix="s"%>

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
                                <a href="#" role="button" class="dropdown-toggle" data-hover="dropdown"> <i class="glyphicon glyphicon-user"></i> 欢迎您， <s:property value="#session.doctor.name"/>  医生<i class="caret"></i></a>
                            
                                 <ul class="dropdown-menu">
                                     <li><a href="#updateinfo" data-toggle="modal">个人资料</a></li>
                                      <li role="presentation" class="divider"></li>
                                       <li><a href="#updatepwd" data-toggle="modal">修改密码</a></li>
                                        <li role="presentation" class="divider"></li>
                                     <!-- href="#identifier"  来指定要切换的特定的模态框（带有 id="identifier"）。-->  
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
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/deliveryManageAction_findDeliveryInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 问卷分发</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/retrieveManageAction_findRetrieveInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 答卷管理</a>
                    </li>
                    
                    <li>
                        <a href="${pageContext.request.contextPath}/doctor/deliverySearchAction_findRetrieveInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 分发查询</a>
                    </li>
                     <li>
                        <a href="${pageContext.request.contextPath}/doctor/forfeitManageAction_findForfeitInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i>逾期处理</a>
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
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">问卷管理</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>根据问卷编号、问卷名称查询问卷基本信息</li>
                                    <li>添加、修改、删除问卷</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">问卷分类管理</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>根据分类名称查询问卷分类信息</li>
                                    <li>添加、修改、删除问卷分类</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">问卷分发</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>根据用户名、问卷编号分发问卷</li>
                                    <li>展示此用户名的分发信息</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">答卷管理</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>根据用户名、问卷编号答卷问卷</li>
                                    <li>展示此用户名的分发信息</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">分发查询</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>展示所有病人的问卷分发信息</li>
                                    <li>可根据问卷编号、问卷名称、用户名、姓名进行查询</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                   <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">病人管理</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>根据用户名、姓名查询病人基本信息</li>
                                    <li>添加、修改、删除病人信息</li>
                                </ul>
                            </div>
                        </div>
                </div>
             
            </div>
            
            
            <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">逾期处理</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>逾期处理</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                   <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-doctor-box-title">系统设置</div>
                            </div>
                            <div class="bootstrap-doctor-panel-content">
                                <ul>
                                    <li>系统设置</li>
                                </ul>
                            </div>
                        </div>
                </div>
             
            </div>
            
        </div>
    </div>
    
    
    
    
    
    
    
    
    
    
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