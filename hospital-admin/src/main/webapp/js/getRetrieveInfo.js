$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function getRetrieveInfoById(id) {
    ajax(
        {
            method: 'POST',
            url: 'doctor/retrieveManageAction_getRetrieveInfoById.action',
            params: "deliveryId=" + id,
            type: "json",
            callback: function (data) {

                $("#deliveryId").val(data.deliveryId);
                $("#surveyName").val(data.survey.surveyName);
                $("#surveyType").val(data.survey.surveyType.typeName);
                $("#openID").val(data.patient.openID);
                $("#patientName").val(data.patient.name);
                $("#patientType").val(data.patient.patientType.patientTypeName);
                $("#overday").val(data.deliveryInfo.overday);
                $("#doctor").val(data.doctor.name);
            }
        }
    );
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


