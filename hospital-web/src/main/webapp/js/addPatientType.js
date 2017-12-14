
$(function () {
	

    $('#addPatientType').click(function () {

    	
    	if (!validAddPatientType()) {
            return;
        }
    	
	var postdata = "patientTypeName="+$.trim($("#addType").val())+"&maxNum="+$.trim($("#addMaxNum").val())+"&bday="+ $.trim($("#addBday").val())
	+"&penalty="+ $.trim($("#addPenalty").val())+"&resendDays="+ $.trim($("#addresendDays").val());
	ajax(
    		  {
			  	method:'POST',
	    		url:'doctor/patientTypeManageAction_addPatientType.action',
				params: postdata,
	    		callback:function(data) {
					if (data == 1) {
						$("#addModal").modal("hide");//关闭模糊框		
						showInfo("添加成功");	

	                }else {
						$("#addModal").modal("hide");//关闭模糊框
						showInfo("添加失败");
					}
								
				}
			}
			   
    	);
			
		
	});
	
		$('#modal_info').on('hide.bs.modal',function() {//提示模糊框隐藏时候触发
       		 location.reload();  	//刷新当前页面
    	});
	
	

});




function validAddPatientType() {
    var flag = true;

	
	
	var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");	
	var typeName = $.trim($("#addType").val());
	if(typeName == ""){
		 $('#addType').parent().addClass("has-error");
        $('#addType').next().text("请输入病人类型名称");
        $("#addType").next().show();
        flag = false;
	}else if(!reg.test(typeName)){
		 $('#addType').parent().addClass("has-error");
        $('#addType').next().text("病人类型名称必须为中文");
        $("#addType").next().show();
		 flag = false;
	}else {
        $('#addType').parent().removeClass("has-error");
        $('#addType').next().text("");
        $("#addType").next().hide();
    }
	
	var maxNum = $.trim($("#addMaxNum").val());
	if(maxNum == ""){
		 $('#addMaxNum').parent().addClass("has-error");
        $('#addMaxNum').next().text("请输入最大分发数量");
        $("#addMaxNum").next().show();
        flag = false;
	}else if(maxNum<=0 || maxNum!=parseInt(maxNum)){
    	$('#addMaxNum').parent().addClass("has-error");
        $('#addMaxNum').next().text("最大分发数量必须为正整数");
        $("#addMaxNum").next().show();
        flag = false;
	} else {
        $('#addMaxNum').parent().removeClass("has-error");
        $('#addMaxNum').next().text("");
        $("#addMaxNum").next().hide();
    } 
	
	
	
	var bday = $.trim($("#addBday").val());
	if(bday == ""){
		 $('#addBday').parent().addClass("has-error");
        $('#addBday').next().text("请输入最大分发天数");
        $("#addBday").next().show();
        flag = false;
	}else if(bday<=0 || bday!=parseInt(bday)){
    	$('#addBday').parent().addClass("has-error");
        $('#addBday').next().text("最大分发天数必须为正整数");
        $("#addBday").next().show();
        flag = false;
	} else {
        $('#addBday').parent().removeClass("has-error");
        $('#addBday').next().text("");
        $("#addBday").next().hide();
    } 
	
	
	var penalty = $.trim($("#addPenalty").val());
	if(penalty == ""){
		 $('#addPenalty').parent().addClass("has-error");
        $('#addPenalty').next().text("请输入逾期几日重发");
        $("#addPenalty").next().show();
        flag = false;
	}else if(penalty<=0 || penalty!=parseInt(penalty)){
    	$('#addPenalty').parent().addClass("has-error");
        $('#addPenalty').next().text("逾期几日重发必须为正整数");
        $("#addPenalty").next().show();
        flag = false;
	} else {
        $('#addPenalty').parent().removeClass("has-error");
        $('#addPenalty').next().text("");
        $("#addPenalty").next().hide();
    } 

	
	var resendDays = $.trim($("#addresendDays").val());
	if(resendDays == ""){
		 $('#addresendDays').parent().addClass("has-error");
        $('#addresendDays').next().text("请输入重发天数");
        $("#addresendDays").next().show();
        flag = false;
	}else if(resendDays<=0 || resendDays!=parseInt(resendDays)){
    	$('#addresendDays').parent().addClass("has-error");
        $('#addresendDays').next().text("重发天数必须为正整数");
        $("#addresendDays").next().show();
        flag = false;
	} else {
        $('#addresendDays').parent().removeClass("has-error");
        $('#addresendDays').next().text("");
        $("#addresendDays").next().hide();
    } 

	

    return flag;
}






function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


