
$(function () {
	

	
		$('#modal_info').on('hide.bs.modal',function() {//提示模糊框隐藏时候触发
       		 location.reload();  	//刷新当前页面
    	});
	
	

});



function getSurveyInfo(id){
		
	ajax(
		  {
		  	method:'POST',
    		url:'doctor/surveyManageAction_getSurvey.action',
			params: "surveyId=" + id,
			type:"json",
    		callback:function(data) {
				$("#findSurveyName").val(data.surveyName);
				$("#findSurveyType").val(data.surveyType.typeName);
				$("#findAuthor").val(data.author);
				$("#findDepartment").val(data.department);
				$("#findDescription").val(data.description);
				$("#findNum").val(data.num);
				$("#findDoctor").val(data.doctor.name);
				$("#findCurrentNum").val(data.currentNum);
			}
		}
										   
							    
						
	);	
	
	
	
	
	
			

}



function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


