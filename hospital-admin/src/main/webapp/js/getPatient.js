
function getPatientInfo(id){

	ajax(
			  {
			  	method:'POST',
	    		url:'doctor/patientManageAction_getPatient.action',
				params: "patientId=" + id,
				type:"json",
	    		callback:function(data) {
					$("#findOpenID").val(data.openID);
					$("#findPatientName").val(data.name);
					$("#findEmail").val(data.email);
					$("#findPhone").val(data.phone);
					$("#findPatientType").val(data.patientType.patientTypeName);
					$("#findDoctor").val(data.doctor.name);
				}
			}
   
	);
}
				
			
				
				
		   
	
	
			





