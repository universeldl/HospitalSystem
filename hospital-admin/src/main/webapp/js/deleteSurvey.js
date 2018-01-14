


function deleteSurvey(id){
	ajax(
    		  {
			  	method:'POST',
	    		url:'doctor/surveyManageAction_deleteSurvey.action',
				params: "surveyId=" + id,
	    		callback:function(data) {
	    			if (data == 1) {
						showInfo("删除成功");
					}else if(data == -1){
						showInfo("该答卷有未答卷记录,不能删除");
					}else if(data==-2){
						showInfo("该答卷有未设置的延期,不能删除");
					}else{
						showInfo("删除失败");
					}
								
				}
			}
			   
    	);
			

}

$('#modal_info').on('hide.bs.modal',function() {//提示模糊框隐藏时候触发
       		 location.reload();  	//刷新当前页面
 });


function question(id){
	var question_action = $.trim($("#question_action").val()) +"?surveyId="+ id;

	 window.location.href = question_action;
}

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


