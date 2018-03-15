$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function retrieveSurvey(id) {
    var postdata = "deliveryId=" + id;
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/retrieveManageAction_retrieveSurvey.action',
            params: postdata,
            callback: function (data) {
                $('#loading').hide();
                if (data == 1) {
                    showInfo("答卷成功");

                } else if (data == -1) {
                    showInfo("已经回答过问卷");
                } else if (data == 2) {
                    showInfo("答卷成功,请设置逾期提醒");
                } else {
                    showInfo("答卷失败");
                }

            }
        }
    );


}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


