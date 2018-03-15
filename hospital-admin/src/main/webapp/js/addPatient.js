window.onload = new function () {
    $('#loading').show();
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

                    document.getElementById("addpatientType").appendChild(op);
                }
            }
        }
    );
    ajax(
        {
            url: "doctor/doctorManageAction_getAllDoctors.action",
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                // 循环遍历每个医生，每个医生姓名生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].aid;//设置op的实际值为当前的医生aid
                    var textNode = document.createTextNode(data[index].name);//创建文本节点--医生姓名
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                    document.getElementById("addAddnDoctor").appendChild(op);
                }
            }
        }
    );
};

/**
 * ajax提交添加病人的信息
 * @param {Object} '#addPatient'
 */
$(function () {

    $('#addPatient').click(function () {

        if (!validAddPatient()) {
            return;
        }

        var postdata = "openID=" + $.trim($("#addOpenID").val()) + "&name=" + $.trim($("#addName").val())
            + "&phone=" + $.trim($("#addPhone").val()) + "&sex=" + $.trim($("#addPatientSex").val())
            + "&patientType=" + $.trim($("#addpatientType").val()) + "&birthday=" + $.trim($("#datepicker").val())
            + "&email=" + $.trim($("#addEmail").val())+ "&addnDoctorId=" + $.trim($("#addAddnDoctor").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/patientManageAction_addPatient.action',
                params: postdata,
                callback: function (data) {
                    if (data == 1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");
                    } else if (data == -1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加失败, 该病人已存在");
                    } else if (data == -2) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加失败, 共享医生与直属医生不能相同");
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

function validAddPatient() {
    var flag = true;

    var birthday = $.trim($("#datepicker").val());
    if (birthday == "") {
        $('#datepicker').parent().addClass("has-error");
        $('#datepicker').next().text("请选择病人出生日期");
        $("#datepicker").next().show();
        flag = false;
    } else {
        $('#datepicker').parent().removeClass("has-error");
        $('#datepicker').next().text("");
        $("#datepicker").next().hide();
    }

    var openID = $.trim($("#addOpenID").val());
    if (openID == "") {
        $('#addOpenID').parent().addClass("has-error");
        $('#addOpenID').next().text("请输入病人用户名");
        $("#addOpenID").next().show();
        flag = false;
    } else {
        $('#addOpenID').parent().removeClass("has-error");
        $('#addOpenID').next().text("");
        $("#addOpenID").next().hide();
    }

    var sex = $.trim($("#addPatientSex").val());
    if (sex == -1) {
        $('#addPatientSex').parent().addClass("has-error");
        $('#addPatientSex').next().text("请选择病人性别");
        $("#addPatientSex").next().show();
        flag = false;
    } else {
        $('#addPatientSex').parent().removeClass("has-error");
        $('#addPatientSex').next().text("");
        $("#addPatientSex").next().hide();
    }

    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var name = $.trim($("#addName").val());
    if (name == "") {
        $('#addName').parent().addClass("has-error");
        $('#addName').next().text("请输入真实姓名");
        $("#addName").next().show();
        flag = false;
    } else if (!reg.test(name)) {
        $('#addName').parent().addClass("has-error");
        $('#addName').next().text("真实姓名必须为中文");
        $("#addName").next().show();
        flag = false;
    } else {
        $('#addName').parent().removeClass("has-error");
        $('#addName').next().text("");
        $("#addName").next().hide();
    }

    var phone = $.trim($("#addPhone").val());
    if (phone == "") {
        $('#addPhone').parent().addClass("has-error");
        $('#addPhone').next().text("请输入联系号码");
        $("#addPhone").next().show();
        flag = false;
    } else if (!(/^1[34578]\d{9}$/.test(phone))) {
        //电话号码格式的校验
        $('#addPhone').parent().addClass("has-error");
        $('#addPhone').next().text("手机号码有误");
        $("#addPhone").next().show();
        return false;
    } else {
        $('#addPhone').parent().removeClass("has-error");
        $('#addPhone').next().text("");
        $("#addPhone").next().hide();
    }

    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    var email = $.trim($("#addEmail").val());
    if (email == "") {
        $('#addEmail').parent().addClass("has-error");
        $('#addEmail').next().text("请输入邮箱");
        $("#addEmail").next().show();
        flag = false;
    } else if (!reg.test(email)) {
        //邮箱格式的校验
        $('#addEmail').parent().addClass("has-error");
        $('#addEmail').next().text("邮箱格式有误");
        $("#addEmail").next().show();
        return false;
    } else {
        $('#addEmail').parent().removeClass("has-error");
        $('#addEmail').next().text("");
        $("#addEmail").next().hide();
    }


    var patientType = $.trim($("#addpatientType").val());
    if (patientType == -1) {
        $('#addpatientType').parent().addClass("has-error");
        $('#addpatientType').next().text("请选择病人类型");
        $("#addpatientType").next().show();
        flag = false;
    } else {
        $('#addpatientType').parent().removeClass("has-error");
        $('#addpatientType').next().text("");
        $("#addpatientType").next().hide();
    }


    var addnDoctor = $.trim($("#addAddnDoctor").val());
    if (addnDoctor == -1) {
        $('#addAddnDoctor').parent().addClass("has-error");
        $('#addAddnDoctor').next().text("请选择共享医生");
        $("#addAddnDoctor").next().show();
        flag = false;
    } else {
        $('#addAddnDoctor').parent().removeClass("has-error");
        $('#addAddnDoctor').next().text("");
        $("#addAddnDoctor").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

