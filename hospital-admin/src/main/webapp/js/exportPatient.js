function exportPatient() {
    //获得basePath
    basePath = $('#basePath').val();
    $('#loading').show();
    ajax(
        {
            method: 'GET',
            url: 'doctor/patientManageAction_exportPatient.action',
            callback: function (data) {
                $('#loading').hide();
                showInfo("数据已导出：<a href='" + basePath + data + "'>点击下载</a>");
            }
        }
    );

}

function showInfo(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}
