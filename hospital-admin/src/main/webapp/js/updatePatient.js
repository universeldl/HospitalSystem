/**
 * ajax提交修改医生的信息
 * @param {Object} '#updatePatient'
 */
$(function () {


    $('#updatePatient').click(function () {


        if (!validUpdatePatient()) {
            return;
        }

        var postdata = "patientId=" + $.trim($("#updatePatientID").val()) + "&patientType=" + $.trim($("#updatePatientType").val())
            + "&name=" + $.trim($("#updateName").val()) + "&phone=" + $.trim($("#updatePhone").val()) + "&email=" + $.trim($("#updateEmail").val())
            + "&openID=" + $.trim($("#updateOpenID").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/patientManageAction_updatePatient.action',
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


/**
 * 显示修改前的数据在输入框中
 * @param {Object} id
 */
function updatePatient(id) {
    $("#updatePatientType option[value!=-1]").remove();//移除先前的选项
    ajax(
        {
            url: "doctor/patientTypeManageAction_getAllPatientTypes.action",
            type: "json",
            callback: function (data) {
                // 循环遍历每个病人分类，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].patientTypeId;//设置op的实际值为当前的病人分类编号
                    var textNode = document.createTextNode(data[index].patientTypeName);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                    document.getElementById("updatePatientType").appendChild(op);
                }
                ajax(
                    {
                        method: 'POST',
                        url: 'doctor/patientManageAction_getPatient.action',
                        params: "patientId=" + id,
                        type: "json",
                        callback: function (data) {
                            $("#updatePatientID").val(data.patientId);
                            $("#updateOpenID").val(data.openID);
                            $("#updateName").val(data.name);
                            $("#updateEmail").val(data.email);
                            $("#updatePhone").val(data.phone);
                            $("#updatePatientType").val(data.patientType.patientTypeId);
                        }
                    }
                );
            }


        }
    );


}


function validUpdatePatient() {
    var flag = true;

    var openID = $.trim($("#updateOpenID").val());
    if (openID == "") {
        $('#updateOpenID').parent().addClass("has-error");
        $('#updateOpenID').next().text("请输入病人用户名");
        $("#updateOpenID").next().show();
        flag = false;
    } else {
        $('#updateOpenID').parent().removeClass("has-error");
        $('#updateOpenID').next().text("");
        $("#updateOpenID").next().hide();
    }


    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var name = $.trim($("#updateName").val());
    if (name == "") {
        $('#updateName').parent().addClass("has-error");
        $('#updateName').next().text("请输入真实姓名");
        $("#updateName").next().show();
        flag = false;
    } else if (!reg.test(name)) {
        $('#updateName').parent().addClass("has-error");
        $('#updateName').next().text("真实姓名必须为中文");
        $("#updateName").next().show();
        flag = false;
    } else {
        $('#updateName').parent().removeClass("has-error");
        $('#updateName').next().text("");
        $("#updateName").next().hide();
    }

    var phone = $.trim($("#updatePhone").val());
    if (phone == "") {
        $('#updatePhone').parent().addClass("has-error");
        $('#updatePhone').next().text("请输入联系号码");
        $("#updatePhone").next().show();
        flag = false;
    } else if (!(/^1[34578]\d{9}$/.test(phone))) {
        //电话号码格式的校验
        $('#updatePhone').parent().addClass("has-error");
        $('#updatePhone').next().text("手机号码有误");
        $("#updatePhone").next().show();
        return false;
    } else {
        $('#updatePhone').parent().removeClass("has-error");
        $('#updatePhone').next().text("");
        $("#updatePhone").next().hide();
    }

    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    var email = $.trim($("#updateEmail").val());
    if (email == "") {
        $('#updateEmail').parent().addClass("has-error");
        $('#updateEmail').next().text("请输入邮箱");
        $("#updateEmail").next().show();
        flag = false;
    } else if (!reg.test(email)) {
        //邮箱格式的校验
        $('#updateEmail').parent().addClass("has-error");
        $('#updateEmail').next().text("邮箱格式有误");
        $("#updateEmail").next().show();
        return false;
    } else {
        $('#updateEmail').parent().removeClass("has-error");
        $('#updateEmail').next().text("");
        $("#updateEmail").next().hide();
    }


    var patientType = $.trim($("#updatePatientType").val());
    if (patientType == -1) {
        $('#updatePatientType').parent().addClass("has-error");
        $('#updatePatientType').next().text("请选择病人类型");
        $("#updatePatientType").next().show();
        flag = false;
    } else {
        $('#updatePatientType').parent().removeClass("has-error");
        $('#updatePatientType').next().text("");
        $("#updatePatientType").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


