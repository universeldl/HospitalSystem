/**
 * ajax提交添加医生的信息
 * @param {Object} '#addDoctor'
 */
$(function () {


    $('#addDoctor').click(function () {

        if (!validAddDoctor()) {
            return;
        }
        var postdata = "username=" + $.trim($("#addUsername").val())
            + "&name=" + $.trim($("#addName").val())
            + "&phone=" + $.trim($("#addPhone").val())
            + "&hospitalId=" + $.trim($("#addHospital").val());

        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/doctorManageAction_addDoctor.action',
                params: postdata,
                callback: function (data) {
                $('#loading').hide();
                    if (data == 1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");

                    } else if (data == -1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("该医生已存在");
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

    $('#btn_add').click(function () {
        $("#addHospital option[value!=-1]").remove();//移除先前的选项
        $('#loading').show();
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
                        document.getElementById("addHospital").appendChild(op);
                    }
                }
            }
        );
    });

});


function validAddDoctor() {
    var flag = true;

    var username = $.trim($("#addUsername").val());
    if (username == "") {
        $('#addUsername').parent().addClass("has-error");
        $('#addUsername').next().text("请输入用户名");
        $("#addUsername").next().show();
        flag = false;
    } else if (username.length < 2 || username.length > 15) {
        $("#addUsername").parent().addClass("has-error");
        $("#addUsername").next().text("用户名长度必须在2~15之间");
        $("#addUsername").next().show();
        flag = false;
    } else {
        $('#addUsername').parent().removeClass("has-error");
        $('#addUsername').next().text("");
        $("#addUsername").next().hide();
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

    var hospitalId = $.trim($("#addHospital").val());
    if (hospitalId == "-1") {
        $('#addHospital').parent().addClass("has-error");
        $('#addHospital').next().text("请选择所属医院");
        $("#addHospital").next().show();
        flag = false;
    } else {
        $('#addHospital').parent().removeClass("has-error");
        $('#addHospital').next().text("");
        $("#addHospital").next().hide();
    }
    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


