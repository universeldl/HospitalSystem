$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function getForfeitInfoById(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'patient/forfeitAction_getForfeitInfoById.action',
            params: "deliveryId=" + id,
            type: "json",
            callback: function (data) {

                $('#loading').hide();
                $("#deliveryId").val(data.deliveryId);
                $("#surveyName").val(data.deliveryInfo.survey.surveyName);
                $("#surveyType").val(data.deliveryInfo.survey.surveyType.typeName);
                $("#openID").val(data.deliveryInfo.patient.openID);
                $("#patientName").val(data.deliveryInfo.patient.name);
                $("#patientType").val(data.deliveryInfo.patient.patientType.patientTypeName);
                $("#overday").val(data.deliveryInfo.overday);
                if (data.isPay == 0) {
                    $("#state").val("未设置延期");
                } else {
                    $("#state").val("已设置延期");
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


