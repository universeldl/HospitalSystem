$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function resendSurvey(id) {
    var postdata = "deliveryId=" + id;
    ajax(
        {
            method: 'POST',
            url: 'doctor/deliveryManageAction_resendSurvey.action',
            params: postdata,
            callback: function (data) {
                if (data == 1) {
                    showInfo("重发成功");
                } else if (data == -1) {
                    showInfo("已经答卷,无法重发");
                } else if (data == -2) {
                    showInfo("该答卷重发过了,无法重发");
                } else if (data == -3) {
                    showInfo("已超重发期了,无法进行重发,请尽快去答卷和设置提醒");
                } else if (data == -0) {
                    showInfo("重发失败");
                }
            }
        }
    );


}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


