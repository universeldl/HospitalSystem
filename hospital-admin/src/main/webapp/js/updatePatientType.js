/**
 * ajax提交修改医生的信息
 * @param {Object} '#updateType'
 */
$(function () {


    $('#updateType').click(function () {


        if (!validUpdatePatientType()) {
            return;
        }

        var postdata = "id=" + $.trim($("#patientTypeId").val()) + "&maxNum=" + $.trim($("#maxNum").val()) + "&bday=" + $.trim($("#bday").val()) + "&penalty=" + $.trim($("#penalty").val())
            + "&patientTypeName=" + $.trim($("#patientTypeName").val()) + "&resendDays=" + $.trim($("#resendDays").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/patientTypeManageAction_updatePatientType.action',
                params: postdata,
                callback: function (data) {
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
    ajax(
        {
            method: 'POST',
            url: 'doctor/patientTypeManageAction_getPatientType.action',
            params: "id=" + id,
            type: "json",
            callback: function (data) {
                $("#patientTypeId").val(data.patientTypeId);
                $("#patientTypeName").val(data.patientTypeName);
                $("#maxNum").val(data.maxNum);
                $("#bday").val(data.bday);
                $("#penalty").val(data.penalty);
                $("#resendDays").val(data.resendDays);
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

    var maxNum = $.trim($("#maxNum").val());
    if (maxNum == "") {
        $('#maxNum').parent().addClass("has-error");
        $('#maxNum').next().text("请输入最大分发数量");
        $("#maxNum").next().show();
        flag = false;
    } else if (maxNum <= 0 || maxNum != parseInt(maxNum)) {
        $('#maxNum').parent().addClass("has-error");
        $('#maxNum').next().text("最大分发数量必须为正整数");
        $("#maxNum").next().show();
        flag = false;
    } else {
        $('#maxNum').parent().removeClass("has-error");
        $('#maxNum').next().text("");
        $("#maxNum").next().hide();
    }


    var bday = $.trim($("#bday").val());
    if (bday == "") {
        $('#bday').parent().addClass("has-error");
        $('#bday').next().text("请输入最大分发天数");
        $("#bday").next().show();
        flag = false;
    } else if (bday <= 0 || bday != parseInt(bday)) {
        $('#bday').parent().addClass("has-error");
        $('#bday').next().text("最大分发天数必须为正整数");
        $("#bday").next().show();
        flag = false;
    } else {
        $('#bday').parent().removeClass("has-error");
        $('#bday').next().text("");
        $("#bday").next().hide();
    }


    var penalty = $.trim($("#penalty").val());
    if (penalty == "") {
        $('#penalty').parent().addClass("has-error");
        $('#penalty').next().text("请输入逾期几日重发");
        $("#penalty").next().show();
        flag = false;
    } else if (penalty <= 0 || penalty != parseInt(penalty)) {
        $('#penalty').parent().addClass("has-error");
        $('#penalty').next().text("逾期几日重发必须为正整数");
        $("#penalty").next().show();
        flag = false;
    } else {
        $('#penalty').parent().removeClass("has-error");
        $('#penalty').next().text("");
        $("#penalty").next().hide();
    }


    var resendDays = $.trim($("#resendDays").val());
    if (resendDays == "") {
        $('#resendDays').parent().addClass("has-error");
        $('#resendDays').next().text("请输入重发天数");
        $("#resendDays").next().show();
        flag = false;
    } else if (resendDays <= 0 || resendDays != parseInt(resendDays)) {
        $('#resendDays').parent().addClass("has-error");
        $('#resendDays').next().text("重发天数必须为正整数");
        $("#resendDays").next().show();
        flag = false;
    } else {
        $('#resendDays').parent().removeClass("has-error");
        $('#resendDays').next().text("");
        $("#resendDays").next().hide();
    }


    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


