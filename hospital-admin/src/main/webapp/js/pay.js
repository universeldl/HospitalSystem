$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function pay(id) {
    var postdata = "deliveryId=" + id;
    ajax(
        {
            method: 'POST',
            url: 'doctor/forfeitManageAction_payForfeit.action',
            params: postdata,
            callback: function (data) {
                if (data == -1) {
                    showInfo("请先去答卷,再来设置延期");
                } else if (data == 1) {
                    showInfo("设置成功");
                } else if (data == -2) {
                    showInfo("已设置过该延期");
                } else {
                    showInfo("设置失败");
                }

            }
        }
    );


}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


