/**
 * ajax提交修改医生的信息
 * @param {Object} '#updateType'
 */
$(function () {


    $('#updateType').click(function () {


        if (!validUpdatePatientType()) {
            return;
        }

        var postdata = "id=" + $.trim($("#patientTypeId").val())
            + "&patientTypeName=" + replaceSpectialChar($.trim($("#patientTypeName").val()));
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/patientTypeManageAction_updatePatientType.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#updateModal").modal("hide");//关闭模糊框		
                        showInfo("修改成功");

                    } else {
                        $("#updateinfo").modal("hide");//关闭模糊框
                        showInfo("修改失败");
                    }

                }
            }
        );


    });

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function updatePatientType(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/patientTypeManageAction_getPatientType.action',
            params: "id=" + id,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                $("#patientTypeId").val(data.patientTypeId);
                $("#patientTypeName").val(data.patientTypeName);
            }
        }
    );


}


function validUpdatePatientType() {
    var flag = true;


    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var typeName = $.trim($("#patientTypeName").val());
    if (typeName == "") {
        $('#patientTypeName').parent().addClass("has-error");
        $('#patientTypeName').next().text("请输入病人类型名称");
        $("#patientTypeName").next().show();
        flag = false;
    } else if (!reg.test(typeName)) {
        $('#patientTypeName').parent().addClass("has-error");
        $('#patientTypeName').next().text("病人类型名称必须为中文");
        $("#patientTypeName").next().show();
        flag = false;
    } else {
        $('#patientTypeName').parent().removeClass("has-error");
        $('#patientTypeName').next().text("");
        $("#patientTypeName").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

