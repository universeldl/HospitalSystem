function exportSinglePatient(id) {
    //获得basePath
    let basePath = $('#basePath').val();
    ajax(
        {
            method: 'POST',
            url: 'doctor/patientManageAction_exportSinglePatient.action',
            params: 'patientId=' + id,
            type: 'json',
            callback: function (data) {
                showInfo("数据已导出：<a href='" + basePath + data.filePath + "'>点击下载</a>");
            }
        }
    );
}

function showInfo(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}
