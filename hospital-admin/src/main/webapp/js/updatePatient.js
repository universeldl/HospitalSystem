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
            + "&name=" + replaceSpectialChar($.trim($("#updateName").val())) + "&phone=" + $.trim($("#updatePhone").val())
            + "&openID=" + $.trim($("#updateOpenID").val()) + "&addnDoctorId=" + $.trim($("#updateAddnDoctor").val())
            + "&createTime=" + $.trim($("#updateCreateTime").val()) + "&doctorId=" + $.trim($("#updateDoctor").val())
            + "&birthday=" + $.trim($("#updateBirthday").val()) + "&oldPatient=" + $.trim($("#updateOldPatient").val());
        if ($.trim($("#updateEmail").val()) != "") {
            postdata = postdata + "&email=" + $.trim($("#updateEmail").val());
        }
        $('#loading').show();

        //alert(postdata)
        ajax(
            {
                method: 'POST',
                url: 'doctor/patientManageAction_updatePatient.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#updateModal").modal("hide");//关闭模糊框		
                        showInfo("修改成功");
                    } else if (data == -2) {
                        $("#updateinfo").modal("hide");//关闭模糊框
                        showInfo("修改失败,直属医生和共享医生不能为同一人");
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
    $("#updateAddnDoctor option[value!=-1]").remove();//移除先前的选项
    $('#loading').show();
    ajax(
        {
            url: "doctor/patientManageAction_getAllPatientTypes.action",
            type: "json",
            callback: function (data) {
                $('#updateAddnDoctor').html('<option value="-1">无</option>');
                $('#updateDoctor').html('<option value="-1">无</option>');
                $('#updatePatientType').html('<option value="-1">无</option>');

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
                        method: 'GET',
                        url: "doctor/patientManageAction_getAllDoctors.action",
                        type: "json",
                        async: false,
                        callback: function (data) {
                            // 循环遍历每个医生，每个医生姓名生成一个option对象，添加到<select>中
                            for (var index in data) {
                                var op = document.createElement("option");//创建一个指名名称元素
                                op.value = data[index].aid;//设置op的实际值为当前的医生aid
                                var textNode = document.createTextNode(data[index].name);//创建文本节点--医生姓名
                                op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
                                document.getElementById("updateAddnDoctor").appendChild(op);
                            }
                            for (var index in data) {
                                var op = document.createElement("option");//创建一个指名名称元素
                                op.value = data[index].aid;//设置op的实际值为当前的医生aid
                                var textNode = document.createTextNode(data[index].name);//创建文本节点--医生姓名
                                op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
                                document.getElementById("updateDoctor").appendChild(op);
                            }

                            ajax(
                                {
                                    method: 'POST',
                                    url: 'doctor/patientManageAction_getPatient.action',
                                    params: "patientId=" + id,
                                    type: "json",
                                    callback: function (data) {
                                        $('#loading').hide();
                                        $("#updatePatientID").val(data.patientId);
                                        $("#updateOpenID").val(data.openID);
                                        $("#updateName").val(data.name);
                                        $("#updateEmail").val(data.email);
                                        $("#updatePhone").val(data.phone);
                                        $("#updatePatientType").val(data.patientType.patientTypeId);

                                        $("#updateOldPatient option:checked").attr("selected", "");
                                        var oldPatient = data.oldPatient;
                                        $("#updateOldPatient option[value='"+oldPatient+"']").attr("selected", "selected");

                                        $("#updateAddnDoctor option:checked").attr("selected", "");
                                        var addnDoctorId = data.addnDoctorId;
                                        $("#updateAddnDoctor option[value='"+addnDoctorId+"']").attr("selected", "selected");

                                        $("#updateDoctor option:checked").attr("selected", "");
                                        var doctorId = data.doctorId;
                                        $("#updateDoctor option[value='"+doctorId+"']").attr("selected", "selected");

                                        $('#updateCreateTime').val(data.createTime);
                                        $('#updateBirthday').val(data.birthday)
                                    }
                                }
                            );
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
        /*
    } else if (!reg.test(name)) {
        $('#updateName').parent().addClass("has-error");
        $('#updateName').next().text("真实姓名必须为中文");
        $("#updateName").next().show();
        flag = false;
        */
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
        /*
        make email address optionnal
        $('#updateEmail').parent().addClass("has-error");
        $('#updateEmail').next().text("请输入邮箱");
        $("#updateEmail").next().show();
        flag = false;
        */
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


    var addnDoctor = $.trim($("#updateAddnDoctor").val());
    if (addnDoctor == -1) {
        /* addndoctor is not a required option
        $('#updateAddnDoctor').parent().addClass("has-error");
        $('#updateAddnDoctor').next().text("请选择共享医生");
        $("#updateAddnDoctor").next().show();
        flag = false;
        */
    } else {
        $('#updateAddnDoctor').parent().removeClass("has-error");
        $('#updateAddnDoctor').next().text("");
        $("#updateAddnDoctor").next().hide();
    }

    var doctor = $.trim($("#updateDoctor").val());
    if (doctor == -1) {
         $('#updateDoctor').parent().addClass("has-error");
         $('#updateDoctor').next().text("请选择直属医生");
         $("#updateDoctor").next().show();
         flag = false;
    } else {
        $('#updateDoctor').parent().removeClass("has-error");
        $('#updateDoctor').next().text("");
        $("#updateDoctor").next().hide();
    }

    if(doctor == addnDoctor) {
        $('#updateAddnDoctor').parent().addClass("has-error");
        $('#updateAddnDoctor').next().text("共享医生和直属医生不能相同");
        $("#updateAddnDoctor").next().show();
        $('#updateDoctor').parent().addClass("has-error");
        $('#updateDoctor').next().text("共享医生和直属医生不能相同");
        $("#updateDoctor").next().show();
        flag = false;
    }

    var reg2 = /^([0-9])+-([0-9])+-([0-9])/;
    var updateCreateTime = $.trim($("#updateCreateTime").val());
    if (updateCreateTime == "") {
        alert("请填写正确的注册日期");
        flag = false;
    } else if (!reg2.test(updateCreateTime)) {
        alert("请使用正确的日期格式（1990-09-10）");
        flag = false;
    } else {
        var createTime = new Date(($("#updateCreateTime").val()).replace(/-/g,"/"));
        var curDate = new Date();
        if (createTime >= curDate) {
            alert("请填写正确的注册日期");
            flag = false;
        }
    }

    var updateBirthday = $.trim($("#updateBirthday").val());
    if (updateBirthday == "") {
        alert("请填写正确的出生日期");
        return false;
    } else if (!reg2.test(updateBirthday)) {
        alert("请使用正确的日期格式（1990-09-10）");
        return false;
    } else {
        var bday = new Date(($("#updateBirthday").val()).replace(/-/g,"/"));
        var curDate = new Date();
        if (bday >= curDate) {
            alert("请填写正确的出生日期");
            return false;
        }
    }

    return flag;
}

function restorePatient(id) {
    if(confirm('确定要还原此病人吗?')) {
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/patientManageAction_restorePatient.action',
                params: "patientId=" + id,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        showInfo("还原成功");
                    } else {
                        showInfo("还原失败");
                    }

                }
            }
        );
    }
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


