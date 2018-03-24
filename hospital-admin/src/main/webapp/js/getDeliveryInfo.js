$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function getDeliveryInfoById(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/deliveryManageAction_getDeliveryInfoById.action',
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
                if (data.state == -1) {
                    $("#state").val("已答卷");
                } else if (data.state == 0) {
                    $("#state").val("未答卷");
                } else if (data.state == -2) {
                    $("#state").val("逾期未答卷");
                } else if (data.state > 0) {
                    $("#state").val("重发未答卷");
                }
                $("#doctor").val(data.doctorName);
            }
        }
    );


}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


