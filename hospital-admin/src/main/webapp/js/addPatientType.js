$(function () {


    $('#addPatientType').click(function () {


        if (!validAddPatientType()) {
            return;
        }

        var postdata = "patientTypeName=" + $.trim($("#addType").val());
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/patientTypeManageAction_addPatientType.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#addModal").modal("hide");//关闭模糊框		
                        showInfo("添加成功");

                    } else {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加失败");
                    }

                }
            }
        );


    });

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function validAddPatientType() {
    var flag = true;

    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var typeName = $.trim($("#addType").val());
    if (typeName == "") {
        $('#addType').parent().addClass("has-error");
        $('#addType').next().text("请输入病人类型名称");
        $("#addType").next().show();
        flag = false;
    } else if (!reg.test(typeName)) {
        $('#addType').parent().addClass("has-error");
        $('#addType').next().text("病人类型名称必须为中文");
        $("#addType").next().show();
        flag = false;
    } else {
        $('#addType').parent().removeClass("has-error");
        $('#addType').next().text("");
        $("#addType").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


