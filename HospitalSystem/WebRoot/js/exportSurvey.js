function exportSurvey(){
	 //获得basePath
    basePath=$('#basePath').val();
	ajax(
  		  {
			  	method:'GET',
	    		url:'doctor/surveyManageAction_exportSurvey.action',
	    		callback:function(data) {
	    			showInfo("数据已导出：<a href='" + basePath + data + "'>点击下载</a>");	
				}
			}
			   
  	);
	
}




function showInfo(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}
