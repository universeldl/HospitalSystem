function checkRetrieve(id) {
    let deliveryId = id;
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/retrieveManageAction_checkRetrieveInfoExists.action',
            params: "deliveryId=" + deliveryId,
            callback: function (data) {
                $('#loading').hide();
                if (data == 1) {
                    window.location.href = $.trim($("#check_retrieve").val()) + "?deliveryId=" + id;
                } else {
                    $("#addModal").modal("hide");//关闭模糊框
                    showInfo("该问卷尚未答卷，无法查看");
                }
            }
        }
    );
}

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

