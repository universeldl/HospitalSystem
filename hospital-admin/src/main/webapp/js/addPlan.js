window.onload = new function () {
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
};

/**
 * ajax提交添加随访计划的信息
 * @param {Object} '#addPlan'
 */
$(function () {


    $('#addPlan').click(function () {

        if (!validAddPlan()) {
            return;
        }

        var postdata = "beginAge=" + $.trim($("#addBeginAge").val()) + "&patientType=" + $.trim($("#addPatientType").val())
            + "&endAge=" + $.trim($("#addEndAge").val()) + "&sex=" + $.trim($("#addSex").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/planManageAction_addPlan.action',
                params: postdata,
                callback: function (data) {
                    if (data == 1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");

                    } else if (data == -1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("该计划已存在");
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


function validAddPlan() {
    var flag = true;

    var beginAge = $.trim($("#addBeginAge").val());
    if (beginAge == "") {
        $('#addBeginAge').parent().addClass("has-error");
        $('#addBeginAge').next().text("年龄下限不能为空");
        $('#addBeginAge').next().show();
        flag = false;
    }
    else if (isNaN(parseInt(beginAge)) || parseInt(beginAge) < 0 || parseInt(beginAge) > 99) {
        $('#addBeginAge').parent().addClass("has-error");
        $('#addBeginAge').next().text("年龄下限必须为0～99之间的正整数");
        $('#addBeginAge').next().show();
        flag = false;
    } else {
        $('#addBeginAge').parent().removeClass("has-error");
        $('#addBeginAge').next().text("");
        $('#addBeginAge').next().hide();
    }

    var endAge = $.trim($("#addEndAge").val());
    if (endAge == "") {
        $('#addEndAge').parent().addClass("has-error");
        $('#addEndAge').next().text("年龄上限不能为空");
        $('#addEndAge').next().show();
        flag = false;
    }
    else if (isNaN(parseInt(endAge)) || parseInt(endAge) < 0 || parseInt(endAge) > 99) {
        $('#addEndAge').parent().addClass("has-error");
        $('#addEndAge').next().text("年龄上限必须为0～99之间的正整数");
        $('#addEndAge').next().show();
        flag = false;
    }
    else if ( parseInt(endAge) <  parseInt(beginAge) ) {
        $('#addEndAge').parent().addClass("has-error");
        $('#addEndAge').next().text("年龄上限不能低于年龄下限");
        $('#addEndAge').next().show();
        flag = false;
    } else {
        $('#addEndAge').parent().removeClass("has-error");
        $('#addEndAge').next().text("");
        $('#addEndAge').next().hide();
    }

    var sex = $.trim($("#addSex").val());
    if (sex == -1) {
        $('#addSex').parent().addClass("has-error");
        $('#addSex').next().text("请选择病人性别");
        $("#addSex").next().show();
        flag = false;
    } else {
        $('#addSex').parent().removeClass("has-error");
        $('#addSex').next().text("");
        $("#addSex").next().hide();
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

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

