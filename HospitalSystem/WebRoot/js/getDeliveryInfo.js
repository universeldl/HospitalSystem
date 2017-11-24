
$(function () {
	

	
		$('#modal_info').on('hide.bs.modal',function() {//提示模糊框隐藏时候触发
       		 location.reload();  	//刷新当前页面
    	});
	
	

});



function getDeliveryInfoById(id){
	ajax(
		  {
		  	method:'POST',
    		url:'doctor/deliveryManageAction_getDeliveryInfoById.action',
			params: "deliveryId=" + id,
			type:"json",
    		callback:function(data) {
				
				$("#deliveryId").val(data.deliveryId);
				$("#surveyName").val(data.survey.surveyName);
				$("#surveyType").val(data.survey.surveyType.typeName);
				$("#openID").val(data.patient.openID);
				$("#patientName").val(data.patient.name);
				$("#patientType").val(data.patient.patientType.patientTypeName);
				$("#overday").val(data.overday);
				if (data.state == 0) {
					$("#state").val("未答卷");
				}else if(data.state == 1){
					$("#state").val("逾期未答卷");
				}else if(data.state == 2){
					$("#state").val("答卷");
				}else if(data.state == 3){
					$("#state").val("重发未答卷");
				}else if(data.state == 4){
					$("#state").val("重发逾期未答卷");
				}else if(data.state == 5){
					$("#state").val("重发答卷");
				}
				$("#doctor").val(data.doctor.name);
			}
		}
										   
							    
						
	);	
	
	
	
	
	
			

}





function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


