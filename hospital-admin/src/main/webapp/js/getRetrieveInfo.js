$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function getRetrieveInfoById(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/retrieveManageAction_getRetrieveInfoById.action',
            params: "deliveryId=" + id,
            type: "json",
            callback: function (data) {

                $('#loading').hide();
                $("#deliveryId").val(data.deliveryId);
                $("#surveyName").val(data.surveyName);
                $("#surveyType").val(data.typeName);
                $("#openID").val(data.openId);
                $("#patientName").val(data.patientName);
                $("#patientType").val(data.patientType);
                $("#overday").val(data.overday);
                $("#doctor").val(data.doctorName);
            }
        }
    );
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


