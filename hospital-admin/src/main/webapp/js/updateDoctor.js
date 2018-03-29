/**
 * ajax提交修改医生的信息
 * @param {Object} '#updateDoctor'
 */
$(function () {


    $('#updateDoctor').click(function () {


        if (!validUpdateDoctor()) {
            return;
        }

        var postdata = "id=" + $.trim($("#updateId").val())
            + "&username=" + replaceSpectialChar($.trim($("#updateUsername").val()))
            + "&name=" + replaceSpectialChar($.trim($("#updateName").val()))
            + "&phone=" + $.trim($("#updatePhone").val())
            + "&hospitalId=" + $.trim($("#updateHospital").val());
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/doctorManageAction_updateDoctor.action',
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


/**
 * 获取需要修改用户信息
 * @param {Object} username 需要修改的用户名
 */
function updateDoctor(id) {
    $('#loading').show();
    $("#updateHospital option[value!=-1]").remove();//移除先前的选项
    ajax(
        {
            url: "doctorManageAction_getHospitals.action",
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                // 循环遍历每个问卷分类，每个名称生成一个option对象，添加到<select>中
                for (let index in data.hospitals) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data.hospitals[index].aid;//设置op的实际值为当前的问卷分类编号
                    var textNode = document.createTextNode(data.hospitals[index].name);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
                    document.getElementById("updateHospital").appendChild(op);
                }

                ajax(
                    {
                        method: 'POST',
                        url: 'doctor/doctorManageAction_getDoctor.action',
                        params: "id=" + id,
                        type: "json",
                        callback: function (data) {
                            $('#loading').hide();
                            $("#updateId").val(data.aid);
                            $("#updateUsername").val(data.username);
                            $("#updateName").val(data.name);
                            $("#updatePhone").val(data.phone);
                            $("#updatePwd").val(data.pwd);
                        }
                    }
                );
            }
        }
    );




}


function validUpdateDoctor() {
    var flag = true;

    var username = $.trim($("#updateUsername").val());
    if (username == "") {
        $('#updateUsername').parent().addClass("has-error");
        $('#updateUsername').next().text("请输入用户名");
        $("#updateUsername").next().show();
        flag = false;
    } else if (username.length < 2 || username.length > 15) {
        $("#updateUsername").parent().addClass("has-error");
        $("#updateUsername").next().text("用户名长度必须在2~15之间");
        $("#updateUsername").next().show();
        flag = false;
    } else {
        $('#updateUsername').parent().removeClass("has-error");
        $('#updateUsername').next().text("");
        $("#updateUsername").next().hide();
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

    var hospitalId = $.trim($("#updateHospital").val());
    if (hospitalId == "-1") {
        $('#updateHospital').parent().addClass("has-error");
        $('#updateHospital').next().text("请选择所属医院");
        $("#updateHospital").next().show();
        flag = false;
    } else {
        $('#updateHospital').parent().removeClass("has-error");
        $('#updateHospital').next().text("");
        $("#updateHospital").next().hide();
    }
    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


