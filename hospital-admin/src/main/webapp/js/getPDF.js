$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});



function getPDFURL(fileName) {
    alert(fileName)
    var postdata = "imgName=" + fileName;
    var basePath = $('#basePath').val();

    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/retrieveManageAction_IoReadPDF.action',
            params: postdata,
            callback: function (data) {
                $('#loading').hide();
                showInfo("PDF链接生成完毕：<a href='" + basePath + data + "'>点击下载</a>");
            }
        }
    );

}



function showInfo(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}

