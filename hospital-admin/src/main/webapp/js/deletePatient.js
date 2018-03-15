function deletePatient(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/patientManageAction_deletePatient.action',
            params: "patientId=" + id,
            callback: function (data) {
                $('#loading').hide();
                if (data == 1) {
                    showInfo("删除成功");
                } else if (data == -1) {
                    showInfo("该病人有未答卷记录,不能删除");
                } else if (data == -2) {
                    showInfo("该病人有未设置的延期,不能删除");
                } else {
                    showInfo("删除失败");
                }

            }
        }
    );


}

$('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
    location.reload();  	//刷新当前页面
});


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


