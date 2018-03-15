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
            url: "doctor/surveyManageAction_findAllSurveys.action",
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                // 循环遍历每个问卷，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].surveyId;//设置op的实际值为当前的问卷编号
                    var textNode = document.createTextNode(data[index].surveyName);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
                    var att = document.createAttribute("name");
                    att.value = "addSurveyName";// + index;
                    op.setAttributeNode(att);

                    document.getElementById("addSurveys").appendChild(op);
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

        var postdata = "active=1&patientType=" + $.trim($("#addpatientType").val())
            + "&oldPatientOnly=" + $.trim($("#addOldPatientOnly").val())
            + "&sex=" + $.trim($("#addSex").val()) + "&" + $("#addForm").serialize();
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

    var oldPatientOnly = $.trim($("#addOldPatientOnly").val());
    if (sex == -1) {
        $('#addOldPatientOnly').parent().addClass("has-error");
        $('#addOldPatientOnly').next().text("请选择是否仅限既往病例");
        $("#addOldPatientOnly").next().show();
        flag = false;
    } else {
        $('#addOldPatientOnly').parent().removeClass("has-error");
        $('#addOldPatientOnly').next().text("");
        $("#addOldPatientOnly").next().hide();
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

    var numSurvey = 0;
    $("#addSurveys option:selected").each(function(){
        numSurvey++;
    });

    if (numSurvey == 0) {
        $("#addSurveys").parent().addClass("has-error");
        $("#addSurveys").next().text("至少需要选择一份问卷");
        $("#addSurveys").next().show();
        flag = false;
    } else {
        $("#addSurveys").parent().removeClass("has-error");
        $("#addSurveys").next().text("");
        $("#addSurveys").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

