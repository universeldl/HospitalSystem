$(function () {


    $('#btn_delivery').click(function () {

        var postdata = "openID=" + $.trim($("#deliveryPatientOpenID").val()) + "&pwd=" + $.trim($("#pwd").val());
    ajax(
        {
            method: 'POST',
            url: 'doctor/deliveryManageAction_deliverySurvey.action',
            params: postdata,
            callback: function (data) {
                if (data == 1) {
                    showInfo("分发成功");

                } else if (data == -1) {
                    showInfo("密码错误");
                } else if (data == -2) {
                    showInfo("分发数量已达上限");

                } else if (data == -3) {
                    showInfo("请先设置未设置的提醒");
                } else if (data == -4) {
                    showInfo("该问卷为馆内最后一本,无法分发");
                } else if (data == 2) {
                    showInfo("病人用户名有误,请重试");
                } else if (data == 3) {
                    showInfo("问卷编号码有误,请重试");
                } else {
                    showInfo("分发失败");
                }
            }
        }
    );


});

$('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发

    location.reload();  	//刷新当前页面
});


})
;


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


